/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.honhimw.ms.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static io.github.honhimw.ms.support.DateTimeUtils.RFC_3339;
import static io.github.honhimw.ms.support.DateTimeUtils.RFC_3339_FORMATTER;

/**
 * @author hon_him
 * @since 2024-01-19
 */

public class GsonJsonHandler implements JsonHandler {

    private final Gson gson;

    /**
     * Creates a new instance of the GsonJsonHandler.
     */
    public GsonJsonHandler() {
        this(defaultBuilder().create());
    }

    /**
     * Creates a new instance of the GsonJsonHandler with the given gson.
     * @param gson configured gson instance
     */
    public GsonJsonHandler(Gson gson) {
        this.gson = gson;
    }

    /**
     * Returns a default GsonBuilder instance with custom type adapters and serialization policies.
     *
     * @return a GsonBuilder instance with default configurations
     */
    public static GsonBuilder defaultBuilder() {
        return new GsonBuilder()
            .setDateFormat(RFC_3339)
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.value(DateTimeFormatter.ISO_DATE.format(value));
                }

                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return LocalDate.parse(in.nextString(), DateTimeFormatter.ISO_DATE);
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.value(RFC_3339_FORMATTER.format(value));
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return LocalDateTime.parse(in.nextString(), RFC_3339_FORMATTER);
                }
            })
            .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                @Override
                public void write(JsonWriter out, Instant value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.value(value.toString());
                }

                @Override
                public Instant read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return LocalDateTime.parse(in.nextString(), RFC_3339_FORMATTER).toInstant(ZoneOffset.UTC);
                }
            })
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .setFieldNamingStrategy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapterFactory(new TypeAdapterFactory() {
                @Override
                public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                    Class<? super T> rawType = typeToken.getRawType();
                    if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class || !EnumValue.class.isAssignableFrom(rawType)) {
                        return null;
                    }
                    if (!rawType.isEnum()) {
                        rawType = rawType.getSuperclass(); // handle anonymous subclasses
                    }

                    final Map<String, Enum<?>> map = new HashMap<>();
                    Object[] enumConstants = rawType.getEnumConstants();
                    for (Object enumConstant : enumConstants) {
                        map.put(((EnumValue<?>) enumConstant).value(), (Enum<?>) enumConstant);
                    }

                    return new TypeAdapter<T>() {
                        @SuppressWarnings({"rawtypes"})
                        @Override
                        public void write(JsonWriter out, T value) throws IOException {
                            if (value == null) {
                                out.nullValue();
                                return;
                            }
                            out.value(((EnumValue) value).value());
                        }

                        @SuppressWarnings({"unchecked"})
                        @Override
                        public T read(JsonReader in) throws IOException {
                            if (in.peek() == JsonToken.NULL) {
                                in.nextNull();
                                return null;
                            }
                            return (T) map.get(in.nextString());
                        }
                    };
                }
            })
            ;
    }

    @Override
    public String toJson(Object o) {
        return gson.toJson(o);
    }

    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    @Override
    public <T> T fromJson(String json, TypeRef<T> typeRef) {
        return gson.fromJson(json, typeRef.getType());
    }

    @Override
    public <T> T transform(Object o, TypeRef<T> typeRef) {
        return gson.fromJson(gson.toJsonTree(o), typeRef.getType());
    }
}

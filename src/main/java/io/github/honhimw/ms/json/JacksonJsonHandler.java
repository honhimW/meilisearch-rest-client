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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static io.github.honhimw.ms.support.DateTimeUtils.RFC_3339;
import static io.github.honhimw.ms.support.DateTimeUtils.RFC_3339_FORMATTER;

/**
 * @author hon_him
 * @since 2023-12-29
 */

@Getter
public class JacksonJsonHandler implements JsonHandler {

    private final ObjectMapper objectMapper;

    public JacksonJsonHandler() {
        this(defaultBuilder().build());
    }

    public JacksonJsonHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("rawtypes")
    public static JsonMapper.Builder defaultBuilder() {
        JsonMapper.Builder builder = JsonMapper.builder();

        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE)); //yyyy-MM-dd
        javaTimeModule.addDeserializer(LocalDate.class,
            new LocalDateDeserializer(DateTimeFormatter.ISO_DATE)); //yyyy-MM-dd

        javaTimeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(RFC_3339_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(RFC_3339_FORMATTER));
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                SimpleDateFormat formatter = new SimpleDateFormat(RFC_3339);
                String formattedDate = formatter.format(value);
                gen.writeString(formattedDate);
            }
        }).addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                SimpleDateFormat formatter = new SimpleDateFormat(RFC_3339);
                try {
                    return formatter.parse(text);
                } catch (ParseException e) {
                    throw new IOException(e);
                }
            }
        });

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(EnumValue.class, new JsonSerializer<EnumValue>() {
            @Override
            public void serialize(EnumValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.value());
            }
        });
        simpleModule
            .setDeserializers(new SimpleDeserializers() {
                @Override
                public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
                    if (EnumValue.class.isAssignableFrom(type)) {
                        final Map<String, Enum<?>> map = new HashMap<>();
                        Object[] enumConstants = type.getEnumConstants();
                        for (Object enumConstant : enumConstants) {
                            map.put(((EnumValue<?>) enumConstant).value(), (Enum<?>) enumConstant);
                        }
                        return new JsonDeserializer<Enum<?>>() {
                            @Override
                            public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                String text = p.getText();
                                return map.get(text);
                            }
                        };
                    }
                    return super.findEnumDeserializer(type, config, beanDesc);
                }
            });

        builder
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .defaultDateFormat(new SimpleDateFormat(RFC_3339))
            .addModules(javaTimeModule, simpleModule)
            .defaultTimeZone(TimeZone.getDefault())
        ;
        return builder;
    }

    @Override
    public String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new IllegalArgumentException("json encode exception", e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("json decode exception", e);
        }
    }

    @Override
    public <T> T fromJson(String json, TypeRef<T> typeRef) {
        try {
            return objectMapper.readValue(json, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return typeRef.getType();
                }
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("json decode exception", e);
        }
    }
}

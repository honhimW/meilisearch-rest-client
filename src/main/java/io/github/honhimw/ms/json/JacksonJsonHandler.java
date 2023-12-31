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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import java.util.TimeZone;

/**
 * @author hon_him
 * @since 2023-12-29
 */

@Getter
public class JacksonJsonHandler implements JsonHandler {

    private final ObjectMapper objectMapper;

    public JacksonJsonHandler() {
        this.objectMapper = defaultBuilder().build();
    }

    public JacksonJsonHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JsonMapper.Builder defaultBuilder() {
        String RFC_3339 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
        DateTimeFormatter RFC_3339_FORMATTER = DateTimeFormatter.ofPattern(RFC_3339);
        JsonMapper.Builder builder = JsonMapper.builder();

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

        SimpleModule longAsStringModule = new SimpleModule();
        longAsStringModule.addSerializer(Long.class, ToStringSerializer.instance);
        longAsStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        builder
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .defaultDateFormat(new SimpleDateFormat(RFC_3339))
            .addModules(javaTimeModule, longAsStringModule)
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

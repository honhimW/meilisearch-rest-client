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

import lombok.*;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-02-29
 */

public class GsonTests {

    private static final JsonHandler jsonHandler = new GsonJsonHandler();

    private static final LocalDateTime localDateTime = LocalDateTime.of(2024, 2, 29, 9, 0, 0, 1_000_001);
    private static final Instant instant = Timestamp.valueOf(localDateTime).toInstant();

    @Test
    @SneakyThrows
    void string() {
        Pojo pojo = new Pojo();
        String key = "string";
        String value = "foo";
        pojo.setString(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":%s}", key, '"' + value + '"'));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getString(), value);
    }

    @Test
    @SneakyThrows
    void integer() {
        Pojo pojo = new Pojo();
        String key = "integer";
        Integer value = 99;
        pojo.setInteger(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":%s}", key, value));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getInteger(), value);
    }

    @Test
    @SneakyThrows
    void _double() {
        Pojo pojo = new Pojo();
        String key = "_double";
        Double value = 9.9;
        pojo.set_double(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":%s}", key, value));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.get_double(), value);
    }

    @Test
    @SneakyThrows
    void date() {
        Pojo pojo = new Pojo();
        String key = "date";
        Date value = Date.from(instant);
        pojo.setDate(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":\"%s\"}", key, "2024-02-29T09:00:00.001Z"));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getDate(), value);
    }

    @Test
    @SneakyThrows
    void localDate() {
        Pojo pojo = new Pojo();
        String key = "localDate";
        LocalDate value = localDateTime.toLocalDate();
        pojo.setLocalDate(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":\"%s\"}", key, "2024-02-29"));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getLocalDate(), value);
    }

    @Test
    @SneakyThrows
    void localDateTime() {
        Pojo pojo = new Pojo();
        String key = "localDateTime";
        LocalDateTime value = localDateTime;
        pojo.setLocalDateTime(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":\"%s\"}", key, "2024-02-29T09:00:00.001000001Z"));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getLocalDateTime(), value);
    }

    @Test
    @SneakyThrows
    void instant() {
        Pojo pojo = new Pojo();
        Instant value = instant;
        pojo.setInstant(value);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getInstant(), value);
    }

    @Test
    @SneakyThrows
    void simpleEnum() {
        Pojo pojo = new Pojo();
        String key = "simple";
        Simple value = Simple.FOO_BAR;
        pojo.setSimple(value);
        String json = jsonHandler.toJson(pojo);
        assert json.equals(String.format("{\"%s\":\"%s\"}", key, value.value()));
        Pojo pojo1 = jsonHandler.fromJson(json, Pojo.class);
        assert Objects.equals(pojo, pojo1);
        Pojo2 transform = jsonHandler.transform(pojo, Pojo2.class);
        assert Objects.equals(transform.getSimple(), value);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pojo implements Serializable {
        private String string;
        private Integer integer;
        private Double _double;
        private Date date;
        private LocalDate localDate;
        private LocalDateTime localDateTime;
        private Instant instant;
        private Simple simple;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pojo2 implements Serializable {
        private String string;
        private Integer integer;
        private Double _double;
        private Date date;
        private LocalDate localDate;
        private LocalDateTime localDateTime;
        private Instant instant;
        private Simple simple;
    }

}

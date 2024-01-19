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

import java.util.HashMap;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-19
 */

public enum Simple implements EnumValue<Simple> {

    HELLO_WORLD("hello_world"),
    FOO_BAR("foo_bar"),
    ;

    private final String value;

    Simple(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static Simple from(String stringValue) {
        return VALUE_MAP.get(stringValue);
    }

    private static final Map<String, Simple> VALUE_MAP;

    static {
        Simple[] values = values();
        Map<String, Simple> simpleMap = new HashMap<>();
        for (Simple value : values) {
            simpleMap.put(value.value, value);
        }
        VALUE_MAP = simpleMap;
    }

}

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

package io.github.honhimw.ms.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-05
 */

public class EntryList {

    private List<Map.Entry<String, String>> list;

    private EntryList() {
        this.list = new ArrayList<>();
    }

    public static EntryList newInstance() {
        return new EntryList();
    }

    public EntryList add(String key, String value) {
        list.add(new AbstractMap.SimpleEntry<>(key, value));
        return this;
    }

    public List<Map.Entry<String, String>> getList() {
        return list;
    }

}

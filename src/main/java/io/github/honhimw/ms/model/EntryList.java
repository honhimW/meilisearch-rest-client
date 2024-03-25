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

    /**
     * Creates a new EntryList instance.
     *
     * @return a new EntryList instance
     */
    public static EntryList newInstance() {
        return new EntryList();
    }

    /**
     * Adds a key-value pair to the EntryList.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return the modified EntryList
     */
    public EntryList add(String key, String value) {
        list.add(new AbstractMap.SimpleEntry<>(key, value));
        return this;
    }

    /**
     * Returns the list of key-value pairs in the EntryList.
     * @return the list of key-value pairs
     */
    public List<Map.Entry<String, String>> getList() {
        return list;
    }

}

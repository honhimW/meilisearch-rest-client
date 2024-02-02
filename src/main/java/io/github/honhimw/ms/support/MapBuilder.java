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

package io.github.honhimw.ms.support;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author hon_him
 * @since 2024-01-23
 */

@SuppressWarnings("unused")
public class MapBuilder<K, V> {

    private final Map<K, V> _map;

    private MapBuilder() {
        this._map = new HashMap<>();
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<>();
    }

    public MapBuilder<K, V> put(K k, V v) {
        _map.put(k, v);
        return this;
    }

    public MapBuilder<K, V> putAll(Map<K, V> another) {
        _map.putAll(another);
        return this;
    }

    public MapBuilder<K, V> clear() {
        _map.clear();
        return this;
    }

    public Map<K, V> build() {
        return build(true);
    }

    public Map<K, V> build(boolean mut) {
        if (mut) {
            return _map;
        } else {
            return new ImmutableHashMap<>(_map);
        }
    }

    private static class ImmutableHashMap<K, V> extends HashMap<K, V> {

        public ImmutableHashMap(Map<? extends K, ? extends V> m) {
            super(m);
        }

        @Override
        public V put(K key, V value) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V putIfAbsent(K key, V value) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V replace(K key, V value) {
            throw new UnsupportedOperationException("immutable map");
        }

        @Override
        public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException("immutable map");
        }
    }

}

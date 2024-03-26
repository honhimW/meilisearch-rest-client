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

import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton {@link TypeRef}, reducing size of compiled classes and runtime memory usage.
 *
 * @author hon_him
 * @since 2024-03-06
 */

public class TypeRefs {

    private static final Map<Class<?>, TypeRef<?>> CACHE = new ConcurrentHashMap<>();

    /**
     * Get the {@link TypeRef} instance for the given class, creating it if necessary.
     * @param type the type
     * @return {@link TypeRef} for the given type
     * @param <T> the type
     */
    @SuppressWarnings("unchecked")
    public static <T> TypeRef<T> of(Class<T> type) {
        return (TypeRef<T>) CACHE.computeIfAbsent(type, aClass -> TypeRef.of(type));
    }

    /**
     * Void type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VoidRef extends TypeRef<Void> {
        /**
         * Instance.
         */
        public static final VoidRef INSTANCE = new VoidRef();
    }

    /**
     * String type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringRef extends TypeRef<String> {
        /**
         * Instance.
         */
        public static final StringRef INSTANCE = new StringRef();
    }

    /**
     * String List type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringListRef extends TypeRef<List<String>> {
        /**
         * Instance.
         */
        public static final StringListRef INSTANCE = new StringListRef();
    }

    /**
     * TaskInfo type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TaskInfoRef extends TypeRef<TaskInfo> {
        /**
         * Instance.
         */
        public static final TaskInfoRef INSTANCE = new TaskInfoRef();
    }

    /**
     * String-String Map type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringStringMapRef extends TypeRef<Map<String, String>> {
        /**
         * Instance.
         */
        public static final StringStringMapRef INSTANCE = new StringStringMapRef();
    }

    /**
     * String-Object Map type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringObjectMapRef extends TypeRef<Map<String, Object>> {
        /**
         * Instance.
         */
        public static final StringObjectMapRef INSTANCE = new StringObjectMapRef();
    }

    /**
     * RankingRule List type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RankingRuleListRef extends TypeRef<List<RankingRule>> {
        /**
         * Instance.
         */
        public static final RankingRuleListRef INSTANCE = new RankingRuleListRef();
    }

    /**
     * Paginated TaskInfo type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageTaskInfoRef extends TypeRef<Page<TaskInfo>> {
        /**
         * Instance.
         */
        public static final PageTaskInfoRef INSTANCE = new PageTaskInfoRef();
    }

    /**
     * Paginated String-Object Map type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageStringObjectMapRef extends TypeRef<Page<Map<String, Object>>> {
        /**
         * Instance.
         */
        public static final PageStringObjectMapRef INSTANCE = new PageStringObjectMapRef();
    }

    /**
     * String-String-Object Map-Map type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringStringObjectMapMapRef extends TypeRef<Map<String, Map<String, Object>>> {
        /**
         * Instance.
         */
        public static final StringStringObjectMapMapRef INSTANCE = new StringStringObjectMapMapRef();
    }

    /**
     * Paginated Key type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageKeyRef extends TypeRef<Page<Key>> {
        /**
         * Instance.
         */
        public static final PageKeyRef INSTANCE = new PageKeyRef();
    }

    /**
     * Paginated Index type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageIndexRef extends TypeRef<Page<Index>> {
        /**
         * Instance.
         */
        public static final PageIndexRef INSTANCE = new PageIndexRef();
    }

    /**
     * String-Object Map SearchResponse List type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringObjectMapSearchResponseListRef extends TypeRef<List<SearchResponse<Map<String, Object>>>> {
        /**
         * Instance.
         */
        public static final StringObjectMapSearchResponseListRef INSTANCE = new StringObjectMapSearchResponseListRef();
    }

    /**
     * String-Object Map SearchResponse type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringObjectMapSearchResponseRef extends TypeRef<SearchResponse<Map<String, Object>>> {
        /**
         * Instance.
         */
        public static final StringObjectMapSearchResponseRef INSTANCE = new StringObjectMapSearchResponseRef();
    }

    /**
     * String-StringList Map type reference.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringStringListMapRef extends TypeRef<Map<String, List<String>>> {
        /**
         * Instance.
         */
        public static final StringStringListMapRef INSTANCE = new StringStringListMapRef();
    }

}

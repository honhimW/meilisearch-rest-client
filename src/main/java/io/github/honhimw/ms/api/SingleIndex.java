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

package io.github.honhimw.ms.api;

import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.IndexStats;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * Single Index operator.
 *
 * @author hon_him
 * @since 2024-03-08
 */

public interface SingleIndex {

    /**
     * Get information about current index.
     *
     * @return current index
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}")
    Optional<Index> get();

    /**
     * Create an index.
     *
     * @param primaryKey Primary key of current index
     * @return create task
     */
    @Operation(method = "POST", tags = "/indexes")
    TaskInfo create(@Nullable String primaryKey);

    /**
     * Update current index. Specify a primaryKey if it doesn't already exists yet.
     *
     * @param primaryKey update primary key of current index
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}")
    TaskInfo update(String primaryKey);

    /**
     * Delete current index.
     *
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}")
    TaskInfo delete();

    /**
     * Documents are objects composed of fields that can store any type of data.
     * Each field contains an attribute and its associated value.
     * Documents are stored inside indexes.
     * Learn more about documents.
     *
     * @return {@link Documents} operator
     */
    @Operation(tags = "/indexes/{index_uid}/documents")
    Documents documents();

    /**
     * Applies the given operation to the documents.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R documents(Function<Documents, R> operation) {
        return operation.apply(documents());
    }

    /**
     * Get typed documents of current index.
     *
     * @param typeRef type reference
     * @param <T>     document type
     * @return {@link TypedDocuments} operator
     */
    <T> TypedDocuments<T> documents(TypeRef<T> typeRef);

    /**
     * Get typed documents of current index.
     *
     * @param type type
     * @param <T>  document type
     * @return {@link TypedDocuments} operator
     */
    default <T> TypedDocuments<T> documents(Class<T> type) {
        return documents(TypeRef.of(type));
    }

    /**
     * Applies the given operation to the typed documents.
     *
     * @param typeRef   type reference
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R documents(TypeRef<T> typeRef, Function<TypedDocuments<T>, R> operation) {
        return operation.apply(documents(typeRef));
    }

    /**
     * Applies the given operation to the typed documents.
     *
     * @param type      type
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R documents(Class<T> type, Function<TypedDocuments<T>, R> operation) {
        return operation.apply(documents(type));
    }

    /**
     * Meilisearch exposes 3 routes to perform document searches:
     * <ul>
     *     <li>A POST route: this is the preferred route when using API authentication, as it allows preflight request caching and better performances.</li>
     *     <li>A GET route: the usage of this route is discouraged, unless you have good reason to do otherwise (specific caching abilities for example). Other than the differences mentioned above, the two routes are strictly equivalent.</li>
     *     <li>A POST multi-search route allowing to perform multiple search queries in a single HTTP request. Meilisearch exposes 1 route to perform facet searches:</li>
     *     <li>A POST facet-search route allowing to perform a facet search query on a facet in a single HTTP request.</li>
     * </ul>
     *
     * @return {@link Search} operator
     */
    @Operation(tags = "/indexes/{index_uid}/search")
    Search search();

    /**
     * Applies the given operation to the search.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R search(Function<Search, R> operation) {
        return operation.apply(search());
    }

    /**
     * Get typed search of current index.
     *
     * @param typeRef type reference
     * @param <T>     document type
     * @return {@link TypedSearch} operator
     */
    <T> TypedSearch<T> search(TypeRef<T> typeRef);

    /**
     * Get typed search of current index.
     *
     * @param type type
     * @param <T>  document type
     * @return {@link TypedSearch} operator
     */
    default <T> TypedSearch<T> search(Class<T> type) {
        return search(TypeRef.of(type));
    }

    /**
     * Applies the given operation to the typed search.
     *
     * @param typeRef   type reference
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R search(TypeRef<T> typeRef, Function<TypedSearch<T>, R> operation) {
        return operation.apply(search(typeRef));
    }

    /**
     * Applies the given operation to the typed search.
     *
     * @param type      type
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R search(Class<T> type, Function<TypedSearch<T>, R> operation) {
        return operation.apply(search(type));
    }

    /**
     * Get typed search with details of current index.
     *
     * @param typeRef type reference
     * @param <T>     document type
     * @return {@link TypedDetailsSearch} operator
     */
    <T> TypedDetailsSearch<T> searchWithDetails(TypeRef<T> typeRef);

    /**
     * Get typed search with details of current index.
     *
     * @param type type
     * @param <T>  document type
     * @return {@link TypedDetailsSearch} operator
     */
    default <T> TypedDetailsSearch<T> searchWithDetails(Class<T> type) {
        return searchWithDetails(TypeRef.of(type));
    }

    /**
     * Applies the given operation to the typed search with details.
     *
     * @param typeRef   type reference
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R searchWithDetails(TypeRef<T> typeRef, Function<TypedDetailsSearch<T>, R> operation) {
        return operation.apply(searchWithDetails(typeRef));
    }

    /**
     * Applies the given operation to the typed search with details.
     *
     * @param type      type
     * @param operation operation
     * @param <T>       document type
     * @param <R>       return type
     * @return the operation result
     */
    default <T, R> R searchWithDetails(Class<T> type, Function<TypedDetailsSearch<T>, R> operation) {
        return operation.apply(searchWithDetails(type));
    }

    /**
     * Get settings of current index.
     *
     * @return {@link Settings} operator
     */
    @Operation(tags = "/indexes/{indexUid}/settings")
    Settings settings();

    /**
     * Applies the given operation to the settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R settings(Function<Settings, R> operation) {
        return operation.apply(settings());
    }

    /**
     * Get stats of current index.
     *
     * @return stats of current index.
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/stats")
    IndexStats stats();


}

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
import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An index is an entity that gathers a set of documents with its own settings. Learn more about indexes.
 *
 * @author hon_him
 * @since 2023-12-29
 */

public interface Indexes {

    /**
     * List all indexes
     *
     * @param offset Number of indexes to skip
     * @param limit  Number of indexes to return
     * @return paged indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    Page<Index> list(@Nullable Integer offset, @Nullable Integer limit);

    /**
     * List all indexes
     *
     * @param page page builder
     * @return paged indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    Page<Index> list(Consumer<PageRequest> page);

    /**
     * List all indexes
     *
     * @param page page request
     * @return paged indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    Page<Index> list(PageRequest page);

    /**
     * Get information about an index.
     *
     * @param uid uid of the requested index
     * @return index
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}")
    Optional<Index> get(String uid);

    /**
     * Create an index.
     *
     * @param uid        uid of the requested index
     * @param primaryKey Primary key of the requested index
     * @return create task
     */
    @Operation(method = "POST", tags = "/indexes")
    TaskInfo create(String uid, @Nullable String primaryKey);

    /**
     * Create an index without primary key.
     *
     * @param uid uid of the requested index
     * @return create task
     */
    @Operation(method = "POST", tags = "/indexes")
    TaskInfo create(String uid);

    /**
     * Update an index. Specify a primaryKey if it doesn't already exists yet.
     *
     * @param uid        uid of the requested index
     * @param primaryKey Primary key of the requested index
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}")
    TaskInfo update(String uid, String primaryKey);

    /**
     * Delete an index.
     *
     * @param uid uid of the requested index
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}")
    TaskInfo delete(String uid);

    /**
     * Deploy a new version of an index without any downtime for clients by swapping documents,
     * settings, and task history between two indexes.
     * Specifying several swap operations that will be processed in an atomic way is possible.
     *
     * @param uids Array of the two indexUids to be swapped
     * @return update task
     */
    @Operation(method = "POST", tags = "/swap-indexes")
    TaskInfo swap(List<Map.Entry<String, String>> uids);

    /**
     * Deploy a new version of an index without any downtime for clients by swapping documents,
     * settings, and task history between two indexes.
     * Specifying several swap operations that will be processed in an atomic way is possible.
     *
     * @param consumer entryList configurer
     * @return update task
     */
    @Operation(method = "POST", tags = "/swap-indexes")
    TaskInfo swap(Consumer<EntryList> consumer);

    /**
     * Get the operator for a single index.
     *
     * @param uid index uid
     * @return {@link SingleIndex} operator
     */
    SingleIndex single(String uid);

    /**
     * Documents are objects composed of fields that can store any type of data.
     * Each field contains an attribute and its associated value.
     * Documents are stored inside indexes.
     * Learn more about documents.
     *
     * @param uid uid of the requested index
     * @return {@link Documents} operator
     */
    @Operation(tags = "/indexes/{index_uid}/documents")
    Documents documents(String uid);

    /**
     * Apply a function to the documents of an index.
     *
     * @param uid       the identifier for the document
     * @param operation the function to apply to the documents
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <R> R documents(String uid, Function<Documents, R> operation);

    /**
     * Get the typed documents of an index.
     *
     * @param uid     uid of the requested index
     * @param typeRef the type of the documents
     * @param <T>     the type of the documents
     * @return {@link TypedDocuments} operator
     */
    <T> TypedDocuments<T> documents(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed documents of an index.
     *
     * @param uid  uid of the requested index
     * @param type the type of the documents
     * @param <T>  the type of the documents
     * @return {@link TypedDocuments} operator
     */
    <T> TypedDocuments<T> documents(String uid, Class<T> type);

    /**
     * Apply a function to the typed documents of an index.
     *
     * @param uid       uid of the requested index
     * @param typeRef   the type of the documents
     * @param operation the function to apply to the documents
     * @param <T>       the type of the documents
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R documents(String uid, TypeRef<T> typeRef, Function<TypedDocuments<T>, R> operation);

    /**
     * Apply a function to the typed documents of an index.
     *
     * @param uid       uid of the requested index
     * @param type      the type of the documents
     * @param operation the function to apply to the documents
     * @param <T>       the type of the documents
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R documents(String uid, Class<T> type, Function<TypedDocuments<T>, R> operation);

    /**
     * Meilisearch exposes 3 routes to perform document searches:
     * <ul>
     *     <li>A POST route: this is the preferred route when using API authentication, as it allows preflight request caching and better performances.</li>
     *     <li>A GET route: the usage of this route is discouraged, unless you have good reason to do otherwise (specific caching abilities for example). Other than the differences mentioned above, the two routes are strictly equivalent.</li>
     *     <li>A POST multi-search route allowing to perform multiple search queries in a single HTTP request. Meilisearch exposes 1 route to perform facet searches:</li>
     *     <li>A POST facet-search route allowing to perform a facet search query on a facet in a single HTTP request.</li>
     * </ul>
     *
     * @param uid uid of the requested index
     * @return {@link Search} operator
     */
    @Operation(tags = "/indexes/{index_uid}/search")
    Search search(String uid);

    /**
     * Apply a function to the search of an index.
     *
     * @param uid       uid of the requested index
     * @param operation the function to apply to the search
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <R> R search(String uid, Function<Search, R> operation);

    /**
     * Get the typed search of an index.
     *
     * @param uid     uid of the requested index
     * @param typeRef the type of the search
     * @param <T>     the type of the search
     * @return {@link TypedSearch} operator
     */
    <T> TypedSearch<T> search(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed search of an index.
     *
     * @param uid  uid of the requested index
     * @param type the type of the search
     * @param <T>  the type of the search
     * @return {@link TypedSearch} operator
     */
    <T> TypedSearch<T> search(String uid, Class<T> type);

    /**
     * Apply a function to the typed search of an index.
     *
     * @param uid       uid of the requested index
     * @param typeRef   the type of the search
     * @param operation the function to apply to the search
     * @param <T>       the type of the search
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R search(String uid, TypeRef<T> typeRef, Function<TypedSearch<T>, R> operation);

    /**
     * Apply a function to the typed search of an index.
     *
     * @param uid       uid of the requested index
     * @param type      the type of the search
     * @param operation the function to apply to the search
     * @param <T>       the type of the search
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R search(String uid, Class<T> type, Function<TypedSearch<T>, R> operation);

    /**
     * Get the typed search of an index with details.
     *
     * @param uid     uid of the requested index
     * @param typeRef the type of the search
     * @param <T>     the type of the search
     * @return {@link TypedDetailsSearch} operator
     */
    <T> TypedDetailsSearch<T> searchWithDetails(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed search of an index with details.
     *
     * @param uid  uid of the requested index
     * @param type the type of the search
     * @param <T>  the type of the search
     * @return {@link TypedDetailsSearch} operator
     */
    <T> TypedDetailsSearch<T> searchWithDetails(String uid, Class<T> type);

    /**
     * Apply a function to the typed search of an index with details.
     *
     * @param uid       uid of the requested index
     * @param typeRef   the type of the search
     * @param operation the function to apply to the search
     * @param <T>       the type of the search
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R searchWithDetails(String uid, TypeRef<T> typeRef, Function<TypedDetailsSearch<T>, R> operation);

    /**
     * Apply a function to the typed search of an index with details.
     *
     * @param uid       uid of the requested index
     * @param type      the type of the search
     * @param operation the function to apply to the search
     * @param <T>       the type of the search
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <T, R> R searchWithDetails(String uid, Class<T> type, Function<TypedDetailsSearch<T>, R> operation);

    /**
     * Get settings of an index.
     *
     * @param uid uid of the requested index
     * @return settings
     */
    @Operation(tags = "/indexes/{indexUid}/settings")
    Settings settings(String uid);

    /**
     * Apply a function to the settings of an index.
     *
     * @param uid       uid of the requested index
     * @param operation the function to apply to the settings
     * @param <R>       the type of the result
     * @return the result of the operation
     */
    <R> R settings(String uid, Function<Settings, R> operation);

    /**
     * Get stats of all indexes.
     *
     * @return stats of all indexes.
     */
    @Operation(method = "GET", tags = "/stats")
    Stats stats();

    /**
     * Get stats of an index.
     *
     * @param uid uid of the requested index
     * @return stats of an index.
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/stats")
    IndexStats stats(String uid);

}

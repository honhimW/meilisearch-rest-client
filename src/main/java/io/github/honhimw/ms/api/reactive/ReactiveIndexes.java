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

package io.github.honhimw.ms.api.reactive;

import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An index is an entity that gathers a set of documents with its own settings. Learn more about indexes.
 *
 * @author hon_him
 * @since 2023-12-29
 */

public interface ReactiveIndexes {

    /**
     * List all indexes
     *
     * @param offset Number of indexes to skip
     * @param limit  Number of indexes to return
     * @return page of indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    Mono<Page<Index>> list(@Nullable Integer offset, @Nullable Integer limit);

    /**
     * List all indexes
     * @param page page builder
     * @return page of indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    default Mono<Page<Index>> list(Consumer<PageRequest> page) {
        PageRequest pageRequest = new PageRequest();
        page.accept(pageRequest);
        return list(pageRequest);
    }

    /**
     * List all indexes
     * @param page page request
     * @return page of indexes
     */
    @Operation(method = "GET", tags = "/indexes")
    default Mono<Page<Index>> list(PageRequest page) {
        return list(page.toOffset(), page.toLimit());
    }

    /**
     * Get information about an index.
     *
     * @param uid uid of the requested index
     * @return {@link Index}
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}")
    Mono<Index> get(String uid);

    /**
     * Create an index.
     *
     * @param uid        uid of the requested index
     * @param primaryKey Primary key of the requested index
     * @return create task
     */
    @Operation(method = "POST", tags = "/indexes")
    Mono<TaskInfo> create(String uid, @Nullable String primaryKey);

    /**
     * Create an index without primary key.
     * @param uid uid of the requested index
     * @return create task
     */
    @Operation(method = "POST", tags = "/indexes")
    default Mono<TaskInfo> create(String uid) {
        return create(uid, null);
    }

    /**
     * Update an index. Specify a primaryKey if it doesn't already exists yet.
     *
     * @param uid uid of the requested index
     * @param primaryKey Primary key of the requested index
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}")
    Mono<TaskInfo> update(String uid, String primaryKey);

    /**
     * Delete an index.
     *
     * @param uid uid of the requested index
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}")
    Mono<TaskInfo> delete(String uid);

    /**
     * Deploy a new version of an index without any downtime for clients by swapping documents,
     * settings, and task history between two indexes.
     * Specifying several swap operations that will be processed in an atomic way is possible.
     *
     * @param uids Array of the two indexUids to be swapped
     * @return indexSwap
     */
    @Operation(method = "POST", tags = "/swap-indexes")
    Mono<TaskInfo> swap(List<Map.Entry<String, String>> uids);

    /**
     * Deploy a new version of an index without any downtime for clients by swapping documents,
     * settings, and task history between two indexes.
     * Specifying several swap operations that will be processed in an atomic way is possible.
     *
     * @param consumer entryList configurer builder
     * @see #swap(List)
     * @return swap task
     */
    @Operation(method = "POST", tags = "/swap-indexes")
    default Mono<TaskInfo> swap(Consumer<EntryList> consumer) {
        EntryList entryList = EntryList.newInstance();
        consumer.accept(entryList);
        return swap(entryList.getList());
    }

    /**
     * Get the operator for a single index.
     * @param uid index uid
     * @return {@link ReactiveSingleIndex} operator
     */
    ReactiveSingleIndex single(String uid);

    /**
     * Documents are objects composed of fields that can store any type of data.
     * Each field contains an attribute and its associated value.
     * Documents are stored inside indexes.
     * Learn more about documents.
     *
     * @param uid uid of the requested index
     * @return {@link ReactiveDocuments} operator
     */
    @Operation(tags = "/indexes/{index_uid}/documents")
    ReactiveDocuments documents(String uid);

    /**
     * Apply a function to the documents of an index.
     * @param uid index uid
     * @param operation operation
     * @return operation result
     * @param <R> return type
     */
    default <R> R documents(String uid, Function<ReactiveDocuments, R> operation) {
        return operation.apply(documents(uid));
    }

    /**
     * Get the typed documents of an index.
     * @param uid index uid
     * @param typeRef type reference
     * @return {@link ReactiveTypedDocuments} operator
     * @param <T> documents type
     */
    <T> ReactiveTypedDocuments<T> documents(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed documents of an index.
     * @param uid index uid
     * @param type type
     * @return typed documents
     * @param <T> documents type
     */
    default <T> ReactiveTypedDocuments<T> documents(String uid, Class<T> type) {
        return documents(uid, TypeRef.of(type));
    }

    /**
     * Apply a function to the typed documents of an index.
     * @param uid index uid
     * @param typeRef type reference
     * @param operation operation
     * @return operation result
     * @param <T> documents type
     * @param <R> return type
     */
    default <T, R> R documents(String uid, TypeRef<T> typeRef, Function<ReactiveTypedDocuments<T>, R> operation) {
        return operation.apply(documents(uid, typeRef));
    }

    /**
     * Apply a function to the typed documents of an index.
     * @param uid index uid
     * @param type type
     * @param operation operation
     * @return operation result
     * @param <T> documents type
     * @param <R> return type
     */
    default <T, R> R documents(String uid, Class<T> type, Function<ReactiveTypedDocuments<T>, R> operation) {
        return operation.apply(documents(uid, type));
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
     * @param uid uid of the requested index
     * @return {@link ReactiveSearch} operator
     */
    @Operation(tags = "/indexes/{index_uid}/search")
    ReactiveSearch search(String uid);

    /**
     * Apply a function to the search of an index.
     * @param uid index uid
     * @param operation operation
     * @return operation result
     * @param <R> return type
     */
    default <R> R search(String uid, Function<ReactiveSearch, R> operation) {
        return operation.apply(search(uid));
    }

    /**
     * Get the typed search of an index.
     * @param uid index uid
     * @param typeRef type reference
     * @return {@link ReactiveTypedSearch} operator
     * @param <T> search type
     */
    <T> ReactiveTypedSearch<T> search(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed search of an index.
     *
     * @param uid index uid
     * @param type type
     * @return typed search
     * @param <T> search type
     */
    default <T> ReactiveTypedSearch<T> search(String uid, Class<T> type) {
        return search(uid, TypeRef.of(type));
    }

    /**
     * Apply a function to the typed search of an index.
     * @param uid index uid
     * @param typeRef type reference
     * @param operation operation
     * @return operation result
     * @param <T> search type
     * @param <R> return type
     */
    default <T, R> R search(String uid, TypeRef<T> typeRef, Function<ReactiveTypedSearch<T>, R> operation) {
        return operation.apply(search(uid, typeRef));
    }

    /**
     * Apply a function to the typed search of an index.
     * @param uid index uid
     * @param type type
     * @param operation operation
     * @return operation result
     * @param <T> search type
     * @param <R> return type
     */
    default <T, R> R search(String uid, Class<T> type, Function<ReactiveTypedSearch<T>, R> operation) {
        return operation.apply(search(uid, type));
    }

    /**
     * Get the typed search of an index with details.
     * @param uid index uid
     * @param typeRef type reference
     * @return {@link ReactiveTypedDetailsSearch} operator
     * @param <T> search type
     */
    <T> ReactiveTypedDetailsSearch<T> searchWithDetails(String uid, TypeRef<T> typeRef);

    /**
     * Get the typed search of an index with details.
     * @param uid index uid
     * @param type type
     * @return typed search
     * @param <T> search type
     */
    default <T> ReactiveTypedDetailsSearch<T> searchWithDetails(String uid, Class<T> type) {
        return searchWithDetails(uid, TypeRef.of(type));
    }

    /**
     * Apply a function to the typed search of an index with details.
     * @param uid index uid
     * @param typeRef type reference
     * @param operation operation
     * @return operation result
     * @param <T> search type
     * @param <R> return type
     */
    default <T, R> R searchWithDetails(String uid, TypeRef<T> typeRef, Function<ReactiveTypedDetailsSearch<T>, R> operation) {
        return operation.apply(searchWithDetails(uid, typeRef));
    }

    /**
     * Apply a function to the typed search of an index with details.
     * @param uid index uid
     * @param type type
     * @param operation operation
     * @return operation result
     * @param <T> search type
     * @param <R> return type
     */
    default <T, R> R searchWithDetails(String uid, Class<T> type, Function<ReactiveTypedDetailsSearch<T>, R> operation) {
        return operation.apply(searchWithDetails(uid, type));
    }

    /**
     * Get the settings of an index.
     * @param uid index uid
     * @return {@link ReactiveSettings} operator
     */
    @Operation(tags = "/indexes/{indexUid}/settings")
    ReactiveSettings settings(String uid);

    /**
     * Apply a function to the settings of an index.
     * @param uid index uid
     * @param operation operation
     * @return operation result
     * @param <R> return type
     */
    default <R> R settings(String uid, Function<ReactiveSettings, R> operation) {
        return operation.apply(settings(uid));
    }

    /**
     * Get stats of all indexes.
     *
     * @return stats of all indexes.
     */
    @Operation(method = "GET", tags = "/stats")
    Mono<Stats> stats();

    /**
     * Get stats of an index.
     *
     * @param uid index uidReactiveIndexes
     * @return stats of an index.
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/stats")
    Mono<IndexStats> stats(String uid);


}

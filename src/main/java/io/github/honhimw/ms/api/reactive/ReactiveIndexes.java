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
     */
    @Operation(method = "GET", tags = "/indexes")
    Mono<Page<Index>> list(@Nullable Integer offset, @Nullable Integer limit);

    @Operation(method = "GET", tags = "/indexes")
    default Mono<Page<Index>> list(Consumer<PageRequest> page) {
        PageRequest pageRequest = new PageRequest();
        page.accept(pageRequest);
        return list(pageRequest.toOffset(), pageRequest.toLimit());
    }

    /**
     * Get information about an index.
     *
     * @param uid uid of the requested index
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}")
    Mono<Index> get(String uid);

    @Operation(method = "POST", tags = "/indexes")
    default Mono<TaskInfo> create(String uid) {
        return create(uid, null);
    }

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
     * Update an index. Specify a primaryKey if it doesn't already exists yet.
     *
     * @param uid uid of the requested index
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
     * @see #swap(List)
     * @param consumer entryList configurer
     */
    @Operation(method = "POST", tags = "/swap-indexes")
    default Mono<TaskInfo> swap(Consumer<EntryList> consumer) {
        EntryList entryList = new EntryList();
        consumer.accept(entryList);
        return swap(entryList.getList());
    }

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

    default <R> R documents(String uid, Function<ReactiveDocuments, R> operation) {
        return operation.apply(documents(uid));
    }

    /**
     * Meilisearch exposes 3 routes to perform document searches:
     * <ul>
     *     <li>A POST route: this is the preferred route when using API authentication, as it allows preflight request caching and better performances.</li>
     *     <li>A GET route: the usage of this route is discouraged, unless you have good reason to do otherwise (specific caching abilities for example). Other than the differences mentioned above, the two routes are strictly equivalent.</li>
     *     <li>A POST multi-search route allowing to perform multiple search queries in a single HTTP request. Meilisearch exposes 1 route to perform facet searches:</li>
     *     <li>A POST facet-search route allowing to perform a facet search query on a facet in a single HTTP request.</li>
     * </ul>
     * @param uid uid of the requested index
     * @return {@link ReactiveSearch} operator
     */
    @Operation(tags = "/indexes/{index_uid}/search")
    ReactiveSearch search(String uid);

    default <R> R search(String uid, Function<ReactiveSearch, R> operation) {
        return operation.apply(search(uid));
    }

    @Operation(tags = "/indexes/{indexUid}/settings")
    ReactiveSettings settings(String uid);

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
     * @return stats of an index.
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/stats")
    Mono<IndexStats> stats(String uid);



}

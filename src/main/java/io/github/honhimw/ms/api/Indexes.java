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

import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.Page;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;

import java.util.List;

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
     * @param limit  Number of indexes to return
     * @param offset Number of indexes to skip
     */
    @Operation(method = "GET", tags = "/indexes")
    Page<Index> list(Integer limit, Integer offset);

    /**
     * Get information about an index.
     *
     * @param uid uid of the requested index
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}")
    Index get(String uid);

    /**
     * Create an index.
     *
     * @param uid        uid of the requested index
     * @param primaryKey Primary key of the requested index
     * @return TODO
     */
    @Operation(method = "POST", tags = "/indexes")
    Object create(String uid, @Nullable String primaryKey);

    /**
     * Update an index. Specify a primaryKey if it doesn't already exists yet.
     *
     * @param uid uid of the requested index
     * @return TODO
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}")
    Object update(String uid);

    /**
     * Delete an index.
     *
     * @param uid uid of the requested index
     * @return TODO
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}")
    Object delete(String uid);

    /**
     * Deploy a new version of an index without any downtime for clients by swapping documents,
     * settings, and task history between two indexes.
     * Specifying several swap operations that will be processed in an atomic way is possible.
     *
     * @param uids Array of the two indexUids to be swapped
     * @return indexSwap
     */
    @Operation(method = "POST", tags = "/indexes/{index_uid}")
    Index swap(List<String> uids);

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
     * Meilisearch exposes 3 routes to perform document searches:
     * <ul>
     *     <li>A POST route: this is the preferred route when using API authentication, as it allows preflight request caching and better performances.</li>
     *     <li>A GET route: the usage of this route is discouraged, unless you have good reason to do otherwise (specific caching abilities for example). Other than the differences mentioned above, the two routes are strictly equivalent.</li>
     *     <li>A POST multi-search route allowing to perform multiple search queries in a single HTTP request. Meilisearch exposes 1 route to perform facet searches:</li>
     *     <li>A POST facet-search route allowing to perform a facet search query on a facet in a single HTTP request.</li>
     * </ul>
     * @param uid uid of the requested index
     * @return {@link Search} operator
     */
    @Operation(tags = "/indexes/{index_uid}/search")
    Search search(String uid);

    @Operation(tags = "/indexes/{indexUid}/settings")
    Settings settings(String uid);

}

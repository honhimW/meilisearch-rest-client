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
     * Update an index's primary key. You can freely update the primary key of an index as long as it contains no
     * documents. To change the primary key of an index that already contains documents, you must first delete all
     * documents in that index. You may then change the primary key and index your dataset again.
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
     * Swap the documents, settings, and task history of two or more indexes. You can only swap indexes in pairs.
     * However, a single request can swap as many index pairs as you wish.
     * <p>
     * Swapping indexes is an atomic transaction: either all indexes are successfully swapped, or none are.
     * <p>
     * Swapping indexA and indexB will also replace every mention of indexA by indexB and vice-versa in the task
     * history. enqueued tasks are left unmodified.
     *
     * @param uids 	Array of the two indexUids to be swapped
     * @return indexSwap
     */
    @Operation(method = "POST", tags = "/indexes/{index_uid}")
    Index swap(List<String> uids);

}

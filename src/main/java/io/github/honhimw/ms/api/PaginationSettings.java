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

import io.github.honhimw.ms.model.Pagination;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#pagination">Pagination</a>
 * <p>
 * To protect your database from malicious scraping, Meilisearch has a default limit of 1000 results per search. This setting allows you to configure the maximum number of results returned per search.
 * <p>
 * maxTotalHits takes priority over search parameters such as limit, offset, hitsPerPage, and page.
 * <p>
 * For example, if you set maxTotalHits to 100, you will not be able to access search results beyond 100 no matter the value configured for offset.
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/front_end/pagination">To learn more about paginating search results with Meilisearch, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @see Pagination
 * @since 2024-01-03
 */

public interface PaginationSettings {

    /**
     * Get the pagination settings of an index.
     *
     * @return current index pagination
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/pagination")
    Pagination get();

    /**
     * Partially update the pagination settings for an index.
     * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
     * <pre>
     * Setting maxTotalHits to a value higher than the default will negatively impact search performance. Setting maxTotalHits to values over 20000 may result in queries taking seconds to complete.
     * </pre>
     *
     * @param pagination new pagination settings
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/pagination")
    TaskInfo update(Pagination pagination);

    /**
     * Partially update the pagination settings for an index.
     * @param maxTotalHits new maxTotalHits in pagination
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/pagination")
    default TaskInfo update(int maxTotalHits) {
        Pagination pagination = new Pagination();
        pagination.setMaxTotalHits(maxTotalHits);
        return update(pagination);
    }

    /**
     * Reset an index's faceting settings to their default value.
     * Setting sortFacetValuesBy to null( --data-binary '{ "sortFacetValuesBy": null }'), will restore it to the default value ("*": "alpha").
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/pagination")
    TaskInfo reset();

}

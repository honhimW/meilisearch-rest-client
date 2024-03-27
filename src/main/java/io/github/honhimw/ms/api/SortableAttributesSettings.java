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

import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#sortable-attributes">Sortable attributes</a>
 * <p>
 * Attributes that can be used when sorting search results using the sort search parameter.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
 * <pre>
 * Updating sortable attributes will re-index all documents in the index, which can take some time. We recommend updating your index settings first and then adding documents as this reduces RAM consumption.
 * </pre>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/fine_tuning_results/sorting">To learn more about sortable attributes, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface SortableAttributesSettings {

    /**
     * Get the sortable attributes of an index.
     *
     * @return current index sortable attribute
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/sortable-attributes")
    List<String> get();

    /**
     * Update an index's sortable attributes list.
     *
     * @param sortableAttributes An array of strings.
     *                           Each string should be an attribute that exists in the selected index.
     *                           If an attribute contains an object,
     *                           you can use dot notation to set one or more of its keys as a value for this setting:
     *                           "sortableAttributes": ["author.surname"].
     *                           <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
     *                           <pre>If the field does not exist, no error will be thrown.</pre>
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/sortable-attributes")
    TaskInfo update(List<String> sortableAttributes);

    /**
     * Reset an index's sortable attributes list back to its default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/sortable-attributes")
    TaskInfo reset();

}

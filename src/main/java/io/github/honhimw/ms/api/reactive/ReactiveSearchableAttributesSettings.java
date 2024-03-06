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

import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#searchable-attributes"><h1>Searchable attributes</h1></a>
 * The values associated with attributes in the searchableAttributes list are searched for matching query words. The order of the list also determines the attribute ranking order.
 * <p>
 * By default, the searchableAttributes array is equal to all fields in your dataset. This behavior is represented by the value ["*"].
 * <h2 style="color:orange">WARNING</h2>
 * <pre>
 * Updating searchable attributes will re-index all documents in the index, which can take some time. We recommend updating your index settings first and then adding documents as this reduces RAM consumption.
 * </pre>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/configuration/displayed_searchable_attributes#searchable-fields">
 * To learn more about searchable attributes, refer to our dedicated guide.
 * </a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveSearchableAttributesSettings {

    /**
     * Get the searchable attributes of an index.
     *
     * @return current index searchable attributes
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/searchable-attributes")
    Mono<List<String>> get();

    /**
     * Update the searchable attributes of an index.
     * <h2 style="color:orange">WARNING</h2>
     * <pre>
     * Due to an implementation bug, manually updating searchableAttributes will change the displayed order of document fields in the JSON response. This behavior is inconsistent and will be fixed in a future release.
     * </pre>
     *
     * @param searchableAttributes An array of strings.
     *                             Each string should be an attribute that exists in the selected index.
     *                             The array should be given in order of importance: from the most important attribute to the least important attribute.
     *                             If an attribute contains an object,
     *                             you can use dot notation to set one or more of its keys as a value for this setting:
     *                             "searchableAttributes": ["release_date.year"].
     *                             <h2 style="color:orange">WARNING</h2>
     *                             <pre>
     *                                                         If the field does not exist, no error will be thrown.
     *                                                         </pre>
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/searchable-attributes")
    Mono<TaskInfo> update(List<String> searchableAttributes);


    /**
     * Reset the searchable attributes of the index to the default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/searchable-attributes")
    Mono<TaskInfo> reset();

}

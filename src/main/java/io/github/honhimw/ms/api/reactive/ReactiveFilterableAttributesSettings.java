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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#filterable-attributes">Filterable attributes</a>
 * <p>
 * Attributes in the filterableAttributes list can be used as filters or facets.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
 * <pre>
 * Updating filterable attributes will re-index all documents in the index, which can take some time. We recommend updating your index settings first and then adding documents as this reduces RAM consumption.
 * </pre>
 * By default, the displayedAttributes array is equal to all fields in your dataset. This behavior is represented by the value ["*"].
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/fine_tuning_results/filtering">To learn more about filterable attributes, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveFilterableAttributesSettings {

    /**
     * Get the filterable attributes for an index.
     *
     * @return current index displayed attributes
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/filterable-attributes")
    Mono<List<String>> get();

    /**
     * Update an index's filterable attributes list.
     *
     * @param filterableAttributes An array of strings containing the attributes that can be used as filters at query time.
     *                             If an attribute contains an object,
     *                             you can use dot notation to set one or more of its keys as a value for this setting:
     *                             "filterableAttributes": ["release_date.year"].
     *                             <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
     *                             <pre>If the field does not exist, no error will be thrown.</pre>
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/filterable-attributes")
    Mono<TaskInfo> update(List<String> filterableAttributes);


    /**
     * Reset an index's filterable attributes list back to its default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/filterable-attributes")
    Mono<TaskInfo> reset();

}

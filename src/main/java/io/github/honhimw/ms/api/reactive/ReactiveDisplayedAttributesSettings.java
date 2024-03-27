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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#displayed-attributes">Displayed attributes</a>
 * <p>
 * The attributes added to the displayedAttributes list appear in search results. displayedAttributes only affects the search endpoints.
 * It has no impact on the get documents with POST and get documents with GET endpoints.
 * <p>
 * By default, the displayedAttributes array is equal to all fields in your dataset. This behavior is represented by the value ["*"].
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/configuration/displayed_searchable_attributes#displayed-fields">To learn more about displayed attributes, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveDisplayedAttributesSettings {

    /**
     * Get the displayed attributes of an index.
     *
     * @return current index displayed attributes
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/displayed-attributes")
    Mono<List<String>> get();

    /**
     * Update the displayed attributes of an index.
     *
     * @param displayedAttributes new displayed attributes
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/displayed-attributes")
    Mono<TaskInfo> update(List<String> displayedAttributes);


    /**
     * Reset the displayed attributes of the index to the default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/displayed-attributes")
    Mono<TaskInfo> reset();

}

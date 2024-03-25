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

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#distinct-attribute">Distinct attribute</a>
 * The distinct attribute is a field whose value will always be unique in the returned documents.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
 * <pre>
 * Updating distinct attributes will re-index all documents in the index, which can take some time.
 * We recommend updating your index settings first and then adding documents as this reduces RAM consumption.
 * </pre>
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/configuration/distinct">To learn more about the distinct attribute, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveDistinctAttributeSettings {

    /**
     * Get the distinct attribute of an index.
     *
     * @return current index distinct attributes
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/distinct-attribute")
    Mono<String> get();

    /**
     * Update the displayed attributes of an index.
     *
     * @param distinctAttribute distinct attribute
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/distinct-attribute")
    Mono<TaskInfo> update(String distinctAttribute);

    /**
     * Reset the displayed attributes of the index to the default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/distinct-attribute")
    Mono<TaskInfo> reset();

}

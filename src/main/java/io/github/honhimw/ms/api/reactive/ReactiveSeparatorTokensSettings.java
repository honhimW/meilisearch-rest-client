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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#separator-tokens">Separator tokens</a>
 * Configure strings as custom separator tokens indicating where a word ends and begins.
 * <p>
 * Tokens in the separatorTokens list are added on top of Meilisearch's default list of separators. To remove separators from the default list, use the nonSeparatorTokens setting.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
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

public interface ReactiveSeparatorTokensSettings {

    /**
     * Get an index's list of custom separator tokens.
     *
     * @return current index separator tokens
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/separator-tokens")
    Mono<List<String>> get();

    /**
     * Update an index's list of custom separator tokens.
     *
     * @param separatorTokens An array of strings, with each string indicating a word separator.
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/separator-tokens")
    Mono<TaskInfo> update(List<String> separatorTokens);


    /**
     * Reset an index's list of custom separator tokens to its default value, [].
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/separator-tokens")
    Mono<TaskInfo> reset();

}

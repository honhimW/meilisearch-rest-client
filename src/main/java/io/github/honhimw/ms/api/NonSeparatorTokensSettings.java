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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#non-separator-tokens">Non-separator tokens</a>
 * Remove tokens from Meilisearch's default list of word separators.
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface NonSeparatorTokensSettings {

    /**
     * Get an index's list of non-separator tokens.
     *
     * @return current index non-separator tokens
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    List<String> get();

    /**
     * Update an index's list of non-separator tokens.
     *
     * @param nonSeparatorTokens An array of strings, with each string indicating a token present in list of word separators.
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    TaskInfo update(List<String> nonSeparatorTokens);


    /**
     * Reset an index's list of non-separator tokens to its default value, [].
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    TaskInfo reset();

}

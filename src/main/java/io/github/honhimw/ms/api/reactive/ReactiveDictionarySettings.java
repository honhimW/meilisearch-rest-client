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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#dictionary">Dictionary</a>
 * <p>
 * Allows users to instruct Meilisearch to consider groups of strings as a single term by adding a supplementary dictionary of user-defined terms.
 * <p>
 * This is particularly useful when working with datasets containing many domain-specific words, and in languages where words are not separated by whitespace such as Japanese.
 * <p>
 * Custom dictionaries are also useful in a few use-cases for space-separated languages, such as datasets with names such as "J. R. R. Tolkien" and "W. E. B. Du Bois".
 * <p style="font-weight:bold;font-size:large">TIP:</p>
 * User-defined dictionaries can be used together with synonyms. It can be useful to configure Meilisearch so different spellings of an author's initials return the same results:
 * <pre>
 * {
 *     "dictionary": ["W. E. B.", "W.E.B."],
 *     "synonyms": {
 *       "W. E. B.": ["W.E.B."],
 *       "W.E.B.": ["W. E. B."]
 *     }
 *     ...
 * }
 * </pre>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveDictionarySettings {

    /**
     * Get an index's user-defined dictionary.
     *
     * @return current index dictionary settings
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/dictionary")
    Mono<List<String>> get();

    /**
     * Update an index's user-defined dictionary.
     *
     * @param dictionary override dictionary settings
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/dictionary")
    Mono<TaskInfo> update(List<String> dictionary);

    /**
     * Reset an index's dictionary to its default value, [].
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/dictionary")
    Mono<TaskInfo> reset();

}

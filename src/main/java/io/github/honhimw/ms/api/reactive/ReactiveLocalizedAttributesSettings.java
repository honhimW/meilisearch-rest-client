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

import io.github.honhimw.ms.model.LocalizedAttribute;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#localized-attributes">Localized attributes</a>
 * <p>
 * You can now set up the language of your index in your settings and during the search.
 * This will prevent users from using alternative Meilisearch images we were separately created until now
 *
 * @author hon_him
 * @since 2024-08-01
 */

public interface ReactiveLocalizedAttributesSettings {

    /**
     * Get an index's user-defined dictionary.
     *
     * @return current index dictionary settings
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/localized-attributes")
    Mono<List<LocalizedAttribute>> get();

    /**
     * Update an index's user-defined dictionary.
     *
     * @param localizedAttributes override dictionary settings
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/localized-attributes")
    Mono<TaskInfo> update(List<LocalizedAttribute> localizedAttributes);

    /**
     * Reset an index's dictionary to its default value, [].
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/localized-attributes")
    Mono<TaskInfo> reset();

}

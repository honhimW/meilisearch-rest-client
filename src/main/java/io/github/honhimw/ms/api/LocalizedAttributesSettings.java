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

import io.github.honhimw.ms.model.LocalizedAttribute;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.api.annotation.Operation;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#localized-attributes">Localized attributes</a>
 * <p>
 * You can now set up the language of your index in your settings and during the search.
 * This will prevent users from using alternative Meilisearch images we were separately created until now
 *
 * @since v1.10
 * @author hon_him
 * @since 2024-08-01
 */

public interface LocalizedAttributesSettings {

    /**
     * Get an index's user-defined dictionary.
     *
     * @return current index dictionary settings
     */
    @Nullable
    @Operation(method = "GET", paths = "/indexes/{index_uid}/settings/localized-attributes")
    List<LocalizedAttribute> get();

    /**
     * Update an index's user-defined dictionary.
     *
     * @param localizedAttributes override dictionary settings
     * @return update task
     */
    @Operation(method = "PUT", paths = "/indexes/{index_uid}/settings/localized-attributes")
    TaskInfo update(List<LocalizedAttribute> localizedAttributes);

    /**
     * Reset an index's dictionary to its default value, [].
     *
     * @return reset task
     */
    @Operation(method = "DELETE", paths = "/indexes/{index_uid}/settings/localized-attributes")
    TaskInfo reset();

}

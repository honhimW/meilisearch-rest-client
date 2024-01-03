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
import io.github.honhimw.ms.model.TypoTolerance;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#typo-tolerance"><h1>Typo tolerance</h1></a>
 * Typo tolerance helps users find relevant results even when their search queries contain spelling mistakes or typos.
 * This setting allows you to configure the minimum word size for typos and disable typo tolerance for specific words or attributes.
 * <p>
 * <a style="color:red"
 * href="https://www.meilisearch.com/docs/learn/configuration/typo_tolerance">
 * To learn more about typo tolerance, refer to our dedicated guide.
 * </a>
 *
 * @author hon_him
 * @see TypoTolerance
 * @since 2024-01-03
 */

public interface TypoToleranceSettings {

    /**
     * Get the typo tolerance settings of an index.
     *
     * @return current index stop words
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/typo-tolerance")
    TypoTolerance get();

    /**
     * Partially update the typo tolerance settings for an index.
     *
     * @param typoTolerance An object that contains all synonyms and their associated words. Add the associated words in an array to set a synonym for a word.
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/typo-tolerance")
    TaskInfo update(TypoTolerance typoTolerance);

    /**
     * Reset an index's typo tolerance settings to their default value.
     *
     * @see TypoTolerance#defaultObject()
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/typo-tolerance")
    TaskInfo reset();

}

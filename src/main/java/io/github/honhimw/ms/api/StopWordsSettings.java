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
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#stop-words">Stop words</a>
 * Words added to the stopWords list are ignored in future search queries.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
 * <pre>
 * Updating stop words will re-index all documents in the index, which can take some time. We recommend updating your index settings first and then adding documents as this reduces RAM consumption.
 * </pre>
 * <p style="color:blue;font-weight:bold;font-size:large">TIP</p>
 * <pre>
 * Stop words are strongly related to the language used in your dataset. For example, most datasets containing English documents will have countless occurrences of the and of. Italian datasets, instead, will benefit from ignoring words like a, la, or il.
 *
 * This website maintained by a French developer offers lists of possible stop words in different languages. Note that, depending on your dataset and use case, you will need to tweak these lists for optimal results.
 * </pre>
 * <a style="color:red"
 * href="https://www.meilisearch.com/docs/learn/fine_tuning_results/sorting">
 * To learn more about sortable attributes, refer to our dedicated guide.
 * </a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface StopWordsSettings {

    /**
     * Get the stop words list of an index.
     *
     * @return current index stop words
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/stop-words")
    List<String> get();

    /**
     * Update the list of stop words of an index.
     *
     * @param stopWords An array of strings. Each string should be a single word.
     *                  If a list of stop words already exists, it will be overwritten (replaced).
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/stop-words")
    TaskInfo update(List<String> stopWords);

    /**
     * Reset the list of stop words of an index to its default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/stop-words")
    TaskInfo reset();

}

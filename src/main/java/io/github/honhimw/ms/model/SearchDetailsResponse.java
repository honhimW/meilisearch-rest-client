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

package io.github.honhimw.ms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h2>Exhaustive and estimated total number of search results</h2>
 * <p>
 * By default, Meilisearch only returns an estimate of the total number of search results in a query: estimatedTotalHits. This happens because Meilisearch prioritizes relevancy and performance over providing an exhaustive number of search results. When working with estimatedTotalHits, use offset and limit to navigate between search results.
 * <p>
 * If you require the total number of search results, use the hitsPerPage and page search parameters in your query. The response to this query replaces estimatedTotalHits with totalHits and includes an extra field with number of search results pages based on your hitsPerPage: totalPages. Using totalHits and totalPages may result in slightly reduced performance, but is recommended when creating UI elements such as numbered page selectors.
 * <p>
 * Neither estimatedTotalHits nor totalHits can exceed the limit configured in the maxTotalHits index setting.
 * <p>
 * You can <a href="https://www.meilisearch.com/docs/learn/front_end/pagination">read more about pagination in our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-02-22 v1.7.0.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDetailsResponse<T> extends SearchResponse<HitDetails<T>> {

}

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

import io.github.honhimw.ms.model.RankingRule;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#ranking-rules">Ranking rules</a>
 * Ranking rules are built-in rules that rank search results according to certain criteria. They are applied in the same order in which they appear in the rankingRules array.
 * <p>
 * <a style="color:red"
 * href="https://www.meilisearch.com/docs/learn/core_concepts/relevancy">
 * To learn more about ranking rules, refer to our dedicated guide.
 * </a>
 * <p style="color:green;font-weight:bold;font-size:large">Ranking rules array</p>
 * <p style="color:orange;font-weight:bold;font-size:large">Default order</p>
 * <pre>
 * [
 *   "words",
 *   "typo",
 *   "proximity",
 *   "attribute",
 *   "sort",
 *   "exactness"
 * ]
 * </pre>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveRankingRulesSettings {

    /**
     * Get the ranking rules of an index.
     *
     * @return current index ranking rules
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/ranking-rules")
    Mono<List<RankingRule>> get();

    /**
     * Update the ranking rules of an index.
     * <p>
     * An array that contains ranking rules in order of importance.
     * <p>
     * To create a custom ranking rule, give an attribute followed by a colon (:) and either asc for ascending order or desc for descending order.
     * <ul>
     *     <li>To apply an ascending sort (results sorted by increasing value): attribute_name:asc</li>
     *     <li>To apply a descending sort (results sorted by decreasing value): attribute_name:desc</li>
     * </ul>
     * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
     * <pre>
     * If some documents do not contain the attribute defined in a custom ranking rule, the application of the ranking rule is undefined and the search results might not be sorted as you expected.
     *
     * Make sure that any attribute used in a custom ranking rule is present in all of your documents. For example, if you set the custom ranking rule desc(year), make sure that all your documents contain the attribute year.
     * </pre>
     *
     * @param rankingRules ordered ranking rules
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/ranking-rules")
    Mono<TaskInfo> update(List<RankingRule> rankingRules);


    /**
     * Reset the ranking rules of an index to their default value.
     * <p style="color:blue;font-weight:bold;font-size:large">TIP</p>
     * <pre>
     * Resetting ranking rules is not the same as removing them. To remove a ranking rule, use the update ranking rules endpoint.
     * </pre>
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/ranking-rules")
    Mono<TaskInfo> reset();

}

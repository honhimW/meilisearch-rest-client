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

import io.github.honhimw.ms.json.EnumValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#ranking-rules-array">Ranking rules array</a>
 *
 * @author hon_him
 * @since 2024-01-02
 */

public enum RankingRule implements EnumValue<RankingRule> {
    /**
     * Sorts results by decreasing number of matched query terms
     */
    WORDS("words"),
    /**
     * Sorts results by increasing number of typos
     */
    TYPO("typo"),
    /**
     * Sorts results by increasing distance between matched query terms
     */
    PROXIMITY("proximity"),
    /**
     * Sorts results based on the attribute ranking order
     */
    ATTRIBUTE("attribute"),
    /**
     * Sorts results based on parameters decided at query time
     */
    SORT("sort"),
    /**
     * Sorts results based on the similarity of the matched words with the query words
     */
    EXACTNESS("exactness"),
    ;
    public final String name;

    RankingRule(String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    public static List<RankingRule> defaultOrder() {
        return Arrays.asList(WORDS, TYPO, PROXIMITY, ATTRIBUTE, SORT, EXACTNESS);
    }

}

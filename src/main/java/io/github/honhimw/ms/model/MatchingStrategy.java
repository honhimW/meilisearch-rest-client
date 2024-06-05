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

/**
 * <a style="font-weight:bold;font-size:large" href="https://www.meilisearch.com/docs/reference/api/search#matching-strategy">Matching strategy</a>
 * <p>
 * Strategy used to match query terms within documents.
 * <p>
 * Expected value: <em style="color:green">last</em> or <em style="color:green">all</em> or <em style="color:green">frequency</em>
 * <p>
 * <em style="color:green">last</em>: returns documents containing all the query terms first. If there are not enough results containing all query terms to meet the requested limit, Meilisearch will remove one query term at a time, starting from the end of the query.
 * <p>
 * <em style="color:green">all</em>:  only returns documents that contain all query terms. Meilisearch will not match any more documents even if there aren't enough to meet the requested limit.
 * <p>
 * <em style="color:green">frequency</em>:  This favors the least frequent query words when retrieving the documents.
 *
 * @author hon_him
 * @since 2024-06-05 v1.9.0.0
 */

public enum MatchingStrategy implements EnumValue<MatchingStrategy> {

    /**
     * returns documents containing all the query terms first. If there are not enough results containing all query terms to meet the requested limit, Meilisearch will remove one query term at a time, starting from the end of the query.
     */
    LAST("last"),
    /**
     * only returns documents that contain all query terms. Meilisearch will not match any more documents even if there aren't enough to meet the requested limit.
     */
    ALL("all"),
    /**
     * This favors the least frequent query words when retrieving the documents.
     */
    FREQUENCY("frequency"),
    ;

    /**
     * Enum value
     */
    public final String matchingStrategy;

    MatchingStrategy(String matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    @Override
    public String value() {
        return this.matchingStrategy;
    }

}

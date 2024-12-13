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

import io.github.honhimw.ms.api.annotation.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Federation search response
 *
 * @param <T> the type of the search result
 * @author hon_him
 * @since 2024-09-29 v1.11
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FederationSearchResponse<T> extends SearchResponse<T> {

    /**
     * Federation search facets by index
     */
    @Schema(description = "Federation search facets by index")
    private Map<String, FacetResult> facetsByIndex;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetResult implements Serializable {

        /**
         * Distribution of the given facets
         */
        @Schema(description = "Distribution of the given facets")
        private Map<String, Map<String, Integer>> distribution;

        /**
         * The numeric min and max values per facet
         */
        @Schema(description = "The numeric min and max values per facet")
        private Map<String, FacetStats> stats;

    }

}

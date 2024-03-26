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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FacetSearchResponse implements Serializable {

    /**
     * Facet hits
     */
    @Schema(description = "Facet hits")
    private List<FacetHit> facetHits;

    /**
     * The original facetQuery
     */
    @Schema(description = "The original facetQuery")
    private String facetQuery;

    /**
     * Processing time of the query
     */
    @Schema(description = "Processing time of the query")
    private Long processingTimeMs;

    /**
     * Facet hit
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetHit implements Serializable {

        /**
         * Facet value matching the facetQuery
         */
        @Schema(description = "Facet value matching the facetQuery")
        private String value;

        /**
         * Number of documents with a facet value matching value
         */
        @Schema(description = "Number of documents with a facet value matching value")
        private Integer count;

    }


}

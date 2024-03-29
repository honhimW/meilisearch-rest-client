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
import java.util.Map;

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
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse<T> implements Serializable {

    /**
     * Results of the query
     */
    @Schema(description = "Results of the query")
    private List<T> hits;
    
    /**
     * Number of documents skipped
     */
    @Schema(description = "Number of documents skipped")
    private Integer offset;

    /**
     * Number of documents to take
     */
    @Schema(description = "Number of documents to take")
    private Integer limit;

    /**
     * Estimated total number of matches
     */
    @Schema(description = "Estimated total number of matches")
    private Integer estimatedTotalHits;

    /**
     * Exhaustive total number of matches
     */
    @Schema(description = "Exhaustive total number of matches")
    private Integer totalHits;

    /**
     * Exhaustive total number of search result pages
     */
    @Schema(description = "Exhaustive total number of search result pages")
    private Integer totalPages;

    /**
     * Number of results on each page
     */
    @Schema(description = "Number of results on each page")
    private Integer hitsPerPage;

    /**
     * Current search results page
     */
    @Schema(description = "Current search results page")
    private Integer page;

    /**
     * Processing time of the query
     */
    @Schema(description = "Processing time of the query")
    private Long processingTimeMs;

    /**
     * Query originating the response
     */
    @Schema(description = "Query originating the response")
    private String query;

    /**
     * Distribution of the given facets
     */
    @Schema(description = "Distribution of the given facets")
    private Map<String, Map<String, Integer>> facetDistribution;

    /**
     * The numeric min and max values per facet
     */
    @Schema(description = "The numeric min and max values per facet")
    private Map<String, FacetStats> facetStats;

    /**
     * FacetStats
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetStats implements Serializable {

        /**
         * lowest numerical value
         */
        @Schema(description = "lowest numerical value")
        private Number min;

        /**
         * highest numerical value
         */
        @Schema(description = "highest numerical value")
        private Number max;
    }

}

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
public class SearchResponse<T> implements Serializable {

    @Schema(description = "Results of the query")
    private List<T> hits;
    
    @Schema(description = "Number of documents skipped")
    private Integer offset;
    
    @Schema(description = "Number of documents to take")
    private Integer limit;
    
    @Schema(description = "Estimated total number of matches")
    private Integer estimatedTotalHits;
    
    @Schema(description = "Exhaustive total number of matches")
    private Integer totalHits;
    
    @Schema(description = "Exhaustive total number of search result pages")
    private Integer totalPages;
    
    @Schema(description = "Number of results on each page")
    private Integer hitsPerPage;
    
    @Schema(description = "Current search results page")
    private Integer page;

    @Schema(description = "Distribution of the given facets")
    private Object facetDistribution;
    
    @Schema(description = "The numeric min and max values per facet")
    private Object facetStats;
    
    @Schema(description = "Processing time of the query")
    private Long processingTimeMs;
    
    @Schema(description = "Query originating the response")
    private String query;
    
}

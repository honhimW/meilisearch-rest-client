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

/**
 * Search with indexUid and federation options request
 *
 * @author hon_him
 * @since v1.10
 * @since 2024-07-30
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchWithIndexAndFederationOptionsRequest extends SearchWithIndexRequest {

    /**
     * uid of the requested index
     */
    @Schema(description = "uid of the requested index")
    private FederationOptions federationOptions;

    /**
     * Creates a new {@link SearchWithIndexAndFederationOptionsRequest} instance from a {@link SearchRequest} with the given {@code indexUid}.
     *
     * @param indexUid      index uid
     * @param searchRequest search request
     * @return {@link SearchWithIndexAndFederationOptionsRequest}
     */
    public static SearchWithIndexAndFederationOptionsRequest from(String indexUid, SearchRequest searchRequest) {
        SearchWithIndexAndFederationOptionsRequest searchWithIndexRequest = new SearchWithIndexAndFederationOptionsRequest();
        searchWithIndexRequest.setIndexUid(indexUid);
        searchWithIndexRequest.setFilter(searchRequest.getFilter());
        searchWithIndexRequest.setQ(searchRequest.getQ());
        searchWithIndexRequest.setOffset(searchRequest.getOffset());
        searchWithIndexRequest.setLimit(searchRequest.getLimit());
        searchWithIndexRequest.setHitsPerPage(searchRequest.getHitsPerPage());
        searchWithIndexRequest.setPage(searchRequest.getPage());
        searchWithIndexRequest.setFacets(searchRequest.getFacets());
        searchWithIndexRequest.setAttributesToRetrieve(searchRequest.getAttributesToRetrieve());
        searchWithIndexRequest.setAttributesToCrop(searchRequest.getAttributesToCrop());
        searchWithIndexRequest.setCropLength(searchRequest.getCropLength());
        searchWithIndexRequest.setCropMarker(searchRequest.getCropMarker());
        searchWithIndexRequest.setAttributesToHighlight(searchRequest.getAttributesToHighlight());
        searchWithIndexRequest.setHighlightPreTag(searchRequest.getHighlightPreTag());
        searchWithIndexRequest.setHighlightPostTag(searchRequest.getHighlightPostTag());
        searchWithIndexRequest.setShowMatchesPosition(searchRequest.getShowMatchesPosition());
        searchWithIndexRequest.setSort(searchRequest.getSort());
        searchWithIndexRequest.setMatchingStrategy(searchRequest.getMatchingStrategy());
        searchWithIndexRequest.setShowRankingScore(searchRequest.getShowRankingScore());
        searchWithIndexRequest.setShowRankingScoreDetails(searchRequest.getShowRankingScoreDetails());
        searchWithIndexRequest.setAttributesToSearchOn(searchRequest.getAttributesToSearchOn());
        searchWithIndexRequest.setHybrid(searchRequest.getHybrid());
        searchWithIndexRequest.setVector(searchRequest.getVector());
        return searchWithIndexRequest;
    }

    /**
     * set the {@link FederationOptions}
     *
     * @param federationOptions federation options
     * @return this
     */
    public SearchWithIndexAndFederationOptionsRequest federationOptions(FederationOptions federationOptions) {
        this.federationOptions = federationOptions;
        return this;
    }

    /**
     * set the {@link FederationOptions}
     *
     * @param federationWeight federation weight
     * @return this
     */
    public SearchWithIndexAndFederationOptionsRequest federationOptions(Number federationWeight) {
        this.federationOptions = new FederationOptions(federationWeight);
        return this;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FederationOptions implements Serializable {

        @Schema(description = "must be positive (>=0)")
        private Number weight;

    }

}

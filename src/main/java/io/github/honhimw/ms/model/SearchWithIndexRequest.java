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

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchWithIndexRequest extends SearchRequest {

    /**
     * uid of the requested index
     */
    @Schema(description = "uid of the requested index")
    private String indexUid;

    /**
     * Creates a new {@link SearchWithIndexRequest} instance from a {@link SearchRequest} with the given {@code indexUid}.
     *
     * @param indexUid      index uid
     * @param searchRequest search request
     * @return {@link SearchWithIndexRequest}
     */
    public static SearchWithIndexRequest from(String indexUid, SearchRequest searchRequest) {
        SearchWithIndexRequest searchWithIndexRequest = new SearchWithIndexRequest();
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

}

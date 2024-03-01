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

import io.github.honhimw.ms.Experimental;
import io.github.honhimw.ms.support.FilterBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest extends FilterableAttributesRequest {

    @Schema(description = "Query string")
    private String q;

    @Schema(description = "Number of documents to skip", defaultValue = "0")
    private Integer offset;

    @Schema(description = "Maximum number of documents returned", defaultValue = "20")
    private Integer limit;

    @Schema(description = "Maximum number of documents returned for a page", defaultValue = "1")
    private Integer hitsPerPage;

    @Schema(description = "Request a specific page of results", defaultValue = "1")
    private Integer page;

    @Schema(description = "Display the count of matches per facet")
    private List<String> facets;

    @Schema(description = "Attributes to display in the returned documents", defaultValue = "[\"*\"]")
    private List<String> attributesToRetrieve;

    @Schema(description = "Attributes whose values have to be cropped")
    private List<String> attributesToCrop;

    @Schema(description = "Maximum length of cropped value in words", defaultValue = "10")
    private Integer cropLength;

    @Schema(description = "String marking crop boundaries", defaultValue = "\"...\"")
    private String cropMarker;

    @Schema(description = "Highlight matching terms contained in an attribute")
    private List<String> attributesToHighlight;

    @Schema(description = "String inserted at the start of a highlighted term", defaultValue = "\"<em>\"")
    private String highlightPreTag;

    @Schema(description = "String inserted at the end of a highlighted term", defaultValue = "\"</em>\"")
    private String highlightPostTag;

    @Schema(description = "Return matching terms location", defaultValue = "false")
    private Boolean showMatchesPosition;

    @Schema(description = "Sort search results by an attribute's value")
    private List<String> sort;

    @Schema(description = "Strategy used to match query terms within documents")
    private String matchingStrategy;

    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScore;

    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScoreDetails;

    @Schema(description = "Restrict search to the specified attributes", defaultValue = "[\"*\"]")
    private List<String> attributesToSearchOn;

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Schema(description = "hybrid is an object and accepts two fields: semanticRatio and embedder")
    private Hybrid hybrid;

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Schema(description = "must be an array of numbers indicating the search vector. You must generate these yourself when using vector search with user-provided embeddings.")
    private List<Number> vector;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String filter;
        private String q;
        private Integer offset;
        private Integer limit;
        private Integer hitsPerPage;
        private Integer page;
        private List<String> facets;
        private List<String> attributesToRetrieve;
        private List<String> attributesToCrop;
        private Integer cropLength;
        private String cropMarker;
        private List<String> attributesToHighlight;
        private String highlightPreTag;
        private String highlightPostTag;
        private Boolean showMatchesPosition;
        private List<String> sort;
        private String matchingStrategy;
        private Boolean showRankingScore;
        private Boolean showRankingScoreDetails;
        private List<String> attributesToSearchOn;
        private Hybrid hybrid;
        private List<Number> vector;

        private Builder() {
        }

        public Builder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder filter(Consumer<FilterBuilder> consumer) {
            FilterBuilder filterBuilder = FilterBuilder.builder();
            consumer.accept(filterBuilder);
            this.filter = filterBuilder.build();
            return this;
        }

        public Builder q(String q) {
            this.q = q;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder hitsPerPage(Integer hitsPerPage) {
            this.hitsPerPage = hitsPerPage;
            return this;
        }

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder facets(List<String> facets) {
            this.facets = facets;
            return this;
        }

        public Builder attributesToRetrieve(List<String> attributesToRetrieve) {
            this.attributesToRetrieve = attributesToRetrieve;
            return this;
        }

        public Builder attributesToCrop(List<String> attributesToCrop) {
            this.attributesToCrop = attributesToCrop;
            return this;
        }

        public Builder cropLength(Integer cropLength) {
            this.cropLength = cropLength;
            return this;
        }

        public Builder cropMarker(String cropMarker) {
            this.cropMarker = cropMarker;
            return this;
        }

        public Builder attributesToHighlight(List<String> attributesToHighlight) {
            this.attributesToHighlight = attributesToHighlight;
            return this;
        }

        public Builder highlightPreTag(String highlightPreTag) {
            this.highlightPreTag = highlightPreTag;
            return this;
        }

        public Builder highlightPostTag(String highlightPostTag) {
            this.highlightPostTag = highlightPostTag;
            return this;
        }

        public Builder showMatchesPosition(Boolean showMatchesPosition) {
            this.showMatchesPosition = showMatchesPosition;
            return this;
        }

        public Builder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }

        public Builder matchingStrategy(String matchingStrategy) {
            this.matchingStrategy = matchingStrategy;
            return this;
        }

        public Builder showRankingScore(Boolean showRankingScore) {
            this.showRankingScore = showRankingScore;
            return this;
        }

        public Builder showRankingScoreDetails(Boolean showRankingScoreDetails) {
            this.showRankingScoreDetails = showRankingScoreDetails;
            return this;
        }

        public Builder attributesToSearchOn(List<String> attributesToSearchOn) {
            this.attributesToSearchOn = attributesToSearchOn;
            return this;
        }

        public Builder hybrid(Hybrid hybrid) {
            this.hybrid = hybrid;
            return this;
        }

        public Builder vector(List<Number> vector) {
            this.vector = vector;
            return this;
        }

        public SearchRequest build() {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setFilter(filter);
            searchRequest.setQ(q);
            searchRequest.setOffset(offset);
            searchRequest.setLimit(limit);
            searchRequest.setHitsPerPage(hitsPerPage);
            searchRequest.setPage(page);
            searchRequest.setFacets(facets);
            searchRequest.setAttributesToRetrieve(attributesToRetrieve);
            searchRequest.setAttributesToCrop(attributesToCrop);
            searchRequest.setCropLength(cropLength);
            searchRequest.setCropMarker(cropMarker);
            searchRequest.setAttributesToHighlight(attributesToHighlight);
            searchRequest.setHighlightPreTag(highlightPreTag);
            searchRequest.setHighlightPostTag(highlightPostTag);
            searchRequest.setShowMatchesPosition(showMatchesPosition);
            searchRequest.setSort(sort);
            searchRequest.setMatchingStrategy(matchingStrategy);
            searchRequest.setShowRankingScore(showRankingScore);
            searchRequest.setShowRankingScoreDetails(showRankingScoreDetails);
            searchRequest.setAttributesToSearchOn(attributesToSearchOn);
            searchRequest.setHybrid(hybrid);
            searchRequest.setVector(vector);
            return searchRequest;
        }
    }
}

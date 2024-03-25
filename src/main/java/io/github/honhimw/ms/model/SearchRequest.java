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

    private SearchRequest(Builder builder) {
        setFilter(builder.filter);
        setQ(builder.q);
        setOffset(builder.offset);
        setLimit(builder.limit);
        setHitsPerPage(builder.hitsPerPage);
        setPage(builder.page);
        setFacets(builder.facets);
        setAttributesToRetrieve(builder.attributesToRetrieve);
        setAttributesToCrop(builder.attributesToCrop);
        setCropLength(builder.cropLength);
        setCropMarker(builder.cropMarker);
        setAttributesToHighlight(builder.attributesToHighlight);
        setHighlightPreTag(builder.highlightPreTag);
        setHighlightPostTag(builder.highlightPostTag);
        setShowMatchesPosition(builder.showMatchesPosition);
        setSort(builder.sort);
        setMatchingStrategy(builder.matchingStrategy);
        setShowRankingScore(builder.showRankingScore);
        setShowRankingScoreDetails(builder.showRankingScoreDetails);
        setAttributesToSearchOn(builder.attributesToSearchOn);
        setHybrid(builder.hybrid);
        setVector(builder.vector);
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return  a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code SearchRequest} builder static inner class.
     */
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

        /**
         * Sets the {@code filter} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code filter} to set
         * @return a reference to this Builder
         */
        public Builder filter(String val) {
            filter = val;
            return this;
        }

        /**
         * Sets the {@code q} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code q} to set
         * @return a reference to this Builder
         */
        public Builder q(String val) {
            q = val;
            return this;
        }

        /**
         * Sets the {@code offset} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code offset} to set
         * @return a reference to this Builder
         */
        public Builder offset(Integer val) {
            offset = val;
            return this;
        }

        /**
         * Sets the {@code limit} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code limit} to set
         * @return a reference to this Builder
         */
        public Builder limit(Integer val) {
            limit = val;
            return this;
        }

        /**
         * Sets the {@code hitsPerPage} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code hitsPerPage} to set
         * @return a reference to this Builder
         */
        public Builder hitsPerPage(Integer val) {
            hitsPerPage = val;
            return this;
        }

        /**
         * Sets the {@code page} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code page} to set
         * @return a reference to this Builder
         */
        public Builder page(Integer val) {
            page = val;
            return this;
        }

        /**
         * Sets the {@code facets} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code facets} to set
         * @return a reference to this Builder
         */
        public Builder facets(List<String> val) {
            facets = val;
            return this;
        }

        /**
         * Sets the {@code attributesToRetrieve} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code attributesToRetrieve} to set
         * @return a reference to this Builder
         */
        public Builder attributesToRetrieve(List<String> val) {
            attributesToRetrieve = val;
            return this;
        }

        /**
         * Sets the {@code attributesToCrop} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code attributesToCrop} to set
         * @return a reference to this Builder
         */
        public Builder attributesToCrop(List<String> val) {
            attributesToCrop = val;
            return this;
        }

        /**
         * Sets the {@code cropLength} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code cropLength} to set
         * @return a reference to this Builder
         */
        public Builder cropLength(Integer val) {
            cropLength = val;
            return this;
        }

        /**
         * Sets the {@code cropMarker} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code cropMarker} to set
         * @return a reference to this Builder
         */
        public Builder cropMarker(String val) {
            cropMarker = val;
            return this;
        }

        /**
         * Sets the {@code attributesToHighlight} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code attributesToHighlight} to set
         * @return a reference to this Builder
         */
        public Builder attributesToHighlight(List<String> val) {
            attributesToHighlight = val;
            return this;
        }

        /**
         * Sets the {@code highlightPreTag} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code highlightPreTag} to set
         * @return a reference to this Builder
         */
        public Builder highlightPreTag(String val) {
            highlightPreTag = val;
            return this;
        }

        /**
         * Sets the {@code highlightPostTag} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code highlightPostTag} to set
         * @return a reference to this Builder
         */
        public Builder highlightPostTag(String val) {
            highlightPostTag = val;
            return this;
        }

        /**
         * Sets the {@code showMatchesPosition} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code showMatchesPosition} to set
         * @return a reference to this Builder
         */
        public Builder showMatchesPosition(Boolean val) {
            showMatchesPosition = val;
            return this;
        }

        /**
         * Sets the {@code sort} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code sort} to set
         * @return a reference to this Builder
         */
        public Builder sort(List<String> val) {
            sort = val;
            return this;
        }

        /**
         * Sets the {@code matchingStrategy} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code matchingStrategy} to set
         * @return a reference to this Builder
         */
        public Builder matchingStrategy(String val) {
            matchingStrategy = val;
            return this;
        }

        /**
         * Sets the {@code showRankingScore} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code showRankingScore} to set
         * @return a reference to this Builder
         */
        public Builder showRankingScore(Boolean val) {
            showRankingScore = val;
            return this;
        }

        /**
         * Sets the {@code showRankingScoreDetails} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code showRankingScoreDetails} to set
         * @return a reference to this Builder
         */
        public Builder showRankingScoreDetails(Boolean val) {
            showRankingScoreDetails = val;
            return this;
        }

        /**
         * Sets the {@code attributesToSearchOn} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code attributesToSearchOn} to set
         * @return a reference to this Builder
         */
        public Builder attributesToSearchOn(List<String> val) {
            attributesToSearchOn = val;
            return this;
        }

        /**
         * Sets the {@code hybrid} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code hybrid} to set
         * @return a reference to this Builder
         */
        public Builder hybrid(Hybrid val) {
            hybrid = val;
            return this;
        }

        /**
         * Sets the {@code vector} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code vector} to set
         * @return a reference to this Builder
         */
        public Builder vector(List<Number> val) {
            vector = val;
            return this;
        }

        /**
         * Returns a {@code SearchRequest} built from the parameters previously set.
         *
         * @return a {@code SearchRequest} built with parameters of this {@code SearchRequest.Builder}
         */
        public SearchRequest build() {
            return new SearchRequest(this);
        }
    }
}

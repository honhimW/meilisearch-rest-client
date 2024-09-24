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
import io.github.honhimw.ms.support.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * Search request
 *
 * @author hon_him
 * @since 2024-08-19 v1.10.0.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttributeSearchRequest extends FilterableAttributesRequest {

    /**
     * Queries with specific attributes
     */
    @Schema(description = "Queries with specific attributes")
    private List<AttributeSearch> queries;

    /**
     * Attributes to display in the returned documents", defaultValue = "[\"*\"]
     */
    @Schema(description = "Attributes to display in the returned documents", defaultValue = "[\"*\"]")
    private List<String> attributesToRetrieve;

    /**
     * Attributes whose values have to be cropped
     */
    @Schema(description = "Attributes whose values have to be cropped")
    private List<String> attributesToCrop;

    /**
     * Maximum length of cropped value in words", defaultValue = "10
     */
    @Schema(description = "Maximum length of cropped value in words", defaultValue = "10")
    private Integer cropLength;

    /**
     * String marking crop boundaries", defaultValue = "\"...\"
     */
    @Schema(description = "String marking crop boundaries", defaultValue = "\"...\"")
    private String cropMarker;

    /**
     * Highlight matching terms contained in an attribute
     */
    @Schema(description = "Highlight matching terms contained in an attribute")
    private List<String> attributesToHighlight;

    /**
     * String inserted at the start of a highlighted term"
     */
    @Schema(description = "String inserted at the start of a highlighted term", defaultValue = "\"<em>\"")
    private String highlightPreTag;

    /**
     * String inserted at the end of a highlighted term"
     */
    @Schema(description = "String inserted at the end of a highlighted term", defaultValue = "\"</em>\"")
    private String highlightPostTag;

    /**
     * Return matching terms location", defaultValue = "false
     */
    @Schema(description = "Return matching terms location", defaultValue = "false")
    private Boolean showMatchesPosition;

    /**
     * Sort search results by an attribute's value
     */
    @Schema(description = "Sort search results by an attribute's value", defaultValue = "null")
    private List<String> sort;

    /**
     * Strategy used to match query terms within documents.
     * <p>
     * Expected value: <em style="color:green">last</em> or <em style="color:green">all</em>
     * <p>
     * <em style="color:green">last</em>: returns documents containing all the query terms first. If there are not enough results containing all query terms to meet the requested limit, Meilisearch will remove one query term at a time, starting from the end of the query.
     * <p>
     * <em style="color:green">all</em>:  only returns documents that contain all query terms. Meilisearch will not match any more documents even if there aren't enough to meet the requested limit.
     */
    @Schema(description = "Strategy used to match query terms within documents", defaultValue = "last")
    private MatchingStrategy matchingStrategy;

    /**
     * Display the global ranking score of a document", defaultValue = "false
     */
    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScore;

    /**
     * Display the global ranking score of a document", defaultValue = "false
     */
    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScoreDetails;

    /**
     * @since v1.9.0
     */
    @Schema(description = "If a distinct attribute is already defined in the settings it'll be ignored in favor of the one defined at search time.")
    private String distinct;

    /**
     * Meilisearch does not return any documents below the configured threshold. Excluded results do not count towards estimatedTotalHits, totalHits, and facet distribution.
     * <p>
     * For performance reasons, if the number of documents above rankingScoreThreshold is higher than limit, Meilisearch does not evaluate the ranking score of the remaining documents. Results ranking below the threshold are not immediately removed from the set of candidates. In this case, Meilisearch may overestimate the count of estimatedTotalHits, totalHits and facet distribution.
     *
     * @since v1.9.0
     */
    @Schema(description = "Exclude search results with low ranking scores")
    private Number rankingScoreThreshold;

    /**
     * If federation is empty ({}) default values of offset and limit are used, so respectively 0 and 20.
     * If federation is null or missing, a classic multi-search will be applied, so a list of search result objects for each index will be returned.
     */
    @Schema(description = "return a single search result object, whose list of hits is built by merging the hits coming from all the queries in descending ranking score order", defaultValue = "null")
    private MultiSearchWithFederationRequest.Federation federation;

    /**
     * allowing the end-user to define the language used in the current query.
     * <p>
     * The locales parameter overrides eventual locales in the index settings.
     *
     * @since v1.10
     */
    @Schema(description = "allowing the end-user to define the language used in the current query")
    private List<String> locales;

    /**
     * Build MultiSearchWithFederationRequest
     * @param indexUid index
     * @return MultiSearchWithFederationRequest
     */
    public MultiSearchWithFederationRequest toMultiSearch(String indexUid) {
        List<AttributeSearchRequest.AttributeSearch> queries = this.getQueries();
        MultiSearchWithFederationRequest.Builder builder = MultiSearchWithFederationRequest.builder();
        queries.forEach(query -> {
            String q = query.getQ();
            List<String> attributesToSearchOn = query.getAttributesToSearchOn();
            builder.addQuery(indexUid, SearchRequest.builder()
                .q(q)
                .attributesToSearchOn(attributesToSearchOn)
                .attributesToRetrieve(this.getAttributesToRetrieve())
                .attributesToCrop(this.getAttributesToCrop())
                .cropLength(this.getCropLength())
                .cropMarker(this.getCropMarker())
                .attributesToHighlight(this.getAttributesToHighlight())
                .highlightPreTag(this.getHighlightPreTag())
                .highlightPostTag(this.getHighlightPostTag())
                .sort(this.getSort())
                .matchingStrategy(this.getMatchingStrategy())
                .distinct(this.getDistinct())
                .build()
            );
        });
        MultiSearchWithFederationRequest.Federation federation = Optional.ofNullable(this.getFederation()).orElseGet(() -> new MultiSearchWithFederationRequest.Federation(0, 20));
        builder.federation(federation);
        return builder.build();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeSearch implements Serializable {
        /**
         * Query string
         */
        @Schema(description = "Query string")
        private String q;

        /**
         * Restrict search to the specified attributes", defaultValue = "[\"*\"]
         */
        @Schema(description = "Restrict search to the specified attributes", defaultValue = "[\"*\"]")
        private List<String> attributesToSearchOn;
    }

    private AttributeSearchRequest(Builder builder) {
        setQueries(builder.queries);
        setFilter(builder.filter);
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
        setDistinct(builder.distinct);
        setRankingScoreThreshold(builder.rankingScoreThreshold);
        setFederation(builder.federation);
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }


    /**
     * {@code SearchRequest} builder static inner class.
     */
    public static final class Builder {
        private String filter;
        private List<AttributeSearch> queries;
        private List<String> attributesToRetrieve;
        private List<String> attributesToCrop;
        private Integer cropLength;
        private String cropMarker;
        private List<String> attributesToHighlight;
        private String highlightPreTag;
        private String highlightPostTag;
        private Boolean showMatchesPosition;
        private List<String> sort;
        private MatchingStrategy matchingStrategy;
        private Boolean showRankingScore;
        private Boolean showRankingScoreDetails;
        private String distinct;
        private Number rankingScoreThreshold;
        private MultiSearchWithFederationRequest.Federation federation;

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
        public Builder matchingStrategy(MatchingStrategy val) {
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
         * Search on specific attributes, at least one attribute is required
         * @param q          content to search
         * @param attribute  attribute to search
         * @param attributes attributes to search
         * @return a reference to this Builder
         */
        public Builder queryOn(String q, String attribute, String... attributes) {
            if (Objects.isNull(this.queries)) {
                this.queries = new ArrayList<>();
            }

            AttributeSearch attributeSearch = new AttributeSearch();
            attributeSearch.setQ(q);
            Set<String> attributesToSearchOn = new HashSet<>();
            attributesToSearchOn.add(attribute);
            if (CollectionUtils.isNotEmpty(attributes)) {
                Collections.addAll(attributesToSearchOn, attributes);
            }
            attributeSearch.setAttributesToSearchOn(new ArrayList<>(attributesToSearchOn));
            queries.add(attributeSearch);
            return this;
        }

        /**
         * Sets the {@code distinct} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code distinct} to set
         * @return a reference to this Builder
         */
        public Builder distinct(String val) {
            distinct = val;
            return this;
        }

        /**
         * Sets the {@code rankingScoreThreshold} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code rankingScoreThreshold} to set
         * @return a reference to this Builder
         */
        public Builder rankingScoreThreshold(Number val) {
            rankingScoreThreshold = val;
            return this;
        }

        public Builder federation(MultiSearchWithFederationRequest.Federation federation) {
            this.federation = federation;
            return this;
        }

        /**
         * Returns a {@code SearchRequest} built from the parameters previously set.
         *
         * @return a {@code SearchRequest} built with parameters of this {@code SearchRequest.Builder}
         */
        public AttributeSearchRequest build() {
            Objects.requireNonNull(this.queries, "queries cannot be null");
            return new AttributeSearchRequest(this);
        }
    }
}

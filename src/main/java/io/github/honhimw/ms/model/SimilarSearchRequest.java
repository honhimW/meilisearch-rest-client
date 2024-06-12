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

import java.util.List;

/**
 * @author hon_him
 * @since 2024-06-05 v1.9.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimilarSearchRequest extends FilterableAttributesRequest {

    /**
     * Mandatory: the external id of the target document
     */
    @Schema(description = "Mandatory: the external id of the target document")
    private String id;

    /**
     * Optional, defaults to 0: how many results to skip
     */
    @Schema(description = "Optional, defaults to 0: how many results to skip", defaultValue = "0")
    private Integer offset;

    /**
     * Optional, defaults to 20: how many results to display
     */
    @Schema(description = "Optional, defaults to 20: how many results to display", defaultValue = "20")
    private Integer limit;

    /**
     * Optional, defaults to the default embedder: name of the embedder to use for computing recommendations.
     */
    @Schema(description = "Optional, defaults to the default embedder: name of the embedder to use for computing recommendations.")
    private String embedder;

    /**
     * Optional, defaults to null: same as the search query parameter of the same name
     */
    @Schema(description = "Optional, defaults to null: same as the search query parameter of the same name")
    private List<String> attributesToRetrieve;

    /**
     * Display the global ranking score of a document, defaultValue = false
     */
    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScore;

    /**
     * Display the global ranking score of a document, defaultValue = false
     */
    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScoreDetails;

    private SimilarSearchRequest(Builder builder) {
        setFilter(builder.filter);
        setId(builder.id);
        setOffset(builder.offset);
        setLimit(builder.limit);
        setEmbedder(builder.embedder);
        setAttributesToRetrieve(builder.attributesToRetrieve);
        setShowRankingScore(builder.showRankingScore);
        setShowRankingScoreDetails(builder.showRankingScoreDetails);
    }

    public static Builder builder() {
        return new Builder();
    }


    /**
     * {@code SimilarSearchRequest} builder static inner class.
     */
    public static final class Builder {
        private String filter;
        private String id;
        private Integer offset;
        private Integer limit;
        private String embedder;
        private List<String> attributesToRetrieve;
        private Boolean showRankingScore;
        private Boolean showRankingScoreDetails;

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
         * Sets the {@code id} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(String val) {
            id = val;
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
         * Sets the {@code embedder} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code embedder} to set
         * @return a reference to this Builder
         */
        public Builder embedder(String val) {
            embedder = val;
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
         * Returns a {@code SimilarSearchRequest} built from the parameters previously set.
         *
         * @return a {@code SimilarSearchRequest} built with parameters of this {@code SimilarSearchRequest.Builder}
         */
        public SimilarSearchRequest build() {
            return new SimilarSearchRequest(this);
        }
    }
}

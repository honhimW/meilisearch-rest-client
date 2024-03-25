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
import lombok.*;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FacetSearchRequest extends FilterableAttributesRequest {

    @Schema(description = "Query string")
    private String q;

    @Schema(description = "Facet name to search values on", requiredMode = Schema.RequiredMode.REQUIRED)
    private String facetName;

    @Schema(description = "Search query for a given facet value. If facetQuery isn't specified, Meilisearch performs a placeholder search which returns all facet values for the searched facet, limited to 100")
    private String facetQuery;

    @Schema(description = "Strategy used to match query terms within documents")
    private String matchingStrategy;

    @Schema(description = "Restrict search to the specified attributes")
    private List<String> attributesToSearchOn;

    private FacetSearchRequest(Builder builder) {
        setQ(builder.q);
        setFacetName(builder.facetName);
        setFacetQuery(builder.facetQuery);
        setMatchingStrategy(builder.matchingStrategy);
        setAttributesToSearchOn(builder.attributesToSearchOn);
        setFilter(builder.filter);
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
     * {@code FacetSearchRequest} builder static inner class.
     */
    public static final class Builder {
        private String q;
        private String facetName;
        private String facetQuery;
        private String matchingStrategy;
        private List<String> attributesToSearchOn;
        private String filter;

        private Builder() {
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
         * Sets the {@code facetName} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code facetName} to set
         * @return a reference to this Builder
         */
        public Builder facetName(String val) {
            facetName = val;
            return this;
        }

        /**
         * Sets the {@code facetQuery} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code facetQuery} to set
         * @return a reference to this Builder
         */
        public Builder facetQuery(String val) {
            facetQuery = val;
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
         * Returns a {@code FacetSearchRequest} built from the parameters previously set.
         *
         * @return a {@code FacetSearchRequest} built with parameters of this {@code FacetSearchRequest.Builder}
         */
        public FacetSearchRequest build() {
            return new FacetSearchRequest(this);
        }
    }
}

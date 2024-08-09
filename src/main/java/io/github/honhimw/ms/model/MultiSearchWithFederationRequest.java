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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * MultiSearch with federation request
 *
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MultiSearchWithFederationRequest implements Serializable {

    /**
     * Contains the list of search queries to perform. The indexUid search parameter is required, all other parameters are optional
     */
    @Schema(description = "Contains the list of search queries to perform. The indexUid search parameter is required, all other parameters are optional")
    private List<SearchWithIndexAndFederationOptionsRequest> queries;

    /**
     * If federation is empty ({}) default values of offset and limit are used, so respectively 0 and 20.
     * If federation is null or missing, a classic multi-search will be applied, so a list of search result objects for each index will be returned.
     */
    @Schema(description = "return a single search result object, whose list of hits is built by merging the hits coming from all the queries in descending ranking score order", defaultValue = "null")
    private Federation federation;

    private MultiSearchWithFederationRequest(Builder builder) {
        setQueries(builder.queries);
        setFederation(builder.federation);
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return a new instance of the Builder class
     */
    public static MultiSearchWithFederationRequest.Builder builder() {
        return new MultiSearchWithFederationRequest.Builder();
    }


    /**
     * Federation
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Federation implements Serializable {

        /**
         * Number of documents to skip", defaultValue = "0
         */
        @Schema(description = "Number of documents to skip", defaultValue = "0")
        private Integer offset;

        /**
         * Maximum number of documents returned", defaultValue = "20
         */
        @Schema(description = "Maximum number of documents returned", defaultValue = "20")
        private Integer limit;

        /**
         * Sets the {@code offset} and returns a reference to this Builder enabling method chaining.
         *
         * @param offset the {@code offset} to set
         * @return this
         */
        public Federation offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        /**
         * Sets the {@code limit} and returns a reference to this Builder enabling method chaining.
         *
         * @param limit the {@code limit} to set
         * @return this
         */
        public Federation limit(Integer limit) {
            this.limit = limit;
            return this;
        }

    }

    /**
     * {@code MultiSearchRequest} builder static inner class.
     */
    public static final class Builder {
        private List<SearchWithIndexAndFederationOptionsRequest> queries;

        private Federation federation;

        private Builder() {
        }

        /**
         * Sets the {@code queries} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code queries} to set
         * @return a reference to this Builder
         */
        public Builder queries(List<SearchWithIndexAndFederationOptionsRequest> val) {
            queries = val;
            return this;
        }

        /**
         * Add the {@code query} and returns a reference to this Builder enabling method chaining.
         *
         * @param query the {@code queries} to added
         * @return a reference to this Builder
         */
        public Builder addQuery(SearchWithIndexAndFederationOptionsRequest query) {
            if (Objects.isNull(queries)) {
                queries = new ArrayList<>();
            }
            queries.add(query);
            return this;
        }

        /**
         * Add the {@code query} and returns a reference to this Builder enabling method chaining.
         *
         * @param indexUid the index uid
         * @param query    the {@code queries} to added
         * @return a reference to this Builder
         */
        public Builder addQuery(String indexUid, SearchRequest query) {
            if (Objects.isNull(queries)) {
                queries = new ArrayList<>();
            }
            queries.add(SearchWithIndexAndFederationOptionsRequest.from(indexUid, query));
            return this;
        }

        /**
         * Add the {@code query} and returns a reference to this Builder enabling method chaining.
         *
         * @param indexUid the index uid
         * @param weight   the weight
         * @param query    the {@code queries} to added
         * @return a reference to this Builder
         */
        public Builder addQuery(String indexUid, Number weight, SearchRequest query) {
            if (Objects.isNull(queries)) {
                queries = new ArrayList<>();
            }
            queries.add(SearchWithIndexAndFederationOptionsRequest.from(indexUid, query).federationOptions(weight));
            return this;
        }

        /**
         * Sets the {@code federation} and returns a reference to this Builder enabling method chaining.
         *
         * @param federation the {@code federation} to set
         * @return a reference to this Builder
         */
        public Builder federation(Federation federation) {
            this.federation = federation;
            return this;
        }

        /**
         * Sets the {@code federation} and returns a reference to this Builder enabling method chaining.
         *
         * @param consumer the {@code federation} configuration
         * @return a reference to this Builder
         */
        public Builder federation(Consumer<Federation> consumer) {
            if (Objects.isNull(this.federation)) {
                this.federation = new Federation();
            }
            consumer.accept(this.federation);
            return this;
        }

        /**
         * Returns a {@code MultiSearchRequest} built from the parameters previously set.
         *
         * @return a {@code MultiSearchRequest} built with parameters of this {@code MultiSearchRequest.Builder}
         */
        public MultiSearchWithFederationRequest build() {
            return new MultiSearchWithFederationRequest(this);
        }
    }
}

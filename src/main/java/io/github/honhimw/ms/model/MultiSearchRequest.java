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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MultiSearch request
 *
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MultiSearchRequest implements Serializable {

    /**
     * Contains the list of search queries to perform. The indexUid search parameter is required, all other parameters are optional
     */
    @Schema(description = "Contains the list of search queries to perform. The indexUid search parameter is required, all other parameters are optional")
    private List<SearchWithIndexRequest> queries;

    private MultiSearchRequest(Builder builder) {
        setQueries(builder.queries);
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
     * {@code MultiSearchRequest} builder static inner class.
     */
    public static final class Builder {
        private List<SearchWithIndexRequest> queries;

        private Builder() {
        }

        /**
         * Sets the {@code queries} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code queries} to set
         * @return a reference to this Builder
         */
        public Builder queries(List<SearchWithIndexRequest> val) {
            queries = val;
            return this;
        }

        /**
         * Add the {@code query} and returns a reference to this Builder enabling method chaining.
         *
         * @param query the {@code queries} to added
         * @return a reference to this Builder
         */
        public Builder addQuery(SearchWithIndexRequest query) {
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
            queries.add(SearchWithIndexRequest.from(indexUid, query));
            return this;
        }

        /**
         * Returns a {@code MultiSearchRequest} built from the parameters previously set.
         *
         * @return a {@code MultiSearchRequest} built with parameters of this {@code MultiSearchRequest.Builder}
         */
        public MultiSearchRequest build() {
            return new MultiSearchRequest(this);
        }
    }
}

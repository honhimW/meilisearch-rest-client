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
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#faceting-object">Faceting object</a>
 * <h2>Default object</h2>
 * <pre>
 * {
 *     "maxValuesPerFacet": 100,
 *     "sortFacetValuesBy": {
 *         "*": "alpha"
 *     }
 * }
 * </pre>
 *
 * @author hon_him
 * @since 2024-01-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Faceting implements Serializable {

    /**
     * Maximum number of facet values returned for each facet. Values are sorted in ascending lexicographical order
     */
    @Schema(description = "Maximum number of facet values returned for each facet. Values are sorted in ascending lexicographical order", defaultValue = "100")
    private Integer maxValuesPerFacet;

    /**
     * Customize facet order to sort by descending value count (count) or ascending alphanumeric order (alpha)
     */
    @Schema(description = "Customize facet order to sort by descending value count (count) or ascending alphanumeric order (alpha)", defaultValue = "{\"*\": \"alpha\"}")
    private Map<String, String> sortFacetValuesBy;

    private Faceting(Builder builder) {
        setMaxValuesPerFacet(builder.maxValuesPerFacet);
        setSortFacetValuesBy(builder.sortFacetValuesBy);
    }

    /**
     * Faceting setting default values object
     * @return Faceting default object
     */
    public static Faceting defaultObject() {
        Faceting faceting = new Faceting();
        faceting.setMaxValuesPerFacet(100);
        Map<String, String> sortFacetValuesBy = new HashMap<>();
        sortFacetValuesBy.put("*", "alpha");
        faceting.setSortFacetValuesBy(sortFacetValuesBy);
        return faceting;
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
     * {@code Faceting} builder static inner class.
     */
    public static final class Builder {
        private Integer maxValuesPerFacet;
        private Map<String, String> sortFacetValuesBy;

        private Builder() {
        }

        /**
         * Sets the {@code maxValuesPerFacet} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code maxValuesPerFacet} to set
         * @return a reference to this Builder
         */
        public Builder maxValuesPerFacet(Integer val) {
            maxValuesPerFacet = val;
            return this;
        }

        /**
         * Sets the {@code sortFacetValuesBy} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code sortFacetValuesBy} to set
         * @return a reference to this Builder
         */
        public Builder sortFacetValuesBy(Map<String, String> val) {
            sortFacetValuesBy = val;
            return this;
        }

        /**
         * Returns a {@code Faceting} built from the parameters previously set.
         *
         * @return a {@code Faceting} built with parameters of this {@code Faceting.Builder}
         */
        public Faceting build() {
            return new Faceting(this);
        }
    }
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * {
 *   "displayedAttributes": [
 *     "*"
 *   ],
 *   "searchableAttributes": [
 *     "*"
 *   ],
 *   "filterableAttributes": [],
 *   "sortableAttributes": [],
 *   "rankingRules":
 *   [
 *     "words",
 *     "typo",
 *     "proximity",
 *     "attribute",
 *     "sort",
 *     "exactness"
 *   ],
 *   "stopWords": [],
 *   "nonSeparatorTokens": [],
 *   "separatorTokens": [],
 *   "dictionary": [],
 *   "synonyms": {},
 *   "distinctAttribute": null,
 *   "typoTolerance": {
 *     "enabled": true,
 *     "minWordSizeForTypos": {
 *       "oneTypo": 5,
 *       "twoTypos": 9
 *     },
 *     "disableOnWords": [],
 *     "disableOnAttributes": []
 *   },
 *   "faceting": {
 *     "maxValuesPerFacet": 100
 *   },
 *   "pagination": {
 *     "maxTotalHits": 1000
 *   },
 *   "proximityPrecision": "byWord"
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
public class Setting implements Serializable {

    @Schema(description = "Fields displayed in the returned documents", defaultValue = "[\"*\"]")
    private List<String> displayedAttributes;

    @Schema(description = "Fields in which to search for matching query words sorted by order of importance", defaultValue = "[\"*\"]")
    private List<String> searchableAttributes;

    @Schema(description = "Attributes to use as filters and facets", defaultValue = "[]")
    private List<String> filterableAttributes;

    @Schema(description = "Attributes to use when sorting search results", defaultValue = "[]")
    private List<String> sortableAttributes;

    @Schema(description = "List of ranking rules in order of importance", defaultValue = "[\"words\",\"typo\",\"proximity\",\"attribute\",\"sort\",\"exactness\"]")
    private List<RankingRule> rankingRules;

    @Schema(description = "List of words ignored by Meilisearch when present in search queries", defaultValue = "[]")
    private List<String> stopWords;

    @Schema(description = "List of characters not delimiting where one term begins and ends", defaultValue = "[]")
    private List<String> nonSeparatorTokens;

    @Schema(description = "List of characters delimiting where one term begins and ends", defaultValue = "[]")
    private List<String> separatorTokens;

    @Schema(description = "List of strings Meilisearch should parse as a single term", defaultValue = "[]")
    private List<String> dictionary;

    @Schema(description = "List of associated words treated similarly", defaultValue = "{}")
    private Map<String, List<String>> synonyms;

    @Schema(description = "Search returns documents with distinct (different) values of the given field", defaultValue = "null")
    private String distinctAttribute;

    @Schema(description = "Typo tolerance settings", defaultValue = "default object")
    private TypoTolerance typoTolerance;

    @Schema(description = "Faceting settings", defaultValue = "default object")
    private Faceting faceting;

    @Schema(description = "Pagination settings", defaultValue = "default object")
    private Pagination pagination;

    @Schema(description = "Precision level when calculating the proximity ranking rule", defaultValue = "byWord")
    private ProximityPrecisionType proximityPrecision;

    @Schema(description = "To use vector search, first configure the embedders index setting. You may configure multiple embedders for an index.")
    private Map<String, ? extends Embedder> embedders;

    private Setting(Builder builder) {
        setDisplayedAttributes(builder.displayedAttributes);
        setSearchableAttributes(builder.searchableAttributes);
        setFilterableAttributes(builder.filterableAttributes);
        setSortableAttributes(builder.sortableAttributes);
        setRankingRules(builder.rankingRules);
        setStopWords(builder.stopWords);
        setNonSeparatorTokens(builder.nonSeparatorTokens);
        setSeparatorTokens(builder.separatorTokens);
        setDictionary(builder.dictionary);
        setSynonyms(builder.synonyms);
        setDistinctAttribute(builder.distinctAttribute);
        setTypoTolerance(builder.typoTolerance);
        setFaceting(builder.faceting);
        setPagination(builder.pagination);
        setProximityPrecision(builder.proximityPrecision);
        setEmbedders(builder.embedders);
    }

    /**
     * Returns a default Setting object with all attributes set to their default values.
     *
     * @return  a Setting object with default values for all attributes
     */
    public static Setting defaultObject() {
        Setting setting = new Setting();
        setting.setDisplayedAttributes(Stream.of("*").collect(Collectors.toList()));
        setting.setSearchableAttributes(Stream.of("*").collect(Collectors.toList()));
        setting.setFilterableAttributes(new ArrayList<>());
        setting.setSortableAttributes(new ArrayList<>());
        setting.setRankingRules(Stream.of(RankingRule.WORDS, RankingRule.TYPO, RankingRule.PROXIMITY, RankingRule.ATTRIBUTE, RankingRule.SORT, RankingRule.EXACTNESS).collect(Collectors.toList()));
        setting.setStopWords(new ArrayList<>());
        setting.setSeparatorTokens(new ArrayList<>());
        setting.setNonSeparatorTokens(new ArrayList<>());
        setting.setDictionary(new ArrayList<>());
        setting.setSynonyms(new HashMap<>());
        setting.setDistinctAttribute(null);
        setting.setTypoTolerance(TypoTolerance.defaultObject());
        setting.setFaceting(Faceting.defaultObject());
        setting.setPagination(Pagination.defaultObject());
        setting.setProximityPrecision(ProximityPrecisionType.BY_WORD);
        return setting;
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
     * {@code Setting} builder static inner class.
     */
    public static final class Builder {
        private List<String> displayedAttributes;
        private List<String> searchableAttributes;
        private List<String> filterableAttributes;
        private List<String> sortableAttributes;
        private List<RankingRule> rankingRules;
        private List<String> stopWords;
        private List<String> nonSeparatorTokens;
        private List<String> separatorTokens;
        private List<String> dictionary;
        private Map<String, List<String>> synonyms;
        private String distinctAttribute;
        private TypoTolerance typoTolerance;
        private Faceting faceting;
        private Pagination pagination;
        private ProximityPrecisionType proximityPrecision;
        private Map<String, ? extends Embedder> embedders;

        private Builder() {
        }

        /**
         * Sets the {@code displayedAttributes} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code displayedAttributes} to set
         * @return a reference to this Builder
         */
        public Builder displayedAttributes(List<String> val) {
            displayedAttributes = val;
            return this;
        }

        /**
         * Sets the {@code searchableAttributes} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code searchableAttributes} to set
         * @return a reference to this Builder
         */
        public Builder searchableAttributes(List<String> val) {
            searchableAttributes = val;
            return this;
        }

        /**
         * Sets the {@code filterableAttributes} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code filterableAttributes} to set
         * @return a reference to this Builder
         */
        public Builder filterableAttributes(List<String> val) {
            filterableAttributes = val;
            return this;
        }

        /**
         * Sets the {@code sortableAttributes} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code sortableAttributes} to set
         * @return a reference to this Builder
         */
        public Builder sortableAttributes(List<String> val) {
            sortableAttributes = val;
            return this;
        }

        /**
         * Sets the {@code rankingRules} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code rankingRules} to set
         * @return a reference to this Builder
         */
        public Builder rankingRules(List<RankingRule> val) {
            rankingRules = val;
            return this;
        }

        /**
         * Sets the {@code stopWords} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code stopWords} to set
         * @return a reference to this Builder
         */
        public Builder stopWords(List<String> val) {
            stopWords = val;
            return this;
        }

        /**
         * Sets the {@code nonSeparatorTokens} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code nonSeparatorTokens} to set
         * @return a reference to this Builder
         */
        public Builder nonSeparatorTokens(List<String> val) {
            nonSeparatorTokens = val;
            return this;
        }

        /**
         * Sets the {@code separatorTokens} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code separatorTokens} to set
         * @return a reference to this Builder
         */
        public Builder separatorTokens(List<String> val) {
            separatorTokens = val;
            return this;
        }

        /**
         * Sets the {@code dictionary} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code dictionary} to set
         * @return a reference to this Builder
         */
        public Builder dictionary(List<String> val) {
            dictionary = val;
            return this;
        }

        /**
         * Sets the {@code synonyms} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code synonyms} to set
         * @return a reference to this Builder
         */
        public Builder synonyms(Map<String, List<String>> val) {
            synonyms = val;
            return this;
        }

        /**
         * Sets the {@code distinctAttribute} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code distinctAttribute} to set
         * @return a reference to this Builder
         */
        public Builder distinctAttribute(String val) {
            distinctAttribute = val;
            return this;
        }

        /**
         * Sets the {@code typoTolerance} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code typoTolerance} to set
         * @return a reference to this Builder
         */
        public Builder typoTolerance(TypoTolerance val) {
            typoTolerance = val;
            return this;
        }

        /**
         * Sets the {@code faceting} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code faceting} to set
         * @return a reference to this Builder
         */
        public Builder faceting(Faceting val) {
            faceting = val;
            return this;
        }

        /**
         * Sets the {@code pagination} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code pagination} to set
         * @return a reference to this Builder
         */
        public Builder pagination(Pagination val) {
            pagination = val;
            return this;
        }

        /**
         * Sets the {@code proximityPrecision} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code proximityPrecision} to set
         * @return a reference to this Builder
         */
        public Builder proximityPrecision(ProximityPrecisionType val) {
            proximityPrecision = val;
            return this;
        }

        /**
         * Sets the {@code embedders} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code embedders} to set
         * @return a reference to this Builder
         */
        public Builder embedders(Map<String, ? extends Embedder> val) {
            embedders = val;
            return this;
        }

        /**
         * Returns a {@code Setting} built from the parameters previously set.
         *
         * @return a {@code Setting} built with parameters of this {@code Setting.Builder}
         */
        public Setting build() {
            return new Setting(this);
        }
    }
}

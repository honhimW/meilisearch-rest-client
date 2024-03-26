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
import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-02-22
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SearchDetails implements Serializable {

    /**
     * formatted document
     */
    @Schema(description = "formatted document")
    private Map<String, Object> _formatted;

    /**
     * the location of matched query terms within all attributes, even attributes that are not set as searchableAttributes.
     */
    @Schema(description = "the location of matched query terms within all attributes, even attributes that are not set as searchableAttributes.")
    private MatchesPosition _matchesPosition;

    /**
     * a sorting function that requires two floating point numbers indicating a location's latitude and longitude. You must also specify whether the sort should be ascending (asc) or descending (desc)
     */
    @Schema(description = "a sorting function that requires two floating point numbers indicating a location's latitude and longitude. You must also specify whether the sort should be ascending (asc) or descending (desc)")
    private Geo _geo;

    /**
     * the distance in meters between the document location
     */
    @Schema(description = "the distance in meters between the document location")
    private Integer _geoDistance;

    /**
     * a numeric value between 0.0 and 1.0. The higher the _rankingScore, the more relevant the document.
     */
    @Schema(description = "a numeric value between 0.0 and 1.0. The higher the _rankingScore, the more relevant the document.")
    private Double _rankingScore;

    /**
     * ranking score details
     */
    @Schema(description = "ranking score details")
    private RankingScoreDetails _rankingScoreDetails;

    /**
     * MatchesPosition
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchesPosition implements Serializable {

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private List<Position> overview;

        /**
         * Position
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Position implements Serializable {

            /**
             * start position
             */
            @Schema(description = "start position")
            private Integer start;

            /**
             * match length
             */
            @Schema(description = "match length")
            private Integer length;
        }
    }

    /**
     * Geo
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Geo implements Serializable {

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Double lat;

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Double lng;
    }

    /**
     * RankingScoreDetails
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingScoreDetails implements Serializable {

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Words words;

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Typo typo;

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Proximity proximity;

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Attribute attribute;

        /**
         * TODO
         */
        @Schema(description = "TODO")
        private Exactness exactness;

        /**
         * Words
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Words implements Serializable {

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer matchingWords;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer maxMatchingWords;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double score;
        }

        /**
         * Typo
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Typo implements Serializable {

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer typoCount;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer maxTypoCount;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double score;
        }

        /**
         * Proximity
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Proximity implements Serializable {

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double score;
        }

        /**
         * Attribute
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Attribute implements Serializable {

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double attributes_ranking_order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double attributes_query_word_order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double score;
        }

        /**
         * Exactness
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Exactness implements Serializable {

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer order;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private String matchType;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer matchingWords;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Integer maxMatchingWords;

            /**
             * TODO
             */
            @Schema(description = "TODO")
            private Double score;

        }

    }

}

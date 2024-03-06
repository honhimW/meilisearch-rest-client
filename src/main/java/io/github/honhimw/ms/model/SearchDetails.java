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

    @Schema(description = "formatted document")
    private Map<String, Object> _formatted;

    @Schema(description = "the location of matched query terms within all attributes, even attributes that are not set as searchableAttributes.")
    private MatchesPosition _matchesPosition;

    @Schema(description = "a sorting function that requires two floating point numbers indicating a location's latitude and longitude. You must also specify whether the sort should be ascending (asc) or descending (desc)")
    private Geo _geo;

    @Schema(description = "the distance in meters between the document location")
    private Integer _geoDistance;

    @Schema(description = "a numeric value between 0.0 and 1.0. The higher the _rankingScore, the more relevant the document.")
    private Double _rankingScore;

    @Schema(description = "ranking score details")
    private RankingScoreDetails _rankingScoreDetails;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchesPosition implements Serializable {
        @Schema(description = "TODO")
        private List<Position> overview;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Position implements Serializable {
            @Schema(description = "start position")
            private Integer start;

            @Schema(description = "match length")
            private Integer length;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Geo implements Serializable {
        @Schema(description = "TODO")
        private Double lat;

        @Schema(description = "TODO")
        private Double lng;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingScoreDetails implements Serializable {
        @Schema(description = "TODO")
        private Words words;

        @Schema(description = "TODO")
        private Typo typo;

        @Schema(description = "TODO")
        private Proximity proximity;

        @Schema(description = "TODO")
        private Attribute attribute;

        @Schema(description = "TODO")
        private Exactness exactness;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Words implements Serializable {
            @Schema(description = "TODO")
            private Integer order;

            @Schema(description = "TODO")
            private Integer matchingWords;

            @Schema(description = "TODO")
            private Integer maxMatchingWords;

            @Schema(description = "TODO")
            private Double score;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Typo implements Serializable {
            @Schema(description = "TODO")
            private Integer order;

            @Schema(description = "TODO")
            private Integer typoCount;

            @Schema(description = "TODO")
            private Integer maxTypoCount;

            @Schema(description = "TODO")
            private Double score;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Proximity implements Serializable {
            @Schema(description = "TODO")
            private Integer order;

            @Schema(description = "TODO")
            private Double score;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Attribute implements Serializable {
            @Schema(description = "TODO")
            private Integer order;

            @Schema(description = "TODO")
            private Double attributes_ranking_order;

            @Schema(description = "TODO")
            private Double attributes_query_word_order;

            @Schema(description = "TODO")
            private Double score;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Exactness implements Serializable {
            @Schema(description = "TODO")
            private Integer order;

            @Schema(description = "TODO")
            private String matchType;

            @Schema(description = "TODO")
            private Integer matchingWords;

            @Schema(description = "TODO")
            private Integer maxMatchingWords;

            @Schema(description = "TODO")
            private Double score;

        }

    }

}

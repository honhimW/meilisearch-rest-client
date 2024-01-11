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

import java.io.Serializable;
import java.util.*;
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
 *   }
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
@Builder(builderClassName = "Builder")
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
    private List<String> rankingRules;
    
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

    public static Setting defaultObject() {
        Setting setting = new Setting();
        setting.setDisplayedAttributes(Stream.of("*").collect(Collectors.toList()));
        setting.setSearchableAttributes(Stream.of("*").collect(Collectors.toList()));
        setting.setFilterableAttributes(new ArrayList<>());
        setting.setSortableAttributes(new ArrayList<>());
        setting.setRankingRules(Stream.of("words", "typo", "proximity", "attribute", "sort", "exactness").collect(Collectors.toList()));
        setting.setStopWords(new ArrayList<>());
        setting.setSeparatorTokens(new ArrayList<>());
        setting.setNonSeparatorTokens(new ArrayList<>());
        setting.setDictionary(new ArrayList<>());
        setting.setSynonyms(new HashMap<>());
        setting.setDistinctAttribute(null);
        setting.setTypoTolerance(TypoTolerance.defaultObject());
        setting.setFaceting(Faceting.defaultObject());
        setting.setPagination(Pagination.defaultObject());
        return setting;
    }

}

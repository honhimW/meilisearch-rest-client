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

import io.github.honhimw.ms.Experimental;
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
@Builder(builderClassName = "Builder")
public class SearchRequest extends FilterableAttributesRequest {

    @Schema(description = "Query string")
    private String q;

    @Schema(description = "Number of documents to skip", defaultValue = "0")
    private Integer offset;

    @Schema(description = "Maximum number of documents returned", defaultValue = "20")
    private Integer limit;

    @Schema(description = "Maximum number of documents returned for a page", defaultValue = "1")
    private Integer hitsPerPage;

    @Schema(description = "Request a specific page of results", defaultValue = "1")
    private Integer page;

    @Schema(description = "Display the count of matches per facet")
    private List<String> facets;

    @Schema(description = "Attributes to display in the returned documents", defaultValue = "[\"*\"]")
    private List<String> attributesToRetrieve;

    @Schema(description = "Attributes whose values have to be cropped")
    private List<String> attributesToCrop;

    @Schema(description = "Maximum length of cropped value in words", defaultValue = "10")
    private Integer cropLength;

    @Schema(description = "String marking crop boundaries", defaultValue = "\"...\"")
    private String cropMarker;

    @Schema(description = "Highlight matching terms contained in an attribute")
    private List<String> attributesToHighlight;

    @Schema(description = "String inserted at the start of a highlighted term", defaultValue = "\"<em>\"")
    private String highlightPreTag;

    @Schema(description = "String inserted at the end of a highlighted term", defaultValue = "\"</em>\"")
    private String highlightPostTag;

    @Schema(description = "Return matching terms location", defaultValue = "false")
    private Boolean showMatchesPosition;

    @Schema(description = "Sort search results by an attribute's value")
    private List<String> sort;

    @Schema(description = "Strategy used to match query terms within documents")
    private String matchingStrategy;

    @Schema(description = "Display the global ranking score of a document", defaultValue = "false")
    private Boolean showRankingScore;

    @Schema(description = "Restrict search to the specified attributes", defaultValue = "[\"*\"]")
    private List<String> attributesToSearchOn;

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Schema(description = "hybrid is an object and accepts two fields: semanticRatio and embedder")
    private Hybrid hybrid;

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Schema(description = "must be an array of numbers indicating the search vector. You must generate these yourself when using vector search with user-provided embeddings.")
    private List<Number> vector;
}

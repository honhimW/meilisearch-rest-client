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
@Builder(builderClassName = "Builder")
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
    
}

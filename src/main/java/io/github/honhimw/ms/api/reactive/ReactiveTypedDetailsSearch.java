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

package io.github.honhimw.ms.api.reactive;

import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * Meilisearch exposes 3 routes to perform document searches:
 * <ul>
 *     <li>A POST route: this is the preferred route when using API authentication, as it allows preflight request caching and better performances.</li>
 *     <li>A GET route: the usage of this route is discouraged, unless you have good reason to do otherwise (specific caching abilities for example). Other than the differences mentioned above, the two routes are strictly equivalent.</li>
 *     <li>A POST multi-search route allowing to perform multiple search queries in a single HTTP request. Meilisearch exposes 1 route to perform facet searches:</li>
 *     <li>A POST facet-search route allowing to perform a facet search query on a facet in a single HTTP request.</li>
 * </ul>
 *
 * @author hon_him
 * @since 2024-02-22 v1.7.0.0
 */

public interface ReactiveTypedDetailsSearch<T> {

    /**
     * Search for documents matching a specific query in the given index.
     * This is the preferred route to perform search when an API key is required, as it allows for preflight requests to be cached. Caching preflight requests improves considerably the speed of the search.
     *
     * @param q Query string
     * @return search result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/search")
    Mono<SearchDetailsResponse<T>> find(String q);

    /**
     * Search for documents matching a specific query in the given index.
     * This is the preferred route to perform search when an API key is required, as it allows for preflight requests to be cached. Caching preflight requests improves considerably the speed of the search.
     *
     * @param request SearchRequest
     * @return search result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/search")
    Mono<SearchDetailsResponse<T>> find(SearchRequest request);

    @Operation(method = "POST", tags = "/indexes/{indexUid}/search")
    default Mono<SearchDetailsResponse<T>> find(Consumer<SearchRequest.Builder> builder) {
        SearchRequest.Builder _builder = SearchRequest.builder();
        builder.accept(_builder);
        return find(_builder.build());
    }

    /**
     * Search for facet values matching a specific query for a facet. When many values exist for a facet,
     * users need to be able to discover non-show values they can select in order to refine their faceted search.
     *
     * @param request facet-search request
     * @return search result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/facet-search")
    Mono<FacetSearchResponse> facetSearch(FacetSearchRequest request);

    @Operation(method = "POST", tags = "/indexes/{indexUid}/facet-search")
    default Mono<FacetSearchResponse> facetSearch(Consumer<FacetSearchRequest.Builder> builder) {
        FacetSearchRequest.Builder _builder = FacetSearchRequest.builder();
        builder.accept(_builder);
        return facetSearch(_builder.build());
    }

}

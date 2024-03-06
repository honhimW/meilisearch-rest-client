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

package io.github.honhimw.ms.internal.reactive;

import io.github.honhimw.ms.api.reactive.ReactiveTypedDetailsSearch;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hon_him
 * @since 2024-02-22 v1.7.0.0
 */

class ReactiveTypedDetailsSearchImpl<T> extends AbstractReactiveImpl implements ReactiveTypedDetailsSearch<T> {

    private final String indexUid;
    private final TypeRef<T> typeRef;

    public ReactiveTypedDetailsSearchImpl(ReactiveIndexesImpl indexes, String indexUid, TypeRef<T> typeRef) {
        super(indexes._client);
        this.indexUid = indexUid;
        this.typeRef = typeRef;
    }

    @Override
    public Mono<SearchDetailsResponse<T>> find(String q) {
        Map<String, String> obj = new HashMap<>();
        obj.put("q", q);
        return post(String.format("/indexes/%s/search", indexUid), configurer -> json(configurer, jsonHandler.toJson(obj)), TypeRefs.StringObjectMapSearchResponseRef.INSTANCE)
            .map(this::transform);
    }

    @Override
    public Mono<SearchDetailsResponse<T>> find(SearchRequest request) {
        return post(String.format("/indexes/%s/search", indexUid), configurer -> json(configurer, jsonHandler.toJson(request)), TypeRefs.StringObjectMapSearchResponseRef.INSTANCE)
            .map(this::transform);
    }

    @Override
    public Mono<FacetSearchResponse> facetSearch(FacetSearchRequest request) {
        return post(String.format("/indexes/%s/facet-search", indexUid), configurer -> json(configurer, jsonHandler.toJson(request)), TypeRefs.of(FacetSearchResponse.class));
    }

    private SearchDetailsResponse<T> transform(SearchResponse<Map<String, Object>> searchResponse) {
        SearchDetailsResponse<T> response = new SearchDetailsResponse<>();
        response.setOffset(searchResponse.getOffset());
        response.setLimit(searchResponse.getLimit());
        response.setEstimatedTotalHits(searchResponse.getEstimatedTotalHits());
        response.setTotalHits(searchResponse.getTotalHits());
        response.setTotalPages(searchResponse.getTotalPages());
        response.setHitsPerPage(searchResponse.getHitsPerPage());
        response.setPage(searchResponse.getPage());
        response.setProcessingTimeMs(searchResponse.getProcessingTimeMs());
        response.setQuery(searchResponse.getQuery());
        response.setFacetDistribution(searchResponse.getFacetDistribution());
        response.setFacetStats(searchResponse.getFacetStats());
        List<Map<String, Object>> hits = searchResponse.getHits();
        if (CollectionUtils.isNotEmpty(hits)) {
            List<HitDetails<T>> searchDetails = hits.stream().map(_map -> {
                SearchDetails _searchDetails = jsonHandler.transform(_map, SearchDetails.class);
                T t = jsonHandler.transform(_map, typeRef);
                return new HitDetails<>(t, _searchDetails);
            }).collect(Collectors.toList());
            response.setHits(searchDetails);
        } else {
            response.setHits(Collections.emptyList());
        }
        return response;
    }

}

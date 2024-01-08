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

import io.github.honhimw.ms.api.reactive.ReactiveTypedSearch;
import io.github.honhimw.ms.json.ComplexTypeRef;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.FacetSearchRequest;
import io.github.honhimw.ms.model.FacetSearchResponse;
import io.github.honhimw.ms.model.SearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveTypedSearchImpl<T> extends AbstractReactiveImpl implements ReactiveTypedSearch<T> {

    private final String indexUid;
    private final TypeRef<T> typeRef;
    private final ComplexTypeRef<SearchResponse<T>> complexTypeRef;

    public ReactiveTypedSearchImpl(ReactiveIndexesImpl indexes, String indexUid, TypeRef<T> typeRef) {
        super(indexes._client);
        this.indexUid = indexUid;
        this.typeRef = typeRef;
        this.complexTypeRef = new ComplexTypeRef<SearchResponse<T>>(typeRef) {
        };
    }

    @Override
    public Mono<SearchResponse<T>> find(String q) {
        return post(String.format("/indexes/%s/search", indexUid), configurer -> configurer
                .body(payload -> payload.raw(raw -> {
                    Map<String, String> obj = new HashMap<>();
                    obj.put("q", q);
                    raw.json(jsonHandler.toJson(obj));
                }))
            , complexTypeRef);
    }

    @Override
    public Mono<SearchResponse<T>> find(SearchRequest request) {
        return post(String.format("/indexes/%s/search", indexUid), configurer -> configurer
                .body(payload -> payload.raw(raw -> raw.json(jsonHandler.toJson(request))))
            , complexTypeRef);
    }

    @Override
    public Mono<FacetSearchResponse> facetSearch(FacetSearchRequest request) {
        return post(String.format("/indexes/%s/facet-search", indexUid), configurer -> configurer
                .body(payload -> payload.raw(raw -> raw.json(jsonHandler.toJson(request))))
            , new TypeRef<FacetSearchResponse>() {
            });
    }
}

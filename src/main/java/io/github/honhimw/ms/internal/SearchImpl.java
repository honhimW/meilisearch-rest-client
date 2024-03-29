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

package io.github.honhimw.ms.internal;

import io.github.honhimw.ms.api.Search;
import io.github.honhimw.ms.api.reactive.ReactiveSearch;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.FacetSearchRequest;
import io.github.honhimw.ms.model.FacetSearchResponse;
import io.github.honhimw.ms.model.SearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.support.ReactorUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class SearchImpl implements Search {

    private final ReactiveSearch _search;

    public SearchImpl(ReactiveSearch search) {
        _search = search;
    }

    @Override
    public SearchResponse<Map<String, Object>> find(String q) {
        return ReactorUtils.blockNonNull(_search.find(q));
    }

    @Override
    public <T> SearchResponse<T> find(String q, TypeRef<T> typeRef) {
        return ReactorUtils.blockNonNull(_search.find(q, typeRef));
    }

    @Override
    public <T> SearchResponse<T> find(String q, Class<T> type) {
        return ReactorUtils.blockNonNull(_search.find(q, type));
    }

    @Override
    public SearchResponse<Map<String, Object>> find(SearchRequest request) {
        return ReactorUtils.blockNonNull(_search.find(request));
    }

    @Override
    public SearchResponse<Map<String, Object>> find(Consumer<SearchRequest.Builder> builder) {
        return ReactorUtils.blockNonNull(_search.find(builder));
    }

    @Override
    public <T> SearchResponse<T> find(SearchRequest request, TypeRef<T> typeRef) {
        return ReactorUtils.blockNonNull(_search.find(request, typeRef));
    }

    @Override
    public <T> SearchResponse<T> find(SearchRequest request, Class<T> type) {
        return ReactorUtils.blockNonNull(_search.find(request, type));
    }

    @Override
    public <T> SearchResponse<T> find(Consumer<SearchRequest.Builder> builder, Class<T> type) {
        return ReactorUtils.blockNonNull(_search.find(builder, type));
    }

    @Override
    public <T> SearchResponse<T> find(Consumer<SearchRequest.Builder> builder, TypeRef<T> typeRef) {
        return ReactorUtils.blockNonNull(_search.find(builder, typeRef));
    }

    @Override
    public FacetSearchResponse facetSearch(FacetSearchRequest request) {
        return ReactorUtils.blockNonNull(_search.facetSearch(request));
    }

    @Override
    public FacetSearchResponse facetSearch(Consumer<FacetSearchRequest.Builder> builder) {
        return ReactorUtils.blockNonNull(_search.facetSearch(builder));
    }
}

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

import io.github.honhimw.ms.api.reactive.ReactiveDocuments;
import io.github.honhimw.ms.json.ComplexTypeRef;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveDocumentsImpl extends AbstractReactiveImpl implements ReactiveDocuments {

    private final ReactiveIndexesImpl indexes;
    private final String indexUid;

    protected ReactiveDocumentsImpl(ReactiveIndexesImpl indexes, String indexUid) {
        super(indexes._client);
        this.indexes = indexes;
        this.indexUid = indexUid;
    }

    @Override
    public Mono<Page<Map<String, Object>>> list(@Nullable Integer offset, @Nullable Integer limit) {
        String _offset = Optional.ofNullable(offset).map(String::valueOf).orElse("0");
        String _limit = Optional.ofNullable(limit).map(String::valueOf).orElse("20");
        return get(String.format("/indexes/%s/documents", indexUid), configurer -> configurer
                .param("offset", _offset)
                .param("limit", _limit),
            new TypeRef<Page<Map<String, Object>>>() {
            });
    }

    @Override
    public <T> Mono<Page<T>> list(@Nullable Integer offset, @Nullable Integer limit, TypeRef<T> typeRef) {
        String _offset = Optional.ofNullable(offset).map(String::valueOf).orElse("0");
        String _limit = Optional.ofNullable(limit).map(String::valueOf).orElse("20");
        return get(String.format("/indexes/%s/documents", indexUid), configurer -> configurer
                .param("offset", _offset)
                .param("limit", _limit),
            new ComplexTypeRef<Page<T>>(typeRef) {
            });
    }

    @Override
    public Mono<TaskInfo> save(String json) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> save(Collection<?> collection) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> saveVectorized(Collection<VectorizedDocument> collection) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> update(String json) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> update(Collection<?> collection) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> deleteAll() {
        return delete(String.format("/indexes/%s/documents", indexUid), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<Page<Map<String, Object>>> batchGet(BatchGetDocumentsRequest fetch) {
        return post(String.format("/indexes/%s/documents/fetch", indexUid), configurer -> json(configurer, fetch), new TypeRef<Page<Map<String, Object>>>() {
        });
    }

    @Override
    public <T> Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef) {
        return post(String.format("/indexes/%s/documents/fetch", indexUid), configurer -> json(configurer, fetch),
            new ComplexTypeRef<Page<T>>(typeRef) {
            });
    }

    @Override
    public Mono<TaskInfo> batchDelete(List<String> ids) {
        return post(String.format("/indexes/%s/documents/delete-batch", indexUid), configurer -> json(configurer, ids), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> delete(FilterableAttributesRequest filter) {
        return post(String.format("/indexes/%s/documents/delete", indexUid), configurer -> json(configurer, filter), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<Map<String, Object>> get(String id, @Nullable String... fields) {
        String _fields;
        if (Objects.isNull(fields) || fields.length == 0) {
            _fields = "*";
        } else {
            _fields = String.join(",", fields);
        }
        return get(String.format("/indexes/%s/documents/%s", indexUid, id), configurer -> configurer
            .param("fields", _fields), new TypeRef<Map<String, Object>>() {
        });
    }

    @Override
    public <T> Mono<T> get(String id, TypeRef<T> typeRef, @Nullable String... fields) {
        String _fields;
        if (Objects.isNull(fields) || fields.length == 0) {
            _fields = "*";
        } else {
            _fields = String.join(",", fields);
        }
        return get(String.format("/indexes/%s/documents/%s", indexUid, id), configurer -> configurer
            .param("fields", _fields), typeRef);
    }

    @Override
    public Mono<TaskInfo> delete(String id) {
        return delete(String.format("/indexes/%s/documents/%s", indexUid, id), new TypeRef<TaskInfo>() {
        });
    }
}

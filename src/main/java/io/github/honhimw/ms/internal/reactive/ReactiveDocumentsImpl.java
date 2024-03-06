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
import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.StringUtils;
import io.github.honhimw.ms.support.TypeRefs;
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
    public Mono<Page<Map<String, Object>>> list(GetDocumentRequest page) {
        return list(page, TypeRefs.StringObjectMapRef.INSTANCE);
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
    public <T> Mono<Page<T>> list(GetDocumentRequest page, TypeRef<T> typeRef) {
        return get(String.format("/indexes/%s/documents", indexUid), configurer -> {
            List<String> fields = page.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                configurer.param("fields", String.join(",", fields));
            }
            String filter = page.getFilter();
            if (StringUtils.isNotEmpty(filter)) {
                configurer.param("filter", filter);
            }
            configurer
                .param("offset", String.valueOf(page.toOffset()))
                .param("limit", String.valueOf(page.toLimit()));
        }, new ComplexTypeRef<Page<T>>(typeRef) {
        });
    }

    @Override
    public Mono<TaskInfo> save(String json) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> save(Collection<?> collection) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> saveVectorized(Collection<VectorizedDocument> collection) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(String json) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(Collection<?> collection) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> deleteAll() {
        return delete(String.format("/indexes/%s/documents", indexUid), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<Page<Map<String, Object>>> batchGet(BatchGetDocumentsRequest fetch) {
        return post(String.format("/indexes/%s/documents/fetch", indexUid), configurer -> json(configurer, fetch), TypeRefs.PageStringObjectMapRef.INSTANCE);
    }

    @Override
    public <T> Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef) {
        return post(String.format("/indexes/%s/documents/fetch", indexUid), configurer -> json(configurer, fetch),
            new ComplexTypeRef<Page<T>>(typeRef) {
            });
    }

    @Override
    public Mono<TaskInfo> batchDelete(List<String> ids) {
        return post(String.format("/indexes/%s/documents/delete-batch", indexUid), configurer -> json(configurer, ids), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> delete(FilterableAttributesRequest filter) {
        return post(String.format("/indexes/%s/documents/delete", indexUid), configurer -> json(configurer, filter), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<Map<String, Object>> get(String id, @Nullable String... fields) {
        return get(id, TypeRefs.StringObjectMapRef.INSTANCE, fields);
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
        return delete(String.format("/indexes/%s/documents/%s", indexUid, id), TypeRefs.TaskInfoRef.INSTANCE);
    }
}

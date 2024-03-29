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

import io.github.honhimw.ms.api.reactive.ReactiveTypedDocuments;
import io.github.honhimw.ms.json.ComplexTypeRef;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.StringUtils;
import io.github.honhimw.ms.support.TypeRefs;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveTypedDocumentsImpl<T> extends AbstractReactiveImpl implements ReactiveTypedDocuments<T> {

    private final ReactiveIndexesImpl indexes;
    private final String indexUid;
    private final TypeRef<T> typeRef;
    private final ComplexTypeRef<Page<T>> complexTypeRef;

    protected ReactiveTypedDocumentsImpl(ReactiveIndexesImpl indexes, String indexUid, TypeRef<T> typeRef) {
        super(indexes._client);
        this.indexes = indexes;
        this.indexUid = indexUid;
        this.typeRef = typeRef;
        this.complexTypeRef = new ComplexTypeRef<Page<T>>(typeRef) {
        };
    }

    @Override
    public Mono<Page<T>> list(GetDocumentRequest page) {
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
        }, complexTypeRef);
    }

    @Override
    public Mono<TaskInfo> save(String json) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> save(Collection<? extends T> collection) {
        return post(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(String json) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, json), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(Collection<? extends T> collection) {
        return put(String.format("/indexes/%s/documents", indexUid), configurer -> json(configurer, collection), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> deleteAll() {
        return delete(String.format("/indexes/%s/documents", indexUid), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch) {
        return post(String.format("/indexes/%s/documents/fetch", indexUid), configurer -> json(configurer, fetch),
            complexTypeRef);
    }

    @Override
    public Mono<TaskInfo> batchDelete(Collection<String> ids) {
        return post(String.format("/indexes/%s/documents/delete-batch", indexUid), configurer -> json(configurer, ids), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> delete(FilterableAttributesRequest filter) {
        return post(String.format("/indexes/%s/documents/delete", indexUid), configurer -> json(configurer, filter), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<T> get(String id, @Nullable String... fields) {
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

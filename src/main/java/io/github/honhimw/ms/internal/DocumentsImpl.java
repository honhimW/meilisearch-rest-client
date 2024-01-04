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

import io.github.honhimw.ms.api.Documents;
import io.github.honhimw.ms.api.reactive.ReactiveDocuments;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.BatchGetDocumentsRequest;
import io.github.honhimw.ms.model.FilterableAttributesRequest;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-03
 */

class DocumentsImpl implements Documents {

    private final ReactiveDocuments _documents;

    protected DocumentsImpl(ReactiveDocuments documents) {
        this._documents = documents;
    }

    @Override
    public Page<Map<String, Object>> list(@Nullable Integer offset, @Nullable Integer limit) {
        return _documents.list(offset, limit).block();
    }

    @Override
    public <T> Page<T> list(@Nullable Integer offset, @Nullable Integer limit, TypeRef<T> typeRef) {
        return _documents.list(offset, limit, typeRef).block();
    }

    @Override
    public TaskInfo save(@Nullable String json) {
        return _documents.save(json).block();
    }

    @Override
    public TaskInfo update(@Nullable String json) {
        return _documents.update(json).block();
    }

    @Override
    public TaskInfo deleteAll() {
        return _documents.deleteAll().block();
    }

    @Override
    public Page<Map<String, Object>> batchGet(BatchGetDocumentsRequest fetch) {
        return _documents.batchGet(fetch).block();
    }

    @Override
    public <T> Page<Map<String, Object>> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef) {
        return _documents.batchGet(fetch, typeRef).block();
    }

    @Override
    public TaskInfo batchDelete(List<String> ids) {
        return _documents.batchDelete(ids).block();
    }

    @Override
    public TaskInfo delete(FilterableAttributesRequest filter) {
        return _documents.delete(filter).block();
    }

    @Override
    public Map<String, Object> get(String id, @Nullable String... fields) {
        return _documents.get(id, fields).block();
    }

    @Override
    public <T> T get(String id, TypeRef<T> typeRef, @Nullable String... fields) {
        return _documents.get(id, typeRef, fields).block();
    }

    @Override
    public TaskInfo delete(String id) {
        return _documents.delete(id).block();
    }
}

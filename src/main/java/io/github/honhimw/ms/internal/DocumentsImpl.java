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
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.Collection;
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
        return ReactorUtils.blockNonNull(_documents.list(offset, limit));
    }

    @Override
    public <T> Page<T> list(@Nullable Integer offset, @Nullable Integer limit, TypeRef<T> typeRef) {
        return ReactorUtils.blockNonNull(_documents.list(offset, limit, typeRef));
    }

    @Override
    public TaskInfo save(@Nullable String json) {
        return ReactorUtils.blockNonNull(_documents.save(json));
    }

    @Override
    public TaskInfo save(Collection<?> collection) {
        return ReactorUtils.blockNonNull(_documents.save(collection));
    }

    @Override
    public TaskInfo saveVectorized(Collection<VectorizedDocument> collection) {
        return ReactorUtils.blockNonNull(_documents.saveVectorized(collection));
    }

    @Override
    public TaskInfo update(String json) {
        return ReactorUtils.blockNonNull(_documents.update(json));
    }

    @Override
    public TaskInfo update(Collection<?> collection) {
        return ReactorUtils.blockNonNull(_documents.update(collection));
    }

    @Override
    public TaskInfo deleteAll() {
        return ReactorUtils.blockNonNull(_documents.deleteAll());
    }

    @Override
    public Page<Map<String, Object>> batchGet(BatchGetDocumentsRequest fetch) {
        return ReactorUtils.blockNonNull(_documents.batchGet(fetch));
    }

    @Override
    public <T> Page<T> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef) {
        return ReactorUtils.blockNonNull(_documents.batchGet(fetch, typeRef));
    }

    @Override
    public TaskInfo batchDelete(List<String> ids) {
        return ReactorUtils.blockNonNull(_documents.batchDelete(ids));
    }

    @Override
    public TaskInfo delete(FilterableAttributesRequest filter) {
        return ReactorUtils.blockNonNull(_documents.delete(filter));
    }

    @Override
    public Map<String, Object> get(String id, @Nullable String... fields) {
        return ReactorUtils.blockNonNull(_documents.get(id, fields));
    }

    @Override
    public <T> T get(String id, TypeRef<T> typeRef, @Nullable String... fields) {
        return ReactorUtils.blockNonNull(_documents.get(id, typeRef, fields));
    }

    @Override
    public TaskInfo delete(String id) {
        return ReactorUtils.blockNonNull(_documents.delete(id));
    }
}

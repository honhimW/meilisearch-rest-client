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

import io.github.honhimw.ms.api.TypedDocuments;
import io.github.honhimw.ms.api.reactive.ReactiveTypedDocuments;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-26
 */

class TypedDocumentImpl<T> implements TypedDocuments<T> {

    private final ReactiveTypedDocuments<T> _document;

    public TypedDocumentImpl(ReactiveTypedDocuments<T> search) {
        _document = search;
    }

    @Override
    public Page<T> list(@Nullable Integer offset, @Nullable Integer limit) {
        return ReactorUtils.blockNonNull(_document.list(offset, limit));
    }

    @Override
    public Page<T> list(Consumer<GetDocumentRequest> page) {
        return ReactorUtils.blockNonNull(_document.list(page));
    }

    @Override
    public Page<T> list(GetDocumentRequest page) {
        return ReactorUtils.blockNonNull(_document.list(page));
    }

    @Override
    public TaskInfo save(String json) {
        return ReactorUtils.blockNonNull(_document.save(json));
    }

    @Override
    public TaskInfo save(T t) {
        return ReactorUtils.blockNonNull(_document.save(t));
    }

    @Override
    public TaskInfo save(Collection<? extends T> collection) {
        return ReactorUtils.blockNonNull(_document.save(collection));
    }

    @Override
    public TaskInfo update(String json) {
        return ReactorUtils.blockNonNull(_document.update(json));
    }

    @Override
    public TaskInfo update(T t) {
        return ReactorUtils.blockNonNull(_document.update(t));
    }

    @Override
    public TaskInfo update(Collection<? extends T> collection) {
        return ReactorUtils.blockNonNull(_document.update(collection));
    }

    @Override
    public TaskInfo deleteAll() {
        return ReactorUtils.blockNonNull(_document.deleteAll());
    }

    @Override
    public Page<T> batchGet(BatchGetDocumentsRequest fetch) {
        return ReactorUtils.blockNonNull(_document.batchGet(fetch));
    }

    @Override
    public TaskInfo batchDelete(Collection<String> ids) {
        return ReactorUtils.blockNonNull(_document.batchDelete(ids));
    }

    @Override
    public TaskInfo delete(FilterableAttributesRequest filter) {
        return ReactorUtils.blockNonNull(_document.delete(filter));
    }

    @Override
    public Optional<T> get(String id, @Nullable String... fields) {
        return _document.get(id, fields).blockOptional();
    }

    @Override
    public TaskInfo delete(String id) {
        return ReactorUtils.blockNonNull(_document.delete(id));
    }
}

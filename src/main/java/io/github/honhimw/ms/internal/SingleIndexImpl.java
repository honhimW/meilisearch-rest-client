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

import io.github.honhimw.ms.api.*;
import io.github.honhimw.ms.api.reactive.ReactiveSingleIndex;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.IndexStats;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-03-08
 */

class SingleIndexImpl implements SingleIndex {

    private final ReactiveSingleIndex _index;

    protected SingleIndexImpl(ReactiveSingleIndex index) {
        this._index = index;
    }

    @Override
    public Optional<Index> get() {
        return _index.get().blockOptional();
    }

    @Override
    public TaskInfo create(@Nullable String primaryKey) {
        return ReactorUtils.blockNonNull(_index.create(primaryKey));
    }

    @Override
    public TaskInfo update(String primaryKey) {
        return ReactorUtils.blockNonNull(_index.update(primaryKey));
    }

    @Override
    public TaskInfo delete() {
        return ReactorUtils.blockNonNull(_index.delete());
    }

    @Override
    public Documents documents() {
        return new DocumentsImpl(_index.documents());
    }

    @Override
    public <T> TypedDocuments<T> documents(TypeRef<T> typeRef) {
        return new TypedDocumentImpl<>(_index.documents(typeRef));
    }

    @Override
    public Search search() {
        return new SearchImpl(_index.search());
    }

    @Override
    public <T> TypedSearch<T> search(TypeRef<T> typeRef) {
        return new TypedSearchImpl<>(_index.search(typeRef));
    }

    @Override
    public <T> TypedDetailsSearch<T> searchWithDetails(TypeRef<T> typeRef) {
        return new TypedDetailsSearchImpl<>(_index.searchWithDetails(typeRef));
    }

    @Override
    public Settings settings() {
        return new SettingsImpl(_index.settings());
    }

    @Override
    public IndexStats stats() {
        return ReactorUtils.blockNonNull(_index.stats());
    }
}

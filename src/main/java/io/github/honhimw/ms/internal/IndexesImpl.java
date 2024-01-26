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
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-03
 */

class IndexesImpl implements Indexes {

    private final ReactiveIndexes _indexes;

    protected IndexesImpl(ReactiveIndexes indexes) {
        this._indexes = indexes;
    }

    @Override
    public Page<Index> list(@Nullable Integer offset, @Nullable Integer limit) {
        return ReactorUtils.blockNonNull(_indexes.list(offset, limit));
    }

    @Override
    public Index get(String uid) {
        return ReactorUtils.blockNonNull(_indexes.get(uid));
    }

    @Override
    public TaskInfo create(String uid, @Nullable String primaryKey) {
        return ReactorUtils.blockNonNull(_indexes.create(uid, primaryKey));
    }

    @Override
    public TaskInfo update(String uid, String primaryKey) {
        return ReactorUtils.blockNonNull(_indexes.update(uid, primaryKey));
    }

    @Override
    public TaskInfo delete(String uid) {
        return ReactorUtils.blockNonNull(_indexes.delete(uid));
    }

    @Override
    public TaskInfo swap(List<Map.Entry<String, String>> uids) {
        return ReactorUtils.blockNonNull(_indexes.swap(uids));
    }

    @Override
    public Documents documents(String uid) {
        return new DocumentsImpl(_indexes.documents(uid));
    }

    @Override
    public <T> TypedDocuments<T> documents(String uid, TypeRef<T> typeRef) {
        return null;
    }

    @Override
    public Search search(String uid) {
        return new SearchImpl(_indexes.search(uid));
    }

    @Override
    public <T> TypedSearch<T> search(String uid, TypeRef<T> typeRef) {
        return new TypedSearchImpl<>(_indexes.search(uid, typeRef));
    }

    @Override
    public Settings settings(String uid) {
        return new SettingsImpl(_indexes.settings(uid));
    }

    @Override
    public Stats stats() {
        return ReactorUtils.blockNonNull(_indexes.stats());
    }

    @Override
    public IndexStats stats(String uid) {
        return ReactorUtils.blockNonNull(_indexes.stats(uid));
    }
}

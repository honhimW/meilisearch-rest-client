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
import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.Search;
import io.github.honhimw.ms.api.Settings;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.model.*;
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
        return _indexes.list(offset, limit).block();
    }

    @Override
    public Index get(String uid) {
        return _indexes.get(uid).block();
    }

    @Override
    public TaskInfo create(String uid, @Nullable String primaryKey) {
        return _indexes.create(uid, primaryKey).block();
    }

    @Override
    public TaskInfo update(String uid, String primaryKey) {
        return _indexes.update(uid, primaryKey).block();
    }

    @Override
    public TaskInfo delete(String uid) {
        return _indexes.delete(uid).block();
    }

    @Override
    public TaskInfo swap(List<Map.Entry<String, String>> uids) {
        return _indexes.swap(uids).block();
    }

    @Override
    public Documents documents(String uid) {
        return new DocumentsImpl(_indexes.documents(uid));
    }

    @Override
    public Search search(String uid) {
        return new SearchImpl(_indexes.search(uid));
    }

    @Override
    public Settings settings(String uid) {
        return new SettingsImpl(_indexes.settings(uid));
    }

    @Override
    public Stats stats() {
        return _indexes.stats().block();
    }

    @Override
    public IndexStats stats(String uid) {
        return _indexes.stats(uid).block();
    }
}

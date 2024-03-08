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

package io.github.honhimw.ms.internal.reactive;

import io.github.honhimw.ms.api.reactive.*;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.IndexStats;
import io.github.honhimw.ms.model.TaskInfo;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

/**
 * @author hon_him
 * @since 2024-03-08
 */

class ReactiveSingleIndexImpl extends AbstractReactiveImpl implements ReactiveSingleIndex {

    private final String uid;
    private final ReactiveIndexesImpl indexes;

    protected ReactiveSingleIndexImpl(String uid, ReactiveIndexesImpl indexes) {
        super(indexes._client);
        this.uid = uid;
        this.indexes = indexes;
    }

    @Override
    public Mono<Index> get() {
        return indexes.get(uid);
    }

    @Override
    public Mono<TaskInfo> create(@Nullable String primaryKey) {
        return indexes.create(uid, primaryKey);
    }

    @Override
    public Mono<TaskInfo> update(String primaryKey) {
        return indexes.update(uid, primaryKey);
    }

    @Override
    public Mono<TaskInfo> delete() {
        return indexes.delete(uid);
    }

    @Override
    public ReactiveDocuments documents() {
        return indexes.documents(uid);
    }

    @Override
    public <T> ReactiveTypedDocuments<T> documents(TypeRef<T> typeRef) {
        return indexes.documents(uid, typeRef);
    }

    @Override
    public ReactiveSearch search() {
        return indexes.search(uid);
    }

    @Override
    public <T> ReactiveTypedSearch<T> search(TypeRef<T> typeRef) {
        return indexes.search(uid, typeRef);
    }

    @Override
    public <T> ReactiveTypedDetailsSearch<T> searchWithDetails(TypeRef<T> typeRef) {
        return indexes.searchWithDetails(uid, typeRef);
    }

    @Override
    public ReactiveSettings settings() {
        return indexes.settings(uid);
    }

    @Override
    public Mono<IndexStats> stats() {
        return indexes.stats(uid);
    }
}

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
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.TypeRefs;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author hon_him
 * @since 2024-01-03
 */

class ReactiveIndexesImpl extends AbstractReactiveImpl implements ReactiveIndexes {

    protected ReactiveIndexesImpl(ReactiveMSearchClientImpl client) {
        super(client);
    }

    @Override
    public Mono<Page<Index>> list(@Nullable Integer offset, @Nullable Integer limit) {
        String _offset = Optional.ofNullable(offset).map(String::valueOf).orElse("0");
        String _limit = Optional.ofNullable(limit).map(String::valueOf).orElse("20");
        return get("/indexes", configurer -> configurer
                .param("offset", _offset)
                .param("limit", _limit),
            TypeRefs.PageIndexRef.INSTANCE);
    }

    @Override
    public Mono<Index> get(String uid) {
        return get(String.format("/indexes/%s", uid), configurer -> {
        }, TypeRefs.of(Index.class));
    }

    @Override
    public Mono<TaskInfo> create(String uid, @Nullable String primaryKey) {
        return post("/indexes", configurer -> configurer
            .body(payload -> payload
                .raw(raw -> {
                    Map<String, String> obj = new HashMap<>();
                    obj.put("uid", uid);
                    Optional.ofNullable(primaryKey)
                        .ifPresent(pk -> obj.put("primaryKey", pk));
                    raw.json(jsonHandler.toJson(obj));
                })), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(String uid, String primaryKey) {
        return patch(String.format("/indexes/%s", uid), configurer -> configurer
            .body(payload -> payload
                .raw(raw -> {
                    Map<String, String> obj = new HashMap<>();
                    obj.put("primaryKey", primaryKey);
                    raw.json(jsonHandler.toJson(obj));
                })), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> delete(String uid) {
        return delete(String.format("/indexes/%s", uid), configurer -> {
        }, TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> swap(List<Map.Entry<String, String>> uids) {
        return post("/swap-indexes", configurer -> configurer
            .body(payload -> payload.raw(raw -> {
                List<Map<String, List<String>>> list = new ArrayList<>();
                for (Map.Entry<String, String> uid : uids) {
                    Map<String, List<String>> swap = new HashMap<>();
                    List<String> indexes = new ArrayList<>(2);
                    indexes.add(uid.getKey());
                    indexes.add(uid.getValue());
                    swap.put("indexes", indexes);
                    list.add(swap);
                }
                raw.json(jsonHandler.toJson(list));
            })), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public ReactiveSingleIndex single(String uid) {
        return new ReactiveSingleIndexImpl(uid, this);
    }

    @Override
    public ReactiveDocuments documents(String uid) {
        return new ReactiveDocumentsImpl(this, uid);
    }

    @Override
    public <T> ReactiveTypedDocuments<T> documents(String uid, TypeRef<T> typeRef) {
        return new ReactiveTypedDocumentsImpl<>(this, uid, typeRef);
    }

    @Override
    public ReactiveSearch search(String uid) {
        return new ReactiveSearchImpl(this, uid);
    }

    @Override
    public <T> ReactiveTypedSearch<T> search(String uid, TypeRef<T> typeRef) {
        return new ReactiveTypedSearchImpl<>(this, uid, typeRef);
    }

    @Override
    public <T> ReactiveTypedDetailsSearch<T> searchWithDetails(String uid, TypeRef<T> typeRef) {
        return new ReactiveTypedDetailsSearchImpl<>(this, uid, typeRef);
    }

    @Override
    public ReactiveSettings settings(String uid) {
        return new ReactiveSettingsImpl(this, uid);
    }

    @Override
    public Mono<Stats> stats() {
        return get("/stats", TypeRefs.of(Stats.class));
    }

    @Override
    public Mono<IndexStats> stats(String uid) {
        return get(String.format("/indexes/%s/stats", uid), TypeRefs.of(IndexStats.class));
    }
}

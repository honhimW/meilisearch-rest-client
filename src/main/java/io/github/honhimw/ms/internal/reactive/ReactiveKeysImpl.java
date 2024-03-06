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

import io.github.honhimw.ms.api.reactive.ReactiveKeys;
import io.github.honhimw.ms.model.CreateKeyRequest;
import io.github.honhimw.ms.model.Key;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.UpdateKeyRequest;
import io.github.honhimw.ms.support.TypeRefs;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveKeysImpl extends AbstractReactiveImpl implements ReactiveKeys {


    protected ReactiveKeysImpl(ReactiveMSearchClientImpl client) {
        super(client);
    }

    @Override
    public Mono<Page<Key>> list(@Nullable Integer offset, @Nullable Integer limit) {
        return get("/keys", configurer -> {
                String _offset = Optional.ofNullable(offset).map(String::valueOf).orElse("0");
                String _limit = Optional.ofNullable(limit).map(String::valueOf).orElse("20");
                configurer
                    .param("offset", _offset)
                    .param("limit", _limit);
            }
            , TypeRefs.PageKeyRef.INSTANCE);
    }

    @Override
    public Mono<Key> get(String keyOrUid) {
        return get("/keys/" + keyOrUid, TypeRefs.of(Key.class));
    }

    @Override
    public Mono<Key> create(CreateKeyRequest request) {
        return post("/keys", configurer -> json(configurer, request), TypeRefs.of(Key.class));
    }

    @Override
    public Mono<Key> update(String keyOrUid, UpdateKeyRequest request) {
        return patch("/keys/" + keyOrUid, configurer -> json(configurer, request), TypeRefs.of(Key.class));
    }

    @Override
    public Mono<Void> delete(String keyOrUid) {
        return delete("/keys/" + keyOrUid, TypeRefs.VoidRef.INSTANCE);
    }
}

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

import io.github.honhimw.ms.api.Keys;
import io.github.honhimw.ms.api.reactive.ReactiveKeys;
import io.github.honhimw.ms.model.CreateKeyRequest;
import io.github.honhimw.ms.model.Key;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.UpdateKeyRequest;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class KeysImpl implements Keys {

    private final ReactiveKeys _reactive;

    KeysImpl(ReactiveKeys reactive) {
        _reactive = reactive;
    }

    @Override
    public Page<Key> list(@Nullable Integer offset, @Nullable Integer limit) {
        return ReactorUtils.blockNonNull(_reactive.list(offset, limit));
    }

    @Override
    public Key get(String keyOrUid) {
        return ReactorUtils.blockNonNull(_reactive.list(keyOrUid));
    }

    @Override
    public Key create(String keyOrUid, CreateKeyRequest request) {
        return ReactorUtils.blockNonNull(_reactive.create(keyOrUid, request));
    }

    @Override
    public Key update(String keyOrUid, UpdateKeyRequest request) {
        return ReactorUtils.blockNonNull(_reactive.update(keyOrUid, request));
    }

    @Override
    public void delete(String keyOrUid) {
        _reactive.delete(keyOrUid).block();
    }
}

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

import io.github.honhimw.ms.api.reactive.ReactiveLogs;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.LogStreamRequest;
import reactor.core.publisher.Mono;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveLogsImpl extends AbstractReactiveImpl implements ReactiveLogs {

    protected ReactiveLogsImpl(ReactiveMSearchClientImpl client) {
        super(client);
    }

    @Override
    public Mono<Void> update(LogStreamRequest request) {
        return post("/logs/stream", configurer -> json(configurer, request), new TypeRef<Void>() {
        });
    }

    @Override
    public Mono<Void> stderr(LogStreamRequest request) {
        return post("/logs/stderr", configurer -> json(configurer, request), new TypeRef<Void>() {
        });
    }

    @Override
    public Mono<Void> reset() {
        return delete("/logs/stream", new TypeRef<Void>() {
        });
    }
}

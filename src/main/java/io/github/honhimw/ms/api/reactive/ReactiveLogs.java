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

package io.github.honhimw.ms.api.reactive;

import io.github.honhimw.ms.model.LogStreamRequest;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * Configure the stream using the same parameters as regular logging: mode and target. The stream will continue to run indefinitely until you interrupt it.
 *
 * @author hon_him
 * @since 2024-02-22
 */

public interface ReactiveLogs {

    /**
     * Focused debugging sessions
     * @param request request
     * @return None
     */
    @Operation(method = "POST", tags = "/logs/stream")
    Mono<Void> update(LogStreamRequest request);

    /**
     * Focused debugging sessions
     * @param builder request builder
     * @return None
     */
    default Mono<Void> update(Consumer<LogStreamRequest.Builder> builder) {
        LogStreamRequest.Builder _builder = LogStreamRequest.builder();
        builder.accept(_builder);
        return update(_builder.build());
    }

    /**
     * Customize logging levels for the default logging system.
     * @param request request
     * @return None
     */
    @Operation(method = "POST", tags = "/logs/stderr")
    Mono<Void> stderr(LogStreamRequest request);

    /**
     * Customize logging levels for the default logging system.
     * @param builder request builder
     * @return None
     */
    default Mono<Void> stderr(Consumer<LogStreamRequest.Builder> builder) {
        LogStreamRequest.Builder _builder = LogStreamRequest.builder();
        builder.accept(_builder);
        _builder.mode(null);
        return stderr(_builder.build());
    }

    /**
     * Reset the stream
     * @return None
     */
    @Operation(method = "DELETE", tags = "/logs/stream")
    Mono<Void> reset();

}

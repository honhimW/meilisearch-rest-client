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

package io.github.honhimw.ms.support;

import jakarta.annotation.Nonnull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-12
 */

public class ReactorUtils {

    @Nonnull
    public static <R> R blockNonNull(@Nonnull Mono<R> mono) {
        R result = mono.block();
        if (Objects.isNull(result)) {
            throw new NoSuchElementException("blocking with an empty result.");
        }
        return result;
    }

    @Nonnull
    public static <R> List<R> blockNonNull(@Nonnull Flux<R> flux) {
        List<R> result = flux.collectList().block();
        if (Objects.isNull(result)) {
            throw new NoSuchElementException("blocking with an empty result.");
        }
        return result;
    }

}

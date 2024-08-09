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

import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.api.annotation.Operation;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#sortable-attributes">Search cutoff</a>
 * <p>
 * Configure the maximum duration of a search query. Meilisearch will interrupt any search taking longer than the cutoff value.
 * <p>
 * By default, Meilisearch interrupts searches after 1500 milliseconds.
 *
 * @author hon_him
 * @since 2024-05-17
 */

public interface ReactiveCutoffSettings {

    /**
     * Get an index's search cutoff value.
     *
     * @return current index sortable attribute
     */
    @Operation(method = "GET", paths = "/indexes/{index_uid}/settings/search-cutoff-ms")
    Mono<Duration> get();

    /**
     * Update an index's search cutoff value.
     *
     * @param cutoff the maximum duration of a search query.
     * @return update task
     */
    @Operation(method = "PUT", paths = "/indexes/{index_uid}/settings/search-cutoff-ms")
    Mono<TaskInfo> update(Duration cutoff);

    /**
     * Reset an index's search cutoff value to its default value, null. This translates to a cutoff of 1500ms.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", paths = "/indexes/{index_uid}/settings/search-cutoff-ms")
    Mono<TaskInfo> reset();

}

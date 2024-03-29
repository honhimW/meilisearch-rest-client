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

import io.github.honhimw.ms.Experimental;
import io.github.honhimw.ms.model.Embedder;
import io.github.honhimw.ms.model.Pagination;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.MapBuilder;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Consumer;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/learn/experimental/vector_search#deactivate-vector-search">Deactivate vector search</a>
 *
 * @author hon_him
 * @see Pagination
 * @since 2024-01-18 V1.6
 */

@Experimental(features = Experimental.Features.VECTOR_SEARCH)
public interface ReactiveEmbeddersSettings {

    /**
     * Get current embedders configuration from your index:
     *
     * @return current embedders
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/embedders")
    Mono<Map<String, ? extends Embedder>> get();

    /**
     * Update all embedder configuration from your index:
     *
     * @param embedders embedders
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/embedders")
    Mono<TaskInfo> update(Map<String, ? extends Embedder> embedders);

    /**
     * Update all embedder configuration from your index:
     * @param embedders embedders builder
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/embedders")
    default Mono<TaskInfo> update(Consumer<MapBuilder<String, ? extends Embedder>> embedders) {
        MapBuilder<String, Embedder> builder = MapBuilder.builder();
        embedders.accept(builder);
        return update(builder.build());
    }

    /**
     * Manually remove all embedder configuration from your index:
     * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
     * <pre>
     * If you don't remove all embedders, Meilisearch will continue auto-generating embeddings for you documents. This will happen even if vectorStore has been set to false and may lead to unexpected expenses when using OpenAI's paid tiers.
     * </pre>
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/embedders")
    Mono<TaskInfo> reset();

}

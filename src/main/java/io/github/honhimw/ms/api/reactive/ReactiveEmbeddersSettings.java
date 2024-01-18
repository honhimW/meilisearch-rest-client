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
import io.github.honhimw.ms.model.Pagination;
import io.github.honhimw.ms.model.ProximityPrecision;
import io.github.honhimw.ms.model.ProximityPrecisionType;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

/**
 * <a href="https://www.meilisearch.com/docs/learn/experimental/vector_search#deactivate-vector-search"><h1>Deactivate vector search</h1></a>
 *
 * @author hon_him
 * @see Pagination
 * @since 2024-01-18 V1.6
 */

@Experimental(feature = "vector-search")
public interface ReactiveEmbeddersSettings {

    /**
     * Manually remove all embedder configuration from your index:
     * <h2 style="color:orange">WARNING</h2>
     * <pre>
     * If you don't remove all embedders, Meilisearch will continue auto-generating embeddings for you documents. This will happen even if vectorStore has been set to false and may lead to unexpected expenses when using OpenAI's paid tiers.
     * </pre>
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/embedders")
    Mono<TaskInfo> reset();

}

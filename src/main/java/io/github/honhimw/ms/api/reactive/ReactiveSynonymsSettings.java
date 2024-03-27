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
import io.github.honhimw.ms.support.MapBuilder;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#synonyms">Synonyms</a>
 * <p>
 * The synonyms object contains words and their respective synonyms. A synonym in Meilisearch is considered equal to its associated word for the purposes of calculating search results.
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/configuration/synonyms">To learn more about synonyms, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveSynonymsSettings {

    /**
     * Get the list of synonyms of an index.
     *
     * @return current index stop words
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/synonyms")
    Mono<Map<String, List<String>>> get();

    /**
     * Update the list of synonyms of an index. Synonyms are normalized.
     *
     * @param synonyms An object that contains all synonyms and their associated words. Add the associated words in an array to set a synonym for a word.
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/synonyms")
    Mono<TaskInfo> update(Map<String, List<String>> synonyms);

    /**
     * Update the list of synonyms of an index.
     * @param synonyms map builder
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/synonyms")
    default Mono<TaskInfo> update(Consumer<MapBuilder<String, List<String>>> synonyms) {
        MapBuilder<String, List<String>> builder = MapBuilder.builder();
        synonyms.accept(builder);
        return update(builder.build());
    }

    /**
     * Reset the list of synonyms of an index to its default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/synonyms")
    Mono<TaskInfo> reset();

}

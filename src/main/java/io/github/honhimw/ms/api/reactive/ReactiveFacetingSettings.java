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

import io.github.honhimw.ms.model.Faceting;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#faceting"><h1>Faceting</h1></a>
 * With Meilisearch, you can create faceted search interfaces. This setting allows you to:
 * <ul>
 *     <li>Define the maximum number of values returned by the facets search parameter</li>
 *     <li>Sort facet values by value count or alphanumeric order</li>
 * </ul>
 * <p>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/fine_tuning_results/faceted_search">To learn more about faceting, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @see Faceting
 * @since 2024-01-03
 */

public interface ReactiveFacetingSettings {

    /**
     * Get the faceting settings of an index.
     *
     * @return current index faceting
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/faceting")
    Mono<Faceting> get();

    /**
     * Partially update the faceting settings for an index.
     * Any parameters not provided in the body will be left unchanged.
     *
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/faceting")
    Mono<TaskInfo> update(Faceting faceting);

    @Operation(method = "PATCH", tags = "/indexes/{index_uid}/settings/faceting")
    default Mono<TaskInfo> update(Consumer<Faceting.Builder> builder) {
        Faceting.Builder _builder = Faceting.builder();
        builder.accept(_builder);
        return update(_builder.build());
    }

    /**
     * Reset an index's faceting settings to their default value.
     * Setting sortFacetValuesBy to null( --data-binary '{ "sortFacetValuesBy": null }'), will restore it to the default value ("*": "alpha").
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/faceting")
    Mono<TaskInfo> reset();

}

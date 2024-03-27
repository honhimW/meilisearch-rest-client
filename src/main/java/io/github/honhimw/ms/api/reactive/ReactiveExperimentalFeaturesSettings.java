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

import io.github.honhimw.ms.model.ExperimentalFeatures;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/experimental_features">Experimental features</a>
 * <p>
 * The /experimental-features route allows you to activate or deactivate some of Meilisearch's experimental features.
 * <p>
 * This route is synchronous. This means that no task object will be returned, and any activated or deactivated features will be made available or unavailable immediately.
 * <p style="color:red;font-weight:bold;font-size:large">DANGER</p>
 * <pre>
 * The experimental API route is not compatible with all experimental features. Consult the experimental feature overview for a compatibility list.
 * </pre>
 * <a style="color:red" href="https://www.meilisearch.com/docs/learn/fine_tuning_results/sorting">To learn more about sortable attributes, refer to our dedicated guide.</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveExperimentalFeaturesSettings {

    /**
     * Get a list of all experimental features that can be activated via the /experimental-features route and whether or not they are currently activated.
     *
     * @return current index sortable attribute
     */
    @Operation(method = "GET", tags = "/experimental-features")
    Mono<ExperimentalFeatures> get();

    /**
     * Activate or deactivate experimental features.
     *
     * @param configure feature configure
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/experimental-features")
    Mono<ExperimentalFeatures> configure(ExperimentalFeatures configure);

    /**
     * Activate or deactivate experimental features.
     * @param builder feature builder
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/experimental-features")
    default Mono<ExperimentalFeatures> configure(Consumer<ExperimentalFeatures.Builder> builder) {
        ExperimentalFeatures.Builder _builder = ExperimentalFeatures.builder();
        builder.accept(_builder);
        return configure(_builder.build());
    }

}

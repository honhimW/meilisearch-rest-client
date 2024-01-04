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

import io.github.honhimw.ms.api.reactive.ReactiveExperimentalFeaturesSettings;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.ExperimentalFeatures;
import reactor.core.publisher.Mono;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveExperimentalFeaturesSettingsImpl extends AbstractReactiveImpl implements ReactiveExperimentalFeaturesSettings {

    protected ReactiveExperimentalFeaturesSettingsImpl(ReactiveMSearchClientImpl client) {
        super(client);
    }

    @Override
    public Mono<ExperimentalFeatures> get() {
        return get("/experimental-features", new TypeRef<ExperimentalFeatures>() {
        });
    }

    @Override
    public Mono<ExperimentalFeatures> configure(ExperimentalFeatures configure) {
        return patch("/experimental-features", configurer -> json(configurer, configure), new TypeRef<ExperimentalFeatures>() {
        });
    }
}

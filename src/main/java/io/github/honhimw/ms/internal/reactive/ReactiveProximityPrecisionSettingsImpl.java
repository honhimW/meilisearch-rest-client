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

import io.github.honhimw.ms.api.reactive.ReactiveProximityPrecisionSettings;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.ProximityPrecision;
import io.github.honhimw.ms.model.ProximityPrecisionType;
import io.github.honhimw.ms.model.TaskInfo;
import reactor.core.publisher.Mono;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveProximityPrecisionSettingsImpl extends AbstractReactiveImpl implements ReactiveProximityPrecisionSettings {

    private static final String PATH = "/indexes/%s/settings/proximity-precision";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactiveProximityPrecisionSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<ProximityPrecision> get() {
        return get(path, new TypeRef<ProximityPrecision>() {
        });
    }

    @Override
    public Mono<TaskInfo> update(ProximityPrecisionType type) {
        return put(path, configurer -> json(configurer, type), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, new TypeRef<TaskInfo>() {
        });
    }
}

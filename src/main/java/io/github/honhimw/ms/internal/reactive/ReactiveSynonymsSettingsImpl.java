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

import io.github.honhimw.ms.api.reactive.ReactiveSynonymsSettings;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveSynonymsSettingsImpl extends AbstractReactiveImpl implements ReactiveSynonymsSettings {

    private static final String PATH = "/indexes/%s/settings/synonyms";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactiveSynonymsSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<Map<String, List<String>>> get() {
        return get(path, TypeRefs.StringStringListMapRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(Map<String, List<String>> synonyms) {
        return put(path, configurer -> json(configurer, synonyms), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, TypeRefs.TaskInfoRef.INSTANCE);
    }
}

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

import io.github.honhimw.ms.api.reactive.ReactiveSeparatorTokensSettings;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.TaskInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveSeparatorTokensSettingsImpl extends AbstractReactiveImpl implements ReactiveSeparatorTokensSettings {

    private static final String PATH = "/indexes/%s/settings/separator-tokens";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactiveSeparatorTokensSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<List<String>> get() {
        return get(path, new TypeRef<List<String>>() {
        });
    }

    @Override
    public Mono<TaskInfo> update(List<String> searchableAttributes) {
        return put(path, configurer -> json(configurer, searchableAttributes), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, new TypeRef<TaskInfo>() {
        });
    }
}

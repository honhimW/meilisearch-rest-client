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

import io.github.honhimw.ms.api.reactive.ReactivePaginationSettings;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.Pagination;
import io.github.honhimw.ms.model.TaskInfo;
import reactor.core.publisher.Mono;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactivePaginationSettingsImpl extends AbstractReactiveImpl implements ReactivePaginationSettings {

    private static final String PATH = "/indexes/%s/settings/pagination";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactivePaginationSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<Pagination> get() {
        return get(path, new TypeRef<Pagination>() {
        });
    }

    @Override
    public Mono<TaskInfo> update(Pagination pagination) {
        return patch(path, configurer -> json(configurer, pagination), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, new TypeRef<TaskInfo>() {
        });
    }
}

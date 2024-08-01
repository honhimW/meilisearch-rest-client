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

import io.github.honhimw.ms.api.reactive.ReactiveLocalizedAttributesSettings;
import io.github.honhimw.ms.api.reactive.ReactiveSettings;
import io.github.honhimw.ms.model.LocalizedAttribute;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-08-01
 */

class ReactiveLocalizedAttributesSettingsImpl extends AbstractReactiveImpl implements ReactiveLocalizedAttributesSettings {

    private static final String PATH = "/indexes/%s/settings/localized-attributes";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactiveLocalizedAttributesSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<List<LocalizedAttribute>> get() {
        return get(path, TypeRefs.LocalizedAttributeListRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> update(List<LocalizedAttribute> localizedAttributes) {
        return put(path, configurer -> json(configurer, localizedAttributes), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, TypeRefs.TaskInfoRef.INSTANCE);
    }
}

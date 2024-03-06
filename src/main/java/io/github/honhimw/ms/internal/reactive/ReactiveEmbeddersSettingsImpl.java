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

import io.github.honhimw.ms.api.reactive.ReactiveEmbeddersSettings;
import io.github.honhimw.ms.model.Embedder;
import io.github.honhimw.ms.model.EmbedderSource;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-18
 */

class ReactiveEmbeddersSettingsImpl extends AbstractReactiveImpl implements ReactiveEmbeddersSettings {

    private static final String PATH = "/indexes/%s/settings/embedders";

    private final ReactiveSettingsImpl _settings;

    private final String path;

    protected ReactiveEmbeddersSettingsImpl(ReactiveSettingsImpl settings) {
        super(settings._client);
        this._settings = settings;
        this.path = String.format(PATH, settings.indexUid);
    }

    @Override
    public Mono<Map<String, ? extends Embedder>> get() {
        return get(path, TypeRefs.StringStringObjectMapMapRef.INSTANCE).map(stringMapMap -> {
            Map<String, Embedder> map = new HashMap<>();
            stringMapMap.forEach((s, stringObjectMap) -> {
                Object source = stringObjectMap.get("source");
                EmbedderSource embedderSource = EmbedderSource.of(String.valueOf(source));
                Embedder embedder;
                String json = jsonHandler.toJson(stringObjectMap);
                switch (embedderSource) {
                    case OPEN_AI:
                        embedder = jsonHandler.fromJson(json, Embedder.OpenAI.class);
                        break;
                    case HUGGING_FACE:
                        embedder = jsonHandler.fromJson(json, Embedder.HuggingFace.class);
                        break;
                    case USER_PROVIDED:
                        embedder = jsonHandler.fromJson(json, Embedder.Custom.class);
                        break;
                    default:
                        embedder = null;
                        break;
                }
                map.put(s, embedder);
            });
            return map;
        });
    }

    @Override
    public Mono<TaskInfo> update(Map<String, ? extends Embedder> embedders) {
        return patch(path, configurer -> json(configurer, embedders), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(path, TypeRefs.TaskInfoRef.INSTANCE);
    }
}

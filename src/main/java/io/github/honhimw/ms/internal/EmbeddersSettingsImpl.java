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

package io.github.honhimw.ms.internal;

import io.github.honhimw.ms.api.EmbeddersSettings;
import io.github.honhimw.ms.api.reactive.ReactiveEmbeddersSettings;
import io.github.honhimw.ms.model.Embedder;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class EmbeddersSettingsImpl implements EmbeddersSettings {

    private final ReactiveEmbeddersSettings _reactive;

    EmbeddersSettingsImpl(ReactiveEmbeddersSettings reactive) {
        _reactive = reactive;
    }

    @Nullable
    @Override
    public Map<String, ? extends Embedder> get() {
        return _reactive.get().block();
    }

    @Override
    public TaskInfo update(Map<String, ? extends Embedder> embedders) {
        return ReactorUtils.blockNonNull(_reactive.update(embedders));
    }

    @Override
    public TaskInfo reset() {
        return ReactorUtils.blockNonNull(_reactive.reset());
    }
}

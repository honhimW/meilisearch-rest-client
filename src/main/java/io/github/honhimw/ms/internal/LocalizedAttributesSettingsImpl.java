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

import io.github.honhimw.ms.api.LocalizedAttributesSettings;
import io.github.honhimw.ms.api.reactive.ReactiveLocalizedAttributesSettings;
import io.github.honhimw.ms.model.LocalizedAttribute;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-08-01
 */

class LocalizedAttributesSettingsImpl implements LocalizedAttributesSettings {

    private final ReactiveLocalizedAttributesSettings _reactive;

    protected LocalizedAttributesSettingsImpl(ReactiveLocalizedAttributesSettings reactive) {
        this._reactive = reactive;
    }

    @Nullable
    @Override
    public List<LocalizedAttribute> get() {
        return _reactive.get().block();
    }

    @Override
    public TaskInfo update(List<LocalizedAttribute> localizedAttributes) {
        return ReactorUtils.blockNonNull(_reactive.update(localizedAttributes));
    }

    @Override
    public TaskInfo reset() {
        return ReactorUtils.blockNonNull(_reactive.reset());
    }
}

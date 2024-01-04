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

import io.github.honhimw.ms.api.TypoToleranceSettings;
import io.github.honhimw.ms.api.reactive.ReactiveTypoToleranceSettings;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TypoTolerance;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class TypoToleranceSettingsImpl implements TypoToleranceSettings {

    private final ReactiveTypoToleranceSettings _reactive;

    TypoToleranceSettingsImpl(ReactiveTypoToleranceSettings reactive) {
        _reactive = reactive;
    }

    @Override
    public TypoTolerance get() {
        return _reactive.get().block();
    }

    @Override
    public TaskInfo update(TypoTolerance typoTolerance) {
        return _reactive.update(typoTolerance).block();
    }

    @Override
    public TaskInfo reset() {
        return _reactive.reset().block();
    }
}

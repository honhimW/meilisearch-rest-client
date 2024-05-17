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

import io.github.honhimw.ms.api.CutoffSettings;
import io.github.honhimw.ms.api.reactive.ReactiveCutoffSettings;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;

import java.time.Duration;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-05-17
 */

class CutoffSettingsImpl implements CutoffSettings {

    private final ReactiveCutoffSettings _reactive;

    CutoffSettingsImpl(ReactiveCutoffSettings _reactive) {
        this._reactive = _reactive;
    }

    @Override
    public Optional<Duration> get() {
        return _reactive.get().blockOptional();
    }

    @Override
    public TaskInfo update(Duration cutoff) {
        return ReactorUtils.blockNonNull(_reactive.update(cutoff));
    }

    @Override
    public TaskInfo reset() {
        return ReactorUtils.blockNonNull(_reactive.reset());
    }
}

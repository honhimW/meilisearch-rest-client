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

import io.github.honhimw.ms.api.reactive.Logs;
import io.github.honhimw.ms.api.reactive.ReactiveLogs;
import io.github.honhimw.ms.model.LogStreamRequest;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class LogsImpl implements Logs {

    private final ReactiveLogs _logs;

    protected LogsImpl(ReactiveLogs logs) {
        this._logs = logs;
    }

    @Override
    public void update(LogStreamRequest request) {
        _logs.update(request).block();
    }

    @Override
    public void stderr(LogStreamRequest request) {
        _logs.stderr(request).block();
    }

    @Override
    public void reset() {
        _logs.reset().block();
    }
}

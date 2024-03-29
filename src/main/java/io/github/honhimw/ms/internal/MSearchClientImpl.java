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

import io.github.honhimw.ms.MSearchConfig;
import io.github.honhimw.ms.api.*;
import io.github.honhimw.ms.api.reactive.Logs;
import io.github.honhimw.ms.api.reactive.ReactiveMSearchClient;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;
import io.github.honhimw.ms.support.ReactorUtils;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2023-07-24
 */

public class MSearchClientImpl implements MSearchClient {

    private final ReactiveMSearchClient reactiveMSearchClient;

    /**
     * Create a MSearchClient with given config
     * @param config client config
     */
    public MSearchClientImpl(MSearchConfig config) {
        this.reactiveMSearchClient = ReactiveMSearchClient.create(config);
    }

    @Override
    public Indexes indexes() {
        return new IndexesImpl(reactiveMSearchClient.indexes());
    }

    @Override
    public Tasks tasks() {
        return new TasksImpl(reactiveMSearchClient.tasks());
    }

    @Override
    public Keys keys() {
        return new KeysImpl(reactiveMSearchClient.keys());
    }

    @Override
    public List<SearchResponse<Map<String, Object>>> multiSearch(MultiSearchRequest request) {
        return ReactorUtils.blockNonNull(reactiveMSearchClient.multiSearch(request));
    }

    @Override
    public void healthy() {
        reactiveMSearchClient.healthy().block();
    }

    @Override
    public Version version() {
        return ReactorUtils.blockNonNull(reactiveMSearchClient.version());
    }

    @Override
    @Operation(method = "POST", tags = "/dumps")
    public TaskInfo dumps() {
        return ReactorUtils.blockNonNull(reactiveMSearchClient.dumps());
    }

    @Override
    @Operation(method = "POST", tags = "/snapshots")
    public TaskInfo snapshots() {
        return ReactorUtils.blockNonNull(reactiveMSearchClient.snapshots());
    }

    @Override
    public Logs logs() {
        return new LogsImpl(reactiveMSearchClient.logs());
    }

    @Override
    public ExperimentalFeaturesSettings experimentalFeatures() {
        return new ExperimentalFeaturesSettingsImpl(reactiveMSearchClient.experimentalFeatures());
    }

    @Override
    public void close() {
        reactiveMSearchClient.close();
    }
}

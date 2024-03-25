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

import io.github.honhimw.ms.MSearchConfig;
import io.github.honhimw.ms.api.reactive.*;
import io.github.honhimw.ms.http.ReactiveHttpUtils;
import io.github.honhimw.ms.http.ResponseFilter;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2023-07-24
 */

public class ReactiveMSearchClientImpl implements ReactiveMSearchClient {

    /**
     * Server full URL.
     */
    protected final String serverUrl;

    /**
     * Server API key.
     */
    protected final String apiKey;

    /**
     * JSON handler.
     */
    protected final JsonHandler jsonHandler;

    /**
     * HTTP client.
     */
    protected final ReactiveHttpUtils httpClient;

    /**
     * HTTP response filter.
     */
    protected final ResponseFilter responseFilter;

    private SimpleReactiveImpl simpleReactive;

    /**
     * Construct a new {@link ReactiveMSearchClientImpl} with the given {@link MSearchConfig}.
     * @param config the {@link MSearchConfig}
     */
    public ReactiveMSearchClientImpl(MSearchConfig config) {
        this.serverUrl = config.getServerUrl();
        this.apiKey = config.getApiKey();
        this.jsonHandler = config.getJsonHandler();
        this.httpClient = config.getHttpClient();
        this.responseFilter = config.getResponseFilter();
    }

    @Override
    public ReactiveIndexes indexes() {
        return new ReactiveIndexesImpl(this);
    }

    @Override
    public ReactiveTasks tasks() {
        return new ReactiveTasksImpl(this);
    }

    @Override
    public ReactiveKeys keys() {
        return new ReactiveKeysImpl(this);
    }

    @Override
    public Mono<List<SearchResponse<Map<String, Object>>>> multiSearch(MultiSearchRequest request) {
        return getSimpleReactive().post("/multi-search", configurer -> simpleReactive.json(configurer, request), TypeRefs.StringObjectMapSearchResponseListRef.INSTANCE);
    }

    @Override
    public Mono<Void> healthy() {
        return getSimpleReactive().get("/health", TypeRefs.StringStringMapRef.INSTANCE).handle((map, sink) -> {
                if (!"available".equals(map.get("status"))) {
                    sink.error(new IllegalStateException("server status not ['available']"));
                }
            })
            .then();
    }

    @Override
    public Mono<Version> version() {
        return getSimpleReactive().get("/version", TypeRefs.of(Version.class));
    }

    @Override
    public Mono<TaskInfo> dumps() {
        return getSimpleReactive().post("/dumps", TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> snapshots() {
        return getSimpleReactive().post("/snapshots", TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public ReactiveLogs logs() {
        return new ReactiveLogsImpl(this);
    }

    @Override
    public ReactiveExperimentalFeaturesSettings experimentalFeatures() {
        return new ReactiveExperimentalFeaturesSettingsImpl(this);
    }

    private SimpleReactiveImpl getSimpleReactive() {
        if (Objects.isNull(simpleReactive)) {
            simpleReactive = new SimpleReactiveImpl(this);
        }
        return simpleReactive;
    }

    @Override
    public void close() {
        this.httpClient.close();
    }
}

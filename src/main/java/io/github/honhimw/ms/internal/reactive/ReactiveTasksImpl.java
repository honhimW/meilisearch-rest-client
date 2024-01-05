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

import io.github.honhimw.ms.api.reactive.ReactiveTasks;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.CancelTasksRequest;
import io.github.honhimw.ms.model.GetTasksRequest;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveTasksImpl extends AbstractReactiveImpl implements ReactiveTasks {

    protected ReactiveTasksImpl(ReactiveMSearchClientImpl client) {
        super(client);
    }

    @Override
    public Mono<Page<TaskInfo>> list(GetTasksRequest request) {
        return get("/tasks", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, new TypeRef<Page<TaskInfo>>() {
        });
    }

    @Override
    public Mono<TaskInfo> delete(GetTasksRequest request) {
        return delete("/tasks", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> get(Integer uid) {
        return get(String.format("/tasks/%s", uid), new TypeRef<TaskInfo>() {
        });
    }

    @Override
    public Mono<TaskInfo> cancel(CancelTasksRequest request) {
        return post("/tasks/cancel", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, new TypeRef<TaskInfo>() {
        });
    }
}

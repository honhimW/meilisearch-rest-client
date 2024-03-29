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

import io.github.honhimw.ms.NonNullApi;
import io.github.honhimw.ms.api.reactive.ReactiveMSearchClient;
import io.github.honhimw.ms.api.reactive.ReactiveTasks;
import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.model.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hon_him
 * @since 2024-03-28
 */

public class RetryTests {

    ReactiveMSearchClient reactiveClient;
    @Test
    @SneakyThrows
    void retryNoError() {
        reactiveClient = ReactiveMSearchClient.create(builder -> builder
            .serverUrl("http://127.0.0.1:7700")
            .awaitAttempts(2)
            .awaitFixedDelay(Duration.ofMillis(5))
            .awaitExhaustedError(false)
            .jsonHandler(new JacksonJsonHandler())
        );

        AtomicInteger count = new AtomicInteger(0);

        TaskInfo unfinished = new TaskInfo();
        unfinished.setTaskUid(1);
        unfinished.setStatus(TaskStatus.ENQUEUED);

        TaskInfo finished = new TaskInfo();
        finished.setTaskUid(1);
        finished.setStatus(TaskStatus.CANCELED);

        ReactiveTasks tasks;
        tasks = new DelegateReactiveTasks((ReactiveMSearchClientImpl) reactiveClient) {
            @NonNull
            @Override
            public Mono<TaskInfo> get(@NonNull Integer uid) {
                int i = count.getAndIncrement();
                if (i < 8) {
                    return Mono.just(unfinished);
                } else {
                    return Mono.just(finished);
                }
            }
        };

        Throwable t = null;
        TaskInfo taskInfo = null;
        try {
            taskInfo = tasks.await(1).block();
        } catch (Exception e) {
            t = e;
        }
        assert Objects.isNull(t) : t.toString();
        assert Objects.nonNull(taskInfo);
        assert taskInfo.getStatus() == TaskStatus.ENQUEUED;
    }

    @Test
    @SneakyThrows
    void retryWithError() {
        reactiveClient = ReactiveMSearchClient.create(builder -> builder
            .serverUrl("http://127.0.0.1:7700")
            .awaitAttempts(2)
            .awaitFixedDelay(Duration.ofMillis(5))
            .awaitExhaustedError(true)
            .jsonHandler(new JacksonJsonHandler())
        );

        TaskInfo unfinished = new TaskInfo();
        unfinished.setTaskUid(1);
        unfinished.setStatus(TaskStatus.ENQUEUED);

        ReactiveTasks tasks;
        tasks = new DelegateReactiveTasks((ReactiveMSearchClientImpl) reactiveClient) {
            @NonNull
            @Override
            public Mono<TaskInfo> get(@NonNull Integer uid) {
                return Mono.just(unfinished);
            }
        };

        Throwable t = null;
        try {
            tasks.await(1).block();
        } catch (Exception e) {
            t = e;
        }
        assert Objects.nonNull(t);
    }

    public static class DelegateReactiveTasks extends ReactiveTasksImpl {

        protected DelegateReactiveTasks(ReactiveMSearchClientImpl client) {
            super(client);
        }
    }

}

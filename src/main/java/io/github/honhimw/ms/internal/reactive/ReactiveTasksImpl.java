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
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.model.exception.TaskStateException;
import io.github.honhimw.ms.support.TypeRefs;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetrySpec;

import java.time.Duration;
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
    public Mono<Page<TaskView>> list(GetTasksRequest request) {
        return get("/tasks", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, TypeRefs.PageTaskViewRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> delete(GetTasksRequest request) {
        return delete("/tasks", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskView> get(Integer uid) {
        return get(String.format("/tasks/%s", uid), TypeRefs.TaskViewRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> cancel(CancelTasksRequest request) {
        return post("/tasks/cancel", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskView> await(int uid) {
        return await(uid, _client.config.getAwaitAttempts(), _client.config.getAwaitFixedDelay());
    }

    @Override
    public Mono<TaskView> await(int uid, int maxAttempts, Duration fixedDelay, Duration maxDuration) {
        boolean awaitExhaustedError = _client.config.isAwaitExhaustedError();
        Mono<TaskView> mono = get(uid)
            .doOnNext(taskView -> {
                TaskStatus status = taskView.getStatus();
                if (status == TaskStatus.ENQUEUED || status == TaskStatus.PROCESSING) {
                    throw new TaskStateException(taskView, "task not completed.");
                }
            })
            .retryWhen(RetrySpec.fixedDelay(maxAttempts, fixedDelay)
                .modifyErrorFilter(throwablePredicate -> throwable -> throwable instanceof TaskStateException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    TaskStateException failure = (TaskStateException) retrySignal.failure();
                    return new TaskStateException(failure.getTaskView(), String.format("task not completed after: %d attempts with fixed delay: %s", maxAttempts, fixedDelay));
                })
            );
        if (!awaitExhaustedError) {
            mono = mono
                .take(maxDuration)
                .onErrorResume(TaskStateException.class::isInstance, throwable -> Mono.just(((TaskStateException) throwable).getTaskView()));
        } else {
            mono = mono.timeout(maxDuration);
        }
        return mono;
    }

    @Override
    public Mono<BatchPage<Batch>> batches(GetTasksRequest request) {
        return get("/batches", configurer -> {
            Map<String, String> parameters = request.toParameters();
            configurer.params(parameters);
        }, TypeRefs.BatchPageBatchViewRef.INSTANCE);
    }

    @Override
    public Mono<Batch> getBatch(Integer uid) {
        return get(String.format("/batches/%s", uid), TypeRefs.BatchRef.INSTANCE);
    }
}

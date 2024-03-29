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

import io.github.honhimw.ms.api.Tasks;
import io.github.honhimw.ms.api.reactive.ReactiveTasks;
import io.github.honhimw.ms.model.CancelTasksRequest;
import io.github.honhimw.ms.model.GetTasksRequest;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;

import java.time.Duration;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class TasksImpl implements Tasks {

    private final ReactiveTasks _reactive;

    TasksImpl(ReactiveTasks reactive) {
        _reactive = reactive;
    }

    @Override
    public Page<TaskInfo> list(GetTasksRequest request) {
        return ReactorUtils.blockNonNull(_reactive.list(request));
    }

    @Override
    public TaskInfo delete(GetTasksRequest request) {
        return ReactorUtils.blockNonNull(_reactive.delete(request));
    }

    @Override
    public TaskInfo get(Integer uid) {
        return ReactorUtils.blockNonNull(_reactive.get(uid));
    }

    @Override
    public TaskInfo cancel(CancelTasksRequest request) {
        return ReactorUtils.blockNonNull(_reactive.cancel(request));
    }

    @Override
    public TaskInfo await(int uid) {
        return ReactorUtils.blockNonNull(_reactive.await(uid));
    }

    @Override
    public TaskInfo await(int uid, int maxAttempts, Duration fixedDelay) {
        return ReactorUtils.blockNonNull(_reactive.await(uid, maxAttempts, fixedDelay));
    }

    @Override
    public TaskInfo await(TaskInfo taskInfo) {
        return ReactorUtils.blockNonNull(_reactive.await(taskInfo));
    }

    @Override
    public TaskInfo await(TaskInfo taskInfo, int maxAttempts, Duration fixedDelay) {
        return ReactorUtils.blockNonNull(_reactive.await(taskInfo, maxAttempts, fixedDelay));
    }

    @Deprecated
    @Override
    public void waitForTask(int uid) {
        _reactive.await(uid).block();
    }

    @Deprecated
    @Override
    public void waitForTask(int uid, int maxAttempts, Duration fixedDelay) {
        _reactive.await(uid, maxAttempts, fixedDelay).block();
    }
}

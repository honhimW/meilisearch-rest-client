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

package io.github.honhimw.ms.api;

import io.github.honhimw.ms.api.annotation.Operation;
import io.github.honhimw.ms.model.*;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * The tasks route gives information about the progress of the asynchronous operations.
 *
 * @author hon_him
 * @since 2024-01-02
 */

public interface Tasks {

    /**
     * Get all tasks
     *
     * @param request request
     * @return paginated result
     */
    @Operation(method = "GET", paths = "/tasks")
    Page<TaskView> list(GetTasksRequest request);

    /**
     * Get all tasks
     *
     * @param builder request builder
     * @return paginated result
     */
    @Operation(method = "GET", paths = "/tasks")
    default Page<TaskView> list(Consumer<GetTasksRequest.Builder> builder) {
        GetTasksRequest.Builder _builder = GetTasksRequest.builder();
        builder.accept(_builder);
        return list(_builder.build());
    }

    /**
     * Delete finished tasks
     *
     * @param request request
     * @return delete task
     */
    @Operation(method = "DELETE", paths = "/tasks")
    TaskInfo delete(GetTasksRequest request);

    /**
     * Delete finished tasks
     *
     * @param builder request builder
     * @return delete task
     */
    @Operation(method = "DELETE", paths = "/tasks")
    default TaskInfo delete(Consumer<GetTasksRequest.Builder> builder) {
        GetTasksRequest.Builder _builder = GetTasksRequest.builder();
        builder.accept(_builder);
        return delete(_builder.build());
    }

    /**
     * Get a single task.
     *
     * @param uid uid of the requested task
     * @return the requested task
     */
    @Operation(method = "GET", paths = "/tasks/{taskUid}")
    TaskView get(Integer uid);

    /**
     * Cancel any number of enqueued or processing tasks based on their uid, status, type, indexUid,
     * or the date at which they were enqueued, processed, or completed.
     * Task cancelation is an atomic transaction: either all tasks are successfully canceled or none are.
     *
     * @param request A valid uids, statuses, types, indexUids, or date(beforeXAt or afterXAt) parameter is required.
     * @return cancel task
     */
    @Operation(method = "POST", paths = "/tasks/cancel")
    TaskInfo cancel(CancelTasksRequest request);

    /**
     * Cancel any number of enqueued or processing tasks based on their uid, status, type, indexUid,
     * or the date at which they were enqueued, processed, or completed.
     * Task cancelation is an atomic transaction: either all tasks are successfully canceled or none are.
     *
     * @param builder A valid uids, statuses, types, indexUids, or date(beforeXAt or afterXAt) parameter is required.
     * @return cancel task
     */
    @Operation(method = "POST", paths = "/tasks/cancel")
    default TaskInfo cancel(Consumer<CancelTasksRequest.Builder> builder) {
        CancelTasksRequest.Builder _builder = CancelTasksRequest.builder();
        builder.accept(_builder);
        return cancel(_builder.build());
    }

    /**
     * Wait for task finish
     *
     * @param uid task uid
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(int uid);

    /**
     * Wait for task finish
     *
     * @param uid         task uid
     * @param maxAttempts max attempts
     * @param fixedDelay  fixed delay
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(int uid, int maxAttempts, Duration fixedDelay);

    /**
     * Wait for task finish
     *
     * @param uid         task uid
     * @param maxAttempts max attempts
     * @param fixedDelay  fixed delay
     * @param maxDuration max duration
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(int uid, int maxAttempts, Duration fixedDelay, Duration maxDuration);

    /**
     * Wait for task finish
     *
     * @param taskInfo task info
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(TaskInfo taskInfo);

    /**
     * Wait for task finish
     *
     * @param taskInfo    task info
     * @param maxAttempts max attempts
     * @param fixedDelay  fixed delay
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(TaskInfo taskInfo, int maxAttempts, Duration fixedDelay);

    /**
     * Wait for task finish
     *
     * @param taskInfo    task info
     * @param maxAttempts max attempts
     * @param fixedDelay  fixed delay
     * @param maxDuration max duration
     * @return task info if MSearchConfig#isAwaitExhaustedError() is false
     */
    TaskView await(TaskInfo taskInfo, int maxAttempts, Duration fixedDelay, Duration maxDuration);

    /**
     * List all batches, regardless of index. The batch objects are contained in the results array.
     * <p>
     * Batches are always returned in descending order of uid. This means that by default, the most recently created batch objects appear first.
     * <p>
     * Batch results are paginated and can be filtered with query parameters.
     *
     * @param request GetTasksRequest
     * @return paginated result
     * @since v1.12
     */
    @Operation(method = "GET", paths = "/batches")
    BatchPage<Batch> batches(GetTasksRequest request);

    /**
     * Get a single batch.
     *
     * @param uid 	uid of the requested batch
     * @return the requested batch
     */
    @Operation(method = "GET", paths = "/batches/{batch_uid}")
    Batch getBatch(Integer uid);

}

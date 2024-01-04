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

import io.github.honhimw.ms.model.CancelTasksRequest;
import io.github.honhimw.ms.model.GetTasksRequest;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;

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
     * @return paginated result
     */
    @Operation(method = "GET", tags = "/tasks")
    Page<TaskInfo> list(GetTasksRequest request);

    /**
     * Delete finished tasks
     *
     * @return paginated result
     */
    @Operation(method = "DELETE", tags = "/tasks")
    Page<TaskInfo> delete(GetTasksRequest request);

    /**
     * Get a single task.
     *
     * @param uid uid of the requested task
     */
    @Operation(method = "GET", tags = "/tasks/{taskUid}")
    TaskInfo get(Integer uid);

    /**
     * Cancel any number of enqueued or processing tasks based on their uid, status, type, indexUid,
     * or the date at which they were enqueued, processed, or completed.
     * Task cancelation is an atomic transaction: either all tasks are successfully canceled or none are.
     *
     * @param request A valid uids, statuses, types, indexUids, or date(beforeXAt or afterXAt) parameter is required.
     */
    @Operation(method = "POST", tags = "/tasks/cancel")
    TaskInfo cancel(CancelTasksRequest request);





}

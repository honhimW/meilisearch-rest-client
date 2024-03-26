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

package io.github.honhimw.ms.model;

import io.github.honhimw.ms.json.EnumValue;

/**
 * <a style="font-weight:bold;font-size:large" href="https://www.meilisearch.com/docs/learn/async/asynchronous_operations#task-status">Task Status</a>
 * <p>
 * Tasks always contain a field indicating the task's current status. This field has one of the following possible values:
 * <ul>
 *     <li>enqueued: the task has been received and will be processed soon</li>
 *     <li>processing: the task is being processed</li>
 *     <li>succeeded: the task has been successfully processed</li>
 *     <li>failed: a failure occurred when processing the task. No changes were made to the database</li>
 *     <li>canceled: the task was canceled</li>
 * </ul>
 * succeeded, failed, and canceled tasks are finished tasks.
 * Meilisearch keeps them in the task database but has finished processing these tasks.
 * It is possible to configure a webhook to notify external services when a task is finished.
 * <p>
 * enqueued and processing tasks are unfinished tasks. Meilisearch is either processing them or will do so in the future.
 *
 * @author hon_him
 * @since 2024-01-02
 */

public enum TaskStatus implements EnumValue<TaskStatus> {

    /**
     * the task has been received and will be processed soon
     */
    ENQUEUED("enqueued"),
    /**
     * the task is being processed
     */
    PROCESSING("processing"),
    /**
     * the task has been successfully processed
     */
    SUCCEEDED("succeeded"),
    /**
     * a failure occurred when processing the task. No changes were made to the database
     */
    FAILED("failed"),
    /**
     * the task was canceled
     */
    CANCELED("canceled");

    /**
     * Enum value
     */
    public final String taskStatus;

    TaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String value() {
        return this.taskStatus;
    }

}

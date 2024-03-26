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
 * <a style="font-weight:bold;font-size:large" href="https://www.meilisearch.com/docs/reference/api/tasks#type">TaskType</a>
 * <p>
 * Type of operation performed by the task.
 *
 * @author hon_him
 * @since 2024-01-02
 */

public enum TaskType implements EnumValue<TaskType> {
    /**
     * index creation
     */
    INDEX_CREATION("indexCreation"),
    /**
     * index update
     */
    INDEX_UPDATE("indexUpdate"),
    /**
     * index deletion
     */
    INDEX_DELETION("indexDeletion"),
    /**
     * index swap
     */
    INDEX_SWAP("indexSwap"),
    /**
     * document addition or update
     */
    DOCUMENT_ADDITION_OR_UPDATE("documentAdditionOrUpdate"),
    /**
     * document deletion
     */
    DOCUMENT_DELETION("documentDeletion"),
    /**
     * settings update
     */
    SETTINGS_UPDATE("settingsUpdate"),
    /**
     * dump creation
     */
    DUMP_CREATION("dumpCreation"),
    /**
     * task cancellation
     */
    TASK_CANCELATION("taskCancelation"),
    /**
     * task deletion
     */
    TASK_DELETION("taskDeletion"),
    /**
     * snapshot creation
     */
    SNAPSHOT_CREATION("snapshotCreation"),
    ;
    /**
     * Enum value
     */
    public final String tasksType;

    TaskType(String tasksType) {
        this.tasksType = tasksType;
    }

    @Override
    public String value() {
        return this.tasksType;
    }

}

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
 * @author hon_him
 * @since 2024-01-02
 */

public enum TaskType implements EnumValue<TaskType> {
    INDEX_CREATION("indexCreation"),
    INDEX_UPDATE("indexUpdate"),
    INDEX_DELETION("indexDeletion"),
    INDEX_SWAP("indexSwap"),
    DOCUMENT_ADDITION_OR_UPDATE("documentAdditionOrUpdate"),
    DOCUMENT_DELETION("documentDeletion"),
    SETTINGS_UPDATE("settingsUpdate"),
    DUMP_CREATION("dumpCreation"),
    TASK_CANCELATION("taskCancelation"),
    TASK_DELETION("taskDeletion"),
    SNAPSHOT_CREATION("snapshotCreation"),
    ;
    public final String tasksType;

    TaskType(String tasksType) {
        this.tasksType = tasksType;
    }

    @Override
    public String value() {
        return this.tasksType;
    }

}

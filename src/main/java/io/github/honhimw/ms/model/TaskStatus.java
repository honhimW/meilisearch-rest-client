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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public enum TaskStatus {
    ENQUEUED("enqueued"),
    PROCESSING("processing"),
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    CANCELED("canceled");

    public final String taskStatus;

    TaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.taskStatus;
    }

}

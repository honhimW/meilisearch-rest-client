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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfo implements Serializable {

    @Schema(description = "Unique sequential identifier")
    private Integer taskUid;

    @Schema(description = "Unique index identifier (always null for global tasks)")
    private String indexUid;

    @Schema(description = "Status of the task. Value is enqueued")
    private TaskStatus status;

    @Schema(description = "Type of task")
    private TaskType type;

    @Schema(description = "Represents the date and time in the RFC 3339 format when the task has been enqueued")
    private LocalDateTime enqueuedAt;

}

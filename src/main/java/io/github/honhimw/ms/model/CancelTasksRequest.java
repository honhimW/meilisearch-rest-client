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
import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CancelTasksRequest implements Serializable {
    
    @Schema(description = "Cancel tasks based on uid. Separate multiple uids with a comma (,). Use uids=* for all uids")
    private List<Integer> uids;
    
    @Schema(description = "Cancel tasks based on status. Separate multiple statuses with a comma (,). Use statuses=* for all statuses")
    private List<TaskStatus> statuses;
    
    @Schema(description = "Cancel tasks based on type. Separate multiple types with a comma (,). Use types=* for all types")
    private List<TaskType> types;
    
    @Schema(description = "Cancel tasks based on indexUid. Separate multiple uids with a comma (,). Use indexUids=* for all indexUids. Case-sensitive")
    private List<String> indexUids;
    
    @Schema(description = "Cancel tasks before a specified enqueuedAt date. Use beforeEnqueuedAt=* to cancel all tasks")
    private LocalDateTime beforeEnqueuedAt;

    @Schema(description = "Cancel tasks before a specified startedAt date. Use beforeStartedAt=* to cancel all tasks")
    private LocalDateTime beforeStartedAt;

    @Schema(description = "Cancel tasks after a specified enqueuedAt date. Use afterEnqueuedAt=* to cancel all tasks")
    private LocalDateTime afterEnqueuedAt;

    @Schema(description = "Cancel tasks after a specified startedAt date. Use afterStartedAt=* to cancel all tasks")
    private LocalDateTime afterStartedAt;

}

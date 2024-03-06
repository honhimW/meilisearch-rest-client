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

import io.github.honhimw.ms.support.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class CancelTasksRequest implements Serializable {

    @Schema(description = "Cancel tasks based on uid. Separate multiple uids with a comma (,). Use uids=* for all uids")
    private List<String> uids;

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

    public Map<String, String> toParameters() {
        Map<String, String> parameters = new HashMap<>();
        Optional.ofNullable(uids).filter(strings -> !strings.isEmpty()).map(strings -> String.join(",", strings)).ifPresent(uids -> parameters.put("uids", uids));
        Optional.ofNullable(statuses).filter(taskStatuses -> !taskStatuses.isEmpty()).map(taskStatuses -> taskStatuses.stream().map(TaskStatus::toString).collect(Collectors.joining(","))).ifPresent(statuses -> parameters.put("statuses", statuses));
        Optional.ofNullable(types).filter(taskTypes -> !taskTypes.isEmpty()).map(taskTypes -> taskTypes.stream().map(TaskType::toString).collect(Collectors.joining(","))).ifPresent(types -> parameters.put("types", types));
        Optional.ofNullable(indexUids).filter(strings -> !strings.isEmpty()).map(strings -> String.join(",", strings)).ifPresent(indexUids -> parameters.put("indexUids", indexUids));
        Optional.ofNullable(beforeEnqueuedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("beforeEnqueuedAt", at));
        Optional.ofNullable(beforeStartedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("beforeStartedAt", at));
        Optional.ofNullable(afterEnqueuedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("afterEnqueuedAt", at));
        Optional.ofNullable(afterStartedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("afterStartedAt", at));
        return parameters;
    }

}

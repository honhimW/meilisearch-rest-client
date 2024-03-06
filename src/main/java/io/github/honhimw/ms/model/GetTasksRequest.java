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
 * @since 2024-01-04
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class GetTasksRequest implements Serializable {

    @Schema(description = "Number of tasks to return", defaultValue = "20")
    private Integer limit;

    @Schema(description = "uid of the first task returned", defaultValue = "uid of the last created task")
    private String from;

    @Schema(description = "Filter tasks by their uid. Separate multiple task uids with a comma (,)", defaultValue = "*")
    private List<String> uids;

    @Schema(description = "Filter tasks by their status. Separate multiple task statuses with a comma (,)", defaultValue = "*")
    private List<TaskStatus> statuses;

    @Schema(description = "Filter tasks by their type. Separate multiple task types with a comma (,)", defaultValue = "*")
    private List<TaskType> types;

    @Schema(description = "Filter tasks by their indexUid. Separate multiple task indexUids with a comma (,). Case-sensitive", defaultValue = "*")
    private List<String> indexUids;

    @Schema(description = "Filter tasks by their canceledBy field. Separate multiple task uids with a comma (,)")
    private List<String> canceledBy;

    @Schema(description = "Filter tasks by their enqueuedAt field", defaultValue = "*")
    private LocalDateTime beforeEnqueuedAt;

    @Schema(description = "Filter tasks by their startedAt field", defaultValue = "*")
    private LocalDateTime beforeStartedAt;

    @Schema(description = "Filter tasks by their finishedAt field", defaultValue = "*")
    private LocalDateTime beforeFinishedAt;

    @Schema(description = "Filter tasks by their enqueuedAt field", defaultValue = "*")
    private LocalDateTime afterEnqueuedAt;

    @Schema(description = "Filter tasks by their startedAt field", defaultValue = "*")
    private LocalDateTime afterStartedAt;

    @Schema(description = "Filter tasks by their finishedAt field", defaultValue = "*")
    private LocalDateTime afterFinishedAt;

    public Map<String, String> toParameters() {
        Map<String, String> parameters = new HashMap<>();
        Optional.ofNullable(limit).ifPresent(limit -> parameters.put("limit", String.valueOf(limit)));
        Optional.ofNullable(from).ifPresent(from -> parameters.put("from", from));
        Optional.ofNullable(uids).filter(strings -> !strings.isEmpty()).map(strings -> String.join(",", strings)).ifPresent(uids -> parameters.put("uids", uids));
        Optional.ofNullable(statuses).filter(taskStatuses -> !taskStatuses.isEmpty()).map(taskStatuses -> taskStatuses.stream().map(TaskStatus::toString).collect(Collectors.joining(","))).ifPresent(statuses -> parameters.put("statuses", statuses));
        Optional.ofNullable(types).filter(taskTypes -> !taskTypes.isEmpty()).map(taskTypes -> taskTypes.stream().map(TaskType::toString).collect(Collectors.joining(","))).ifPresent(types -> parameters.put("types", types));
        Optional.ofNullable(indexUids).filter(strings -> !strings.isEmpty()).map(strings -> String.join(",", strings)).ifPresent(indexUids -> parameters.put("indexUids", indexUids));
        Optional.ofNullable(canceledBy).filter(strings -> !strings.isEmpty()).map(strings -> String.join(",", strings)).ifPresent(canceledBy -> parameters.put("canceledBy", canceledBy));
        Optional.ofNullable(beforeEnqueuedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("beforeEnqueuedAt", at));
        Optional.ofNullable(beforeStartedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("beforeStartedAt", at));
        Optional.ofNullable(beforeFinishedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("beforeFinishedAt", at));
        Optional.ofNullable(afterEnqueuedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("afterEnqueuedAt", at));
        Optional.ofNullable(afterStartedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("afterStartedAt", at));
        Optional.ofNullable(afterFinishedAt).map(DateTimeUtils::format).ifPresent(at -> parameters.put("afterFinishedAt", at));
        return parameters;
    }

}

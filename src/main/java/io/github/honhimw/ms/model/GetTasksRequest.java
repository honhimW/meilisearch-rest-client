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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    private GetTasksRequest(Builder builder) {
        setLimit(builder.limit);
        setFrom(builder.from);
        setUids(builder.uids);
        setStatuses(builder.statuses);
        setTypes(builder.types);
        setIndexUids(builder.indexUids);
        setCanceledBy(builder.canceledBy);
        setBeforeEnqueuedAt(builder.beforeEnqueuedAt);
        setBeforeStartedAt(builder.beforeStartedAt);
        setBeforeFinishedAt(builder.beforeFinishedAt);
        setAfterEnqueuedAt(builder.afterEnqueuedAt);
        setAfterStartedAt(builder.afterStartedAt);
        setAfterFinishedAt(builder.afterFinishedAt);
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public static final class Builder {
        private Integer limit;
        private String from;
        private List<String> uids;
        private List<TaskStatus> statuses;
        private List<TaskType> types;
        private List<String> indexUids;
        private List<String> canceledBy;
        private LocalDateTime beforeEnqueuedAt;
        private LocalDateTime beforeStartedAt;
        private LocalDateTime beforeFinishedAt;
        private LocalDateTime afterEnqueuedAt;
        private LocalDateTime afterStartedAt;
        private LocalDateTime afterFinishedAt;

        private Builder() {
        }

        public Builder limit(Integer val) {
            limit = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder uids(List<String> val) {
            uids = val;
            return this;
        }

        public Builder statuses(List<TaskStatus> val) {
            statuses = val;
            return this;
        }

        public Builder types(List<TaskType> val) {
            types = val;
            return this;
        }

        public Builder indexUids(List<String> val) {
            indexUids = val;
            return this;
        }

        public Builder canceledBy(List<String> val) {
            canceledBy = val;
            return this;
        }

        public Builder beforeEnqueuedAt(LocalDateTime val) {
            beforeEnqueuedAt = val;
            return this;
        }

        public Builder beforeStartedAt(LocalDateTime val) {
            beforeStartedAt = val;
            return this;
        }

        public Builder beforeFinishedAt(LocalDateTime val) {
            beforeFinishedAt = val;
            return this;
        }

        public Builder afterEnqueuedAt(LocalDateTime val) {
            afterEnqueuedAt = val;
            return this;
        }

        public Builder afterStartedAt(LocalDateTime val) {
            afterStartedAt = val;
            return this;
        }

        public Builder afterFinishedAt(LocalDateTime val) {
            afterFinishedAt = val;
            return this;
        }

        public GetTasksRequest build() {
            return new GetTasksRequest(this);
        }
    }
}

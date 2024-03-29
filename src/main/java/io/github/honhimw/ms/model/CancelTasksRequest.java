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
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CancelTasksRequest implements Serializable {

    /**
     * Cancel tasks based on uid. Separate multiple uids with a comma (,). Use uids=* for all uids
     */
    @Schema(description = "Cancel tasks based on uid. Separate multiple uids with a comma (,). Use uids=* for all uids")
    private List<String> uids;

    /**
     * Cancel tasks based on status. Separate multiple statuses with a comma (,). Use statuses=* for all statuses
     */
    @Schema(description = "Cancel tasks based on status. Separate multiple statuses with a comma (,). Use statuses=* for all statuses")
    private List<TaskStatus> statuses;

    /**
     * Cancel tasks based on type. Separate multiple types with a comma (,). Use types=* for all types
     */
    @Schema(description = "Cancel tasks based on type. Separate multiple types with a comma (,). Use types=* for all types")
    private List<TaskType> types;

    /**
     * Cancel tasks based on indexUid. Separate multiple uids with a comma (,). Use indexUids=* for all indexUids. Case-sensitive
     */
    @Schema(description = "Cancel tasks based on indexUid. Separate multiple uids with a comma (,). Use indexUids=* for all indexUids. Case-sensitive")
    private List<String> indexUids;

    /**
     * Cancel tasks before a specified enqueuedAt date. Use beforeEnqueuedAt=* to cancel all tasks
     */
    @Schema(description = "Cancel tasks before a specified enqueuedAt date. Use beforeEnqueuedAt=* to cancel all tasks")
    private LocalDateTime beforeEnqueuedAt;

    /**
     * Cancel tasks before a specified startedAt date. Use beforeStartedAt=* to cancel all tasks
     */
    @Schema(description = "Cancel tasks before a specified startedAt date. Use beforeStartedAt=* to cancel all tasks")
    private LocalDateTime beforeStartedAt;

    /**
     * Cancel tasks after a specified enqueuedAt date. Use afterEnqueuedAt=* to cancel all tasks
     */
    @Schema(description = "Cancel tasks after a specified enqueuedAt date. Use afterEnqueuedAt=* to cancel all tasks")
    private LocalDateTime afterEnqueuedAt;

    /**
     * Cancel tasks after a specified startedAt date. Use afterStartedAt=* to cancel all tasks
     */
    @Schema(description = "Cancel tasks after a specified startedAt date. Use afterStartedAt=* to cancel all tasks")
    private LocalDateTime afterStartedAt;

    private CancelTasksRequest(Builder builder) {
        setUids(builder.uids);
        setStatuses(builder.statuses);
        setTypes(builder.types);
        setIndexUids(builder.indexUids);
        setBeforeEnqueuedAt(builder.beforeEnqueuedAt);
        setBeforeStartedAt(builder.beforeStartedAt);
        setAfterEnqueuedAt(builder.afterEnqueuedAt);
        setAfterStartedAt(builder.afterStartedAt);
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return  a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Converts the current parameter to HTTP query parameters.
     *
     * @return query parameters map
     */
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

    /**
     * {@code CancelTasksRequest} builder static inner class.
     */
    public static final class Builder {
        private List<String> uids;
        private List<TaskStatus> statuses;
        private List<TaskType> types;
        private List<String> indexUids;
        private LocalDateTime beforeEnqueuedAt;
        private LocalDateTime beforeStartedAt;
        private LocalDateTime afterEnqueuedAt;
        private LocalDateTime afterStartedAt;

        private Builder() {
        }

        /**
         * Sets the {@code uids} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code uids} to set
         * @return a reference to this Builder
         */
        public Builder uids(List<String> val) {
            uids = val;
            return this;
        }

        /**
         * Sets the {@code statuses} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code statuses} to set
         * @return a reference to this Builder
         */
        public Builder statuses(List<TaskStatus> val) {
            statuses = val;
            return this;
        }

        /**
         * Sets the {@code types} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code types} to set
         * @return a reference to this Builder
         */
        public Builder types(List<TaskType> val) {
            types = val;
            return this;
        }

        /**
         * Sets the {@code indexUids} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code indexUids} to set
         * @return a reference to this Builder
         */
        public Builder indexUids(List<String> val) {
            indexUids = val;
            return this;
        }

        /**
         * Sets the {@code beforeEnqueuedAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code beforeEnqueuedAt} to set
         * @return a reference to this Builder
         */
        public Builder beforeEnqueuedAt(LocalDateTime val) {
            beforeEnqueuedAt = val;
            return this;
        }

        /**
         * Sets the {@code beforeStartedAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code beforeStartedAt} to set
         * @return a reference to this Builder
         */
        public Builder beforeStartedAt(LocalDateTime val) {
            beforeStartedAt = val;
            return this;
        }

        /**
         * Sets the {@code afterEnqueuedAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code afterEnqueuedAt} to set
         * @return a reference to this Builder
         */
        public Builder afterEnqueuedAt(LocalDateTime val) {
            afterEnqueuedAt = val;
            return this;
        }

        /**
         * Sets the {@code afterStartedAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code afterStartedAt} to set
         * @return a reference to this Builder
         */
        public Builder afterStartedAt(LocalDateTime val) {
            afterStartedAt = val;
            return this;
        }

        /**
         * Returns a {@code CancelTasksRequest} built from the parameters previously set.
         *
         * @return a {@code CancelTasksRequest} built with parameters of this {@code CancelTasksRequest.Builder}
         */
        public CancelTasksRequest build() {
            return new CancelTasksRequest(this);
        }
    }
}

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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.meilisearch.com/docs/reference/api/tasks#tasks">Tasks</a>
 *
 * @author hon_him
 * @since 2024-04-18
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TaskView implements Serializable {

    /**
     * Unique sequential identifier of the task.
     * The task uid is incremented globally.
     */
    @Schema(description = "Unique sequential identifier")
    private Integer uid;

    /**
     * Unique identifier of the targeted index.
     * This value is always null for global tasks.
     */
    @Schema(description = "Unique index identifier (always null for global tasks)")
    private String indexUid;

    /**
     * Status of the task.
     */
    @Schema(description = "Status of the task. Value is enqueued")
    private TaskStatus status;

    /**
     * Type of operation performed by the task.
     */
    @Schema(description = "Type of task")
    private TaskType type;

    /**
     * If the task was canceled, canceledBy contains the uid of a taskCancelation task. If the task was not canceled, canceledBy is always null
     */
    @Schema(description = "If the task was canceled, canceledBy contains the uid of a taskCancelation task. If the task was not canceled, canceledBy is always null")
    private Integer canceledBy;

    /**
     * Detailed information on the task payload. This object's contents depend on the task's type
     */
    @Schema(description = "Detailed information on the task payload. This object's contents depend on the task's type")
    private Details details;

    /**
     * If the task has the failed status, then this object contains the error definition. Otherwise, set to null
     */
    @Schema(description = "If the task has the failed status, then this object contains the error definition. Otherwise, set to null")
    private Error error;

    /**
     * The total elapsed time the task spent in the processing state, in ISO 8601 format
     */
    @Schema(description = "The total elapsed time the task spent in the processing state, in ISO 8601 format")
    private Duration duration;

    /**
     * The date and time when the task was first enqueued, in RFC 3339 format
     */
    @Schema(description = "The date and time when the task was first enqueued, in RFC 3339 format")
    private LocalDateTime enqueuedAt;

    /**
     * The date and time when the task began processing, in RFC 3339 format
     */
    @Schema(description = "The date and time when the task began processing, in RFC 3339 format")
    private LocalDateTime startedAt;

    /**
     * The date and time when the task finished processing, whether failed, succeeded, or canceled, in RFC 3339 format
     */
    @Schema(description = "The date and time when the task finished processing, whether failed, succeeded, or canceled, in RFC 3339 format")
    private LocalDateTime finishedAt;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error implements Serializable {

        /**
         * A human-readable description of the error
         */
        @Schema(description = "A human-readable description of the error")
        private String message;

        /**
         * The <a href="https://www.meilisearch.com/docs/reference/errors/error_codes">error code</a>
         */
        @Schema(description = "The error code")
        private String code;

        /**
         * The <a href="https://www.meilisearch.com/docs/reference/errors/overview#errors">error type</a>
         */
        @Schema(description = "The error type")
        private String type;

        /**
         * A link to the relevant section of the documentation
         */
        @Schema(description = "A link to the relevant section of the documentation")
        private String link;

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details implements Serializable {

        /**
         * Number of documents received
         */
        @Schema(description = "Number of documents received")
        private Integer receivedDocuments;

        /**
         * Number of documents indexed. null while the task status is enqueued or processing
         */
        @Schema(description = "Number of documents indexed. null while the task status is enqueued or processing")
        private Integer indexedDocuments;

        /**
         * Number of documents queued for deletion
         */
        @Schema(description = "Number of documents queued for deletion")
        private Integer providedIds;

        /**
         * The filter used to delete documents. null if it was not specified
         */
        @Schema(description = "The filter used to delete documents. null if it was not specified")
        private String originalFilter;

        /**
         * Number of documents deleted. null while the task status is enqueued or processing
         */
        @Schema(description = "Number of documents deleted. null while the task status is enqueued or processing")
        private Integer deletedDocuments;

        /**
         * Value of the primaryKey field supplied during index creation. null if it was not specified
         */
        @Schema(description = "Value of the primaryKey field supplied during index creation. null if it was not specified")
        private String primaryKey;

        /**
         * Object containing the payload for the indexSwap task
         */
        @Schema(description = "Object containing the payload for the indexSwap task")
        private List<Swaps> swaps;

        /**
         * List of ranking rules
         */
        @Schema(description = "List of ranking rules")
        private List<String> rankingRules;

        /**
         * List of filterable attributes
         */
        @Schema(description = "List of filterable attributes")
        private List<String> filterableAttributes;

        /**
         * The distinct attribute
         */
        @Schema(description = "The distinct attribute")
        private String distinctAttribute;

        /**
         * List of searchable attributes
         */
        @Schema(description = "List of searchable attributes")
        private List<String> searchableAttributes;

        /**
         * List of displayed attributes
         */
        @Schema(description = "List of displayed attributes")
        private List<String> displayedAttributes;

        /**
         * List of sortable attributes
         */
        @Schema(description = "List of sortable attributes")
        private List<String> sortableAttributes;

        /**
         * List of stop words
         */
        @Schema(description = "List of stop words")
        private List<String> stopWords;

        /**
         * List of synonyms
         */
        @Schema(description = "List of synonyms")
        private Map<String, List<String>> synonyms;

        /**
         * The typoTolerance object
         */
        @Schema(description = "The typoTolerance object")
        private TypoTolerance typoTolerance;

        /**
         * The pagination object
         */
        @Schema(description = "The pagination object")
        private Pagination pagination;

        /**
         * The faceting object
         */
        @Schema(description = "The faceting object")
        private Faceting faceting;

        /**
         * The generated uid of the dump. This is also the name of the generated dump file. null when the task status is enqueued, processing, canceled, or failed
         */
        @Schema(description = "The generated uid of the dump. This is also the name of the generated dump file. null when the task status is enqueued, processing, canceled, or failed")
        private String dumpUid;

        /**
         * The number of matched tasks. If the API key used for the request doesn’t have access to an index, tasks relating to that index will not be included in matchedTasks
         */
        @Schema(description = "The number of matched tasks. If the API key used for the request doesn’t have access to an index, tasks relating to that index will not be included in matchedTasks")
        private Integer matchedTasks;

        /**
         * The number of tasks successfully canceled. If the task cancelation fails, this will be 0. null when the task status is enqueued or processing
         */
        @Schema(description = "The number of tasks successfully canceled. If the task cancelation fails, this will be 0. null when the task status is enqueued or processing")
        private Integer canceledTasks;

        /**
         * The number of tasks successfully deleted. If the task deletion fails, this will be 0. null when the task status is enqueued or processing
         */
        @Schema(description = "The number of tasks successfully deleted. If the task deletion fails, this will be 0. null when the task status is enqueued or processing")
        private Integer deletedTasks;

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Swaps implements Serializable {

        /**
         * swaps indexes
         */
        @Schema(description = "swaps indexes")
        private List<String> indexes;
    }

}

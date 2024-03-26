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

/**
 * @author hon_him
 * @since 2024-01-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentalFeatures implements Serializable {

    /**
     * true if feature is active, false otherwise
     */
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean metrics;

    /**
     * <a href="https://github.com/meilisearch/documentation/pull/2647">documentation ISSUE #2647</a>
     */
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean vectorStore;

    /**
     * true if feature is active, false otherwise
     */
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean exportPuffinReports;

    /**
     * true if feature is active, false otherwise
     */
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean logsRoute;

    private ExperimentalFeatures(Builder builder) {
        setMetrics(builder.metrics);
        setVectorStore(builder.vectorStore);
        setExportPuffinReports(builder.exportPuffinReports);
        setLogsRoute(builder.logsRoute);
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
     * {@code ExperimentalFeatures} builder static inner class.
     */
    public static final class Builder {
        private Boolean metrics;
        private Boolean vectorStore;
        private Boolean exportPuffinReports;
        private Boolean logsRoute;

        private Builder() {
        }

        /**
         * Sets the {@code metrics} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code metrics} to set
         * @return a reference to this Builder
         */
        public Builder metrics(Boolean val) {
            metrics = val;
            return this;
        }

        /**
         * Sets the {@code vectorStore} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code vectorStore} to set
         * @return a reference to this Builder
         */
        public Builder vectorStore(Boolean val) {
            vectorStore = val;
            return this;
        }

        /**
         * Sets the {@code exportPuffinReports} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code exportPuffinReports} to set
         * @return a reference to this Builder
         */
        public Builder exportPuffinReports(Boolean val) {
            exportPuffinReports = val;
            return this;
        }

        /**
         * Sets the {@code logsRoute} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code logsRoute} to set
         * @return a reference to this Builder
         */
        public Builder logsRoute(Boolean val) {
            logsRoute = val;
            return this;
        }

        /**
         * Returns a {@code ExperimentalFeatures} built from the parameters previously set.
         *
         * @return a {@code ExperimentalFeatures} built with parameters of this {@code ExperimentalFeatures.Builder}
         */
        public ExperimentalFeatures build() {
            return new ExperimentalFeatures(this);
        }
    }

    /*
     * Stabilize since 1.7
     * @Schema(description = "true if feature is active, false otherwise")
     * private Boolean scoreDetails;
     */

}

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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hon_him
 * @since 2024-02-22
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class LogStreamRequest implements Serializable {

    @Schema(description = "to select what logs you’re interested into. It takes the form of code_part=log_level.", example = "index_scheduler=info")
    private String target;

    @Schema(description = "to select in what format of log you want")
    private Mode mode;

    private LogStreamRequest(Builder builder) {
        setTarget(builder.target);
        setMode(builder.mode);
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
     * Logging Mode.
     */
    public enum Mode implements EnumValue<Mode> {
        /**
         * human
         */
        HUMAN("human"),
        /**
         * json
         */
        JSON("json"),
        /**
         * profile
         */
        PROFILE("profile"),
        ;

        private final String value;

        Mode(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }

    /**
     * {@code LogStreamRequest} builder static inner class.
     */
    public static final class Builder {
        private String target;
        private Mode mode;

        private Builder() {
        }

        /**
         * Sets the {@code target} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code target} to set
         * @return a reference to this Builder
         */
        public Builder target(String val) {
            target = val;
            return this;
        }

        /**
         * Sets the {@code mode} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code mode} to set
         * @return a reference to this Builder
         */
        public Builder mode(Mode val) {
            mode = val;
            return this;
        }

        /**
         * Returns a {@code LogStreamRequest} built from the parameters previously set.
         *
         * @return a {@code LogStreamRequest} built with parameters of this {@code LogStreamRequest.Builder}
         */
        public LogStreamRequest build() {
            return new LogStreamRequest(this);
        }
    }
}

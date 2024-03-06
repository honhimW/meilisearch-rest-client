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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String target;
        private Mode mode;

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder error(String target) {
            this.target = format(target, "error");
            return this;
        }

        public Builder warn(String target) {
            this.target = format(target, "warn");
            return this;
        }

        public Builder info(String target) {
            this.target = format(target, "info");
            return this;
        }

        public Builder debug(String target) {
            this.target = format(target, "debug");
            return this;
        }

        public Builder trace(String target) {
            this.target = format(target, "trace");
            return this;
        }

        private String format(String name, String level) {
            return String.format("%s=%s", name, level);
        }

        public LogStreamRequest build() {
            return new LogStreamRequest(target, mode);
        }
    }

    public enum Mode implements EnumValue<Mode> {
        HUMAN("human"),
        JSON("json"),
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

}

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
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKeyRequest implements Serializable {

    /**
     * A human-readable name for the key
     */
    @Schema(description = "A human-readable name for the key")
    private String name;

    /**
     * An optional description for the key
     */
    @Schema(description = "An optional description for the key")
    private String description;

    private UpdateKeyRequest(Builder builder) {
        setName(builder.name);
        setDescription(builder.description);
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
     * {@code UpdateKeyRequest} builder static inner class.
     */
    public static final class Builder {
        private String name;
        private String description;

        private Builder() {
        }

        /**
         * Sets the {@code name} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String val) {
            name = val;
            return this;
        }

        /**
         * Sets the {@code description} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code description} to set
         * @return a reference to this Builder
         */
        public Builder description(String val) {
            description = val;
            return this;
        }

        /**
         * Returns a {@code UpdateKeyRequest} built from the parameters previously set.
         *
         * @return a {@code UpdateKeyRequest} built with parameters of this {@code UpdateKeyRequest.Builder}
         */
        public UpdateKeyRequest build() {
            return new UpdateKeyRequest(this);
        }
    }
}

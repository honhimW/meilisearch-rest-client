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
import java.util.List;

/**
 * <p style="font-weight:bold;font-size:large">NOTE:</p>
 * <ul>
 *     <li>if an attribute matches several rules, only the first rule in the list will be applied</li>
 *     <li>if the locales list is empty, then Meilisearch is allowed to auto-detect any language in the matching attributes</li>
 *     <li>These rules are applied to the searchableAttributes, the filterableAttributes, and the sortableAttributes.</li>
 * </ul>
 * <pre>{@code
 * {
 *     "locales": [],
 *     "attributePatterns": ["*"]
 * }
 * }
 * </pre>
 * means the is the default rule.
 * <p style="font-weight:bold;font-size:large">Default: null</p>
 *
 * @author hon_him
 * @since v1.10
 * @since 2024-07-31
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedAttribute implements Serializable {

    /**
     * a list of language codes to assign to a pattern
     */
    @Schema(description = "a list of language codes to assign to a pattern")
    private List<String> locales;

    /**
     * a pattern that can start or end with a * to match one or several attributes.
     */
    @Schema(description = "a pattern that can start or end with a * to match one or several attributes.")
    private List<String> attributePatterns;

    private LocalizedAttribute(Builder builder) {
        setLocales(builder.locales);
        setAttributePatterns(builder.attributePatterns);
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code LocalizedAttribute} builder static inner class.
     */
    public static final class Builder {
        private List<String> locales;
        private List<String> attributePatterns;

        private Builder() {
        }

        /**
         * Sets the {@code locales} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code locales} to set
         * @return a reference to this Builder
         */
        public Builder locales(List<String> val) {
            locales = val;
            return this;
        }

        /**
         * Sets the {@code attributePatterns} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code attributePatterns} to set
         * @return a reference to this Builder
         */
        public Builder attributePatterns(List<String> val) {
            attributePatterns = val;
            return this;
        }

        /**
         * Returns a {@code LocalizedAttribute} built from the parameters previously set.
         *
         * @return a {@code LocalizedAttribute} built with parameters of this {@code LocalizedAttribute.Builder}
         */
        public LocalizedAttribute build() {
            return new LocalizedAttribute(this);
        }
    }
}

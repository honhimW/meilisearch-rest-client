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
import java.util.ArrayList;
import java.util.List;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#typo-tolerance-object">Typo tolerance object</a>
 * <p style="font-weight:bold;font-size:large">Default object:</p>
 * <pre>
 * {
 *     "enabled": true,
 *     "minWordSizeForTypos": {
 *       "oneTypo": 5,
 *       "twoTypos": 9
 *     },
 *     "disableOnWords": [],
 *     "disableOnAttributes": []
 * }
 * </pre>
 *
 * @author hon_him
 * @since 2024-01-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TypoTolerance implements Serializable {

    @Schema(description = "Whether typo tolerance is enabled or not", defaultValue = "true")
    private Boolean enabled;

    private MinWordSizeForTypos minWordSizeForTypos;

    @Schema(description = "An array of words for which the typo tolerance feature is disabled", defaultValue = "[]")
    private List<String> disableOnWords;

    @Schema(description = "An array of attributes for which the typo tolerance feature is disabled", defaultValue = "[]")
    private List<String> disableOnAttributes;

    private TypoTolerance(Builder builder) {
        setEnabled(builder.enabled);
        setMinWordSizeForTypos(builder.minWordSizeForTypos);
        setDisableOnWords(builder.disableOnWords);
        setDisableOnAttributes(builder.disableOnAttributes);
    }

    /**
     * Creates and returns a default TypoTolerance object with default values.
     *
     * @return  the default TypoTolerance object
     */
    public static TypoTolerance defaultObject() {
        TypoTolerance typoTolerance = new TypoTolerance();
        typoTolerance.setEnabled(true);
        MinWordSizeForTypos minWordSizeForTypos = new MinWordSizeForTypos();
        minWordSizeForTypos.setOneTypo(5);
        minWordSizeForTypos.setTwoTypos(9);
        typoTolerance.setMinWordSizeForTypos(minWordSizeForTypos);
        typoTolerance.setDisableOnWords(new ArrayList<>());
        typoTolerance.setDisableOnAttributes(new ArrayList<>());
        return typoTolerance;
    }

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return  a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MinWordSizeForTypos implements Serializable {

        @Schema(description = "The minimum word size for accepting 1 typo; must be between 0 and twoTypos", defaultValue = "5")
        private Integer oneTypo;

        @Schema(description = "The minimum word size for accepting 2 typos; must be between oneTypo and 255", defaultValue = "9")
        private Integer twoTypos;

        private MinWordSizeForTypos(Builder builder) {
            setOneTypo(builder.oneTypo);
            setTwoTypos(builder.twoTypos);
        }

        /**
         * Creates and returns a new instance of the Builder class.
         *
         * @return  a new instance of the Builder class
         */
        public static Builder builder() {
            return new Builder();
        }


        public static final class Builder {
            private Integer oneTypo;
            private Integer twoTypos;

            private Builder() {
            }

            public Builder oneTypo(Integer val) {
                oneTypo = val;
                return this;
            }

            public Builder twoTypos(Integer val) {
                twoTypos = val;
                return this;
            }

            public MinWordSizeForTypos build() {
                return new MinWordSizeForTypos(this);
            }
        }
    }


    public static final class Builder {
        private Boolean enabled;
        private MinWordSizeForTypos minWordSizeForTypos;
        private List<String> disableOnWords;
        private List<String> disableOnAttributes;

        private Builder() {
        }

        public Builder enabled(Boolean val) {
            enabled = val;
            return this;
        }

        public Builder minWordSizeForTypos(MinWordSizeForTypos val) {
            minWordSizeForTypos = val;
            return this;
        }

        public Builder disableOnWords(List<String> val) {
            disableOnWords = val;
            return this;
        }

        public Builder disableOnAttributes(List<String> val) {
            disableOnAttributes = val;
            return this;
        }

        public TypoTolerance build() {
            return new TypoTolerance(this);
        }
    }
}

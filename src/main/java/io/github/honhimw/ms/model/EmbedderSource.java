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

import io.github.honhimw.ms.Experimental;
import io.github.honhimw.ms.json.EnumValue;
import io.github.honhimw.ms.support.StringUtils;

/**
 * @author hon_him
 * @since 2024-01-18 V1.6
 */

@Experimental(features = Experimental.Features.VECTOR_SEARCH)
public enum EmbedderSource implements EnumValue<EmbedderSource> {

    /**
     * Open AI
     */
    OPEN_AI("openAi"),
    /**
     * Hugging Face
     */
    HUGGING_FACE("huggingFace"),
    /**
     * User provided
     */
    USER_PROVIDED("userProvided"),
    ;

    private final String source;

    EmbedderSource(String source) {
        this.source = source;
    }

    @Override
    public String value() {
        return this.source;
    }

    /**
     * Get enum value by name
     *
     * @param name name of enum
     * @return enum
     */
    public static EmbedderSource of(String name) {
        for (EmbedderSource value : values()) {
            if (StringUtils.equal(value.value(), name)) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format("unknown Embedder Source: [%s]", name));
    }

}

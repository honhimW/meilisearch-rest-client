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

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.honhimw.ms.Experimental;

/**
 * @author hon_him
 * @since 2024-01-18 V1.6
 */

@Experimental(feature = "vector-search")
public enum EmbedderSource {

    OPEN_AI("openAi"),
    HUGGING_FACE("huggingFace"),
    USER_PROVIDED("userProvided"),
    ;

    private final String source;

    EmbedderSource(String source) {
        this.source = source;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.source;
    }

}

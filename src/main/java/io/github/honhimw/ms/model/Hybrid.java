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
 * <h2>Default object</h2>
 * <pre>
 * {
 *     "semanticRatio": 0.5,
 *     "embedder": "default"
 * }
 * </pre>
 *
 * @author hon_him
 * @since 2024-01-18 V1.6
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Hybrid implements Serializable {

    /**
     * number between 0 and 1. 0 indicates full keyword search, 1 indicates full semantic search. Defaults to 0.5", defaultValue = "0.5
     */
    @Schema(description = "number between 0 and 1. 0 indicates full keyword search, 1 indicates full semantic search. Defaults to 0.5", defaultValue = "0.5")
    private Double semanticRatio;

    /**
     * string, indicating one of the embedders configured for the queried index. Defaults to \"default\"", defaultValue = "default
     */
    @Schema(description = "string, indicating one of the embedders configured for the queried index. Defaults to \"default\"", defaultValue = "default")
    private String embedder;

    /**
     * Hybrid default object
     * @return Hybrid default
     */
    public static Hybrid defaultObject() {
        Hybrid hybrid = new Hybrid();
        hybrid.setSemanticRatio(0.5);
        hybrid.setEmbedder("default");
        return hybrid;
    }

}

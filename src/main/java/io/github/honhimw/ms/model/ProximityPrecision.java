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
 *   "proximityPrecision": "byWord"
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
public class ProximityPrecision implements Serializable {

    @Schema(description = "Precision level when calculating the proximity ranking rule", defaultValue = "byWord")
    private ProximityPrecisionType proximityPrecision;

    /**
     * A static method that creates and returns a default ProximityPrecision object.
     *
     * @return         	the default ProximityPrecision object
     */
    public static ProximityPrecision defaultObject() {
        ProximityPrecision pagination = new ProximityPrecision();
        pagination.setProximityPrecision(ProximityPrecisionType.BY_WORD);
        return pagination;
    }

}

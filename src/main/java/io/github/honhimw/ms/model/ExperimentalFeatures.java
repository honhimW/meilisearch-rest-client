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
import lombok.*;

import java.io.Serializable;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class ExperimentalFeatures implements Serializable {
    
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean metrics;
    
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean scoreDetails;
    
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean vectorSearch;
    
    @Schema(description = "true if feature is active, false otherwise")
    private Boolean exportPuffinReports;
    
}

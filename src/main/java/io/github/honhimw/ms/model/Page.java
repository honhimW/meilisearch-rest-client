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
 * @author hon_him
 * @since 2023-12-29
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> implements Serializable {
    
    @Schema(description = "Limit given for the query. If limit is not provided as a query parameter, this parameter displays the default limit value.", example = "10")
    private Integer limit;
    
    @Schema(description = "Offset given for the query. If offset is not provided as a query parameter, this parameter displays the default offset value.", example = "0")
    private Integer offset;

    @Schema(description = "Total number of browsable results using offset/limit parameters for the given resource.", example = "50")
    private Integer total;
    
    @Schema(description = "results")
    private List<T> results;
    
}

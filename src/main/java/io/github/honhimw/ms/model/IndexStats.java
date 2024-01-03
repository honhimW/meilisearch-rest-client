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
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class IndexStats implements Serializable {

    @Schema(description = "Total number of documents in an index")
    private Integer numberOfDocuments;

    @Schema(description = "If true, the index is still processing documents and attempts to search will result in undefined behavior. If false, the index has finished processing and you can start searching")
    private Boolean isIndexing;

    @Schema(description = "Shows every field in the index along with the total number of documents containing that field in said index")
    private Map<String, Integer> fieldDistribution;

}

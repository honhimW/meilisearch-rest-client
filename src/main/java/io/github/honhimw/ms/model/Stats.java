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
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/stats">Stats</a>
 *
 * @author hon_him
 * @since 2024-01-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Stats implements Serializable {

    /**
     * Size of the database in bytes
     */
    @Schema(description = "Size of the database in bytes")
    private Long databaseSize;

    /**
     * When the last update was made to the database in the RFC 3339 format
     */
    @Schema(description = "When the last update was made to the database in the RFC 3339 format")
    private LocalDateTime lastUpdate;

    /**
     * Object containing the statistics for each index found in the database
     */
    @Schema(description = "Object containing the statistics for each index found in the database")
    private Map<String, IndexStats> indexes;

}

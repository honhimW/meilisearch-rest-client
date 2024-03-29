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
 * <a href="https://www.meilisearch.com/docs/reference/api/settings#pagination-object">Pagination object</a>
 * <h2>Default object</h2>
 * <pre>
 * {
 *     "maxTotalHits": 1000
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
public class Pagination implements Serializable {

    /**
     * The maximum number of search results Meilisearch can return", defaultValue = "1000
     */
    @Schema(description = "The maximum number of search results Meilisearch can return", defaultValue = "1000")
    private Integer maxTotalHits;

    /**
     * Creates and returns a new instance of the Pagination class with default values.
     *
     * @return  a new instance of the Pagination class with default values
     */
    public static Pagination defaultObject() {
        Pagination pagination = new Pagination();
        pagination.setMaxTotalHits(1000);
        return pagination;
    }

}

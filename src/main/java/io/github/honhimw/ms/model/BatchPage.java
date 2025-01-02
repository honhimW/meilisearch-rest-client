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

import io.github.honhimw.ms.api.annotation.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hon_him
 * @since 2025-01-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BatchPage<T> extends Page<T> {

    /**
     * uid of the first batch returned
     */
    @Schema(description = "uid of the first batch returned")
    private Integer from;

    /**
     * Value passed to from to view the next "page" of results.
     * When the value of next is null, there are no more tasks to view
     */
    @Schema(description = "Value passed to from to view the next \"page\" of results.")
    private Integer next;

}

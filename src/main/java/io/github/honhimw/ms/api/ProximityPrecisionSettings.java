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

package io.github.honhimw.ms.api;

import io.github.honhimw.ms.model.Pagination;
import io.github.honhimw.ms.model.ProximityPrecisionType;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://www.meilisearch.com/docs/reference/api/settings#proximity-precision">Proximity precision</a>
 * Calculating the distance between words is a resource-intensive operation. Lowering the precision of this operation may significantly improve performance and will have little impact on result relevancy in most use-cases. Meilisearch uses word distance when ranking results according to proximity and when users perform phrase searches.
 * <p>
 * proximityPrecision accepts one of the following string values:
 * <ul>
 *     <li>"byWord": calculate the precise distance between query terms. Higher precision, but may lead to longer indexing time. This is the default setting</li>
 *     <li>"byAttribute": determine if multiple query terms are present in the same attribute. Lower precision, but shorter indexing time</li>
 * </ul>
 *
 * @author hon_him
 * @see Pagination
 * @since 2024-01-18 V1.6
 */

public interface ProximityPrecisionSettings {

    /**
     * Get the proximity precision settings of an index.
     *
     * @return current index proximity-precision
     */
    @Operation(method = "GET", tags = "/indexes/{index_uid}/settings/proximity-precision")
    ProximityPrecisionType get();

    /**
     * Update the proximity precision settings of an index.
     * @param type proximity precision
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{index_uid}/settings/proximity-precision")
    TaskInfo update(ProximityPrecisionType type);

    /**
     * Reset an index's proximity precision setting to its default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{index_uid}/settings/proximity-precision")
    TaskInfo reset();

}

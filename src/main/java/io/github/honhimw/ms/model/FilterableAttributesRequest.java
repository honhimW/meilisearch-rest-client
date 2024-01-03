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
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FilterableAttributesRequest implements Serializable {

    @Schema(description = "Refine results based on attributes in the filterableAttributes list")
    private String filter;

    public FilterableAttributesRequest and(String filter) {
        if (StringUtils.isBlank(this.filter)) {
            this.filter = filter;
        } else {
            this.filter = this.filter + " AND " + filter;
        }
        return this;
    }

    public FilterableAttributesRequest or(String filter) {
        if (StringUtils.isBlank(this.filter)) {
            this.filter = filter;
        } else {
            this.filter = this.filter + " OR " + filter;
        }
        return this;
    }

}

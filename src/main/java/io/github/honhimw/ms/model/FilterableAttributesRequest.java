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

import io.github.honhimw.ms.support.FilterBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FilterableAttributesRequest implements Serializable {

    /**
     * Refine results based on attributes in the filterableAttributes list
     */
    @Schema(description = "Refine results based on attributes in the filterableAttributes list")
    private String filter;

    /**
     * Sets the filter for the request using the provided consumer.
     *
     * @param  consumer  the consumer that builds the filter
     * @return           the updated FilterableAttributesRequest object
     */
    public FilterableAttributesRequest filter(Consumer<FilterBuilder> consumer) {
        FilterBuilder filterBuilder = FilterBuilder.builder();
        consumer.accept(filterBuilder);
        this.filter = filterBuilder.build();
        return this;
    }

    /**
     * Sets the filter for the request using the provided consumer.
     * @param consumer the consumer that builds the filter
     * @return new FilterableAttributesRequest object
     */
    public static FilterableAttributesRequest builder(Consumer<FilterBuilder> consumer) {
        FilterBuilder filterBuilder = FilterBuilder.builder();
        consumer.accept(filterBuilder);
        String filter = filterBuilder.build();
        FilterableAttributesRequest filterableAttributesRequest = new FilterableAttributesRequest();
        filterableAttributesRequest.setFilter(filter);
        return filterableAttributesRequest;
    }

    /**
     * Set filter and return self
     * @param filter the filter string
     * @return self
     */
    public FilterableAttributesRequest filter(String filter) {
        this.filter = filter;
        return this;
    }
}

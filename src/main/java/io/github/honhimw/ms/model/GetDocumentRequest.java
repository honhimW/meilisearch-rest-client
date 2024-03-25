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

import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetDocumentRequest extends PageRequest {

    @Schema(description = "Document attributes to show (case-sensitive, comma-separated)", defaultValue = "*")
    private List<String> fields;

    @Schema(description = "Refine results based on attributes in the filterableAttributes list", defaultValue = "null")
    private String filter;

    @Override
    public GetDocumentRequest no(int no) {
        setNo(no);
        return this;
    }

    @Override
    public GetDocumentRequest size(int size) {
        setSize(size);
        return this;
    }

    /**
     * set fields
     * @param fields fields
     * @return this
     */
    public GetDocumentRequest fields(List<String> fields) {
        setFields(fields);
        return this;
    }

    /**
     * set filter
     * @param filter filter
     * @return this
     */
    public GetDocumentRequest filter(String filter) {
        setFilter(filter);
        return this;
    }

}

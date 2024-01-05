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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class BatchGetDocumentsRequest extends FilterableAttributesRequest {
    
    @Schema(description = "Number of documents to skip", defaultValue = "0")
    private Integer offset;
    
    @Schema(description = "Number of documents to return", defaultValue = "20")
    private Integer limit;
    
    @Schema(description = "Document attributes to show (case-sensitive, comma-separated)")
    private List<String> fields;

    public BatchGetDocumentsRequest addField(String field) {
        if (Objects.isNull(getFields())) {
            setFields(new ArrayList<>());
        }
        getFields().add(field);
        return this;
    }

}

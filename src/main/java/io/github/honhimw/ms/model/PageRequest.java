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
 * @author hon_him
 * @since 2024-01-05
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PageRequest implements Serializable {

    @Schema(description = "page no, >= 0")
    private Integer no;

    @Schema(description = "page size, > 0")
    private Integer size;

    public PageRequest() {
        this.no = 0;
        this.size = 20;
    }

    public static PageRequest of(int no, int size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setNo(no);
        pageRequest.setSize(size);
        return pageRequest;
    }

    public PageRequest no(int no) {
        setNo(no);
        return this;
    }

    public PageRequest size(int size) {
        setSize(size);
        return this;
    }

    public int toOffset() {
        return no * size;
    }

    public int toLimit() {
        return size;
    }

}

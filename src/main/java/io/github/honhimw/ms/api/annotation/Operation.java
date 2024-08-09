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

package io.github.honhimw.ms.api.annotation;

import java.lang.annotation.*;

/**
 * Rest api operation description
 *
 * @author hon_him
 * @since 2024-06-04
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface Operation {

    /**
     * The HTTP methods for this operation.
     *
     * @return The HTTP methods for this operation.
     */
    String[] method() default {};

    /**
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     *
     * @return the array of tags associated with this operation
     */
    String[] paths() default {};

}

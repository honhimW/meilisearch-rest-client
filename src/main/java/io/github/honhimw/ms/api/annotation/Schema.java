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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * The annotation may be used to define a Schema for a set of elements of the OpenAPI spec, and/ or to define additional properties for the schema. It is applicable e. g. to parameters, schema classes (aka "models"), properties of such models, request and response content, header.
 *
 * @author hon_him
 * @since 2024-08-09
 */

@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Schema {

    /**
     * A description of the schema.
     *
     * @return the schema's description
     */
    String description() default "";

    /**
     * Provides a default value.
     *
     * @return the default value of this schema
     */
    String defaultValue() default "";

    /**
     * Provides an example of the schema. When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
     *
     * @return an example of this schema
     */
    String example() default "";

    /**
     * If true, designates a value as possibly null.
     *
     * @return whether or not this schema is nullable
     */
    boolean nullable() default false;

}

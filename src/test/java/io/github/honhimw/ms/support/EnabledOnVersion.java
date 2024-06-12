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

package io.github.honhimw.ms.support;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

/**
 * Server version base condition.
 *
 * @author hon_him
 * @since 2024-06-06
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(EnabledOnVersionCondition.class)
public @interface EnabledOnVersion {

    /**
     * Tests support versions.
     * @return support versions
     */
    String[] value();

    /**
     * Version higher then or equal to the version{@link #value()}.
     * @return default true
     */
    boolean since() default true;

    /**
     * Version lower than the version{@link #value()}.
     * @return default false
     */
    boolean before() default false;

}

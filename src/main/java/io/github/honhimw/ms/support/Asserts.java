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

/**
 * @author hon_him
 * @since 2024-01-05
 */

public class Asserts {

    /**
     * Assert that an expression is {@code true}.
     *
     * @param status  the expression to assert
     * @param message the exception message to use if the assertion fails
     */
    public static void status(boolean status, String message) {
        if (!status) {
            throw new IllegalStateException(message);
        }
    }

}

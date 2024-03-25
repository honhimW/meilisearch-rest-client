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

package io.github.honhimw.ms.http;

import lombok.Getter;
import lombok.Setter;

/**
 * Rest client http exception
 * @author hon_him
 * @since 2024-02-27
 */

@Setter
@Getter
public class HttpFailureException extends IllegalStateException {

    private final int statusCode;

    private String method;

    private String uri;

    /**
     * Constructor with status code
     * @param statusCode http status code
     */
    public HttpFailureException(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Constructor with status code and message
     * @param statusCode http status code
     * @param s message
     */
    public HttpFailureException(int statusCode, String s) {
        super(s);
        this.statusCode = statusCode;
    }

    /**
     * Constructor with status code, message and cause
     * @param statusCode http status code
     * @param message message
     * @param cause cause
     */
    public HttpFailureException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * Constructor with status code and cause
     * @param statusCode http status code
     * @param cause cause
     */
    public HttpFailureException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    /**
     * Formatted message
     * @return formatted message
     */
    public String formatted() {
        return String.format("failure with status code: [%d] [%s] %s, %s", statusCode, method, uri, getMessage());
    }

    @Override
    public String getLocalizedMessage() {
        return formatted();
    }
}

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
 * @author hon_him
 * @since 2024-02-27
 */

@Setter
@Getter
public class HttpFailureException extends IllegalStateException {

    private final int statusCode;

    private String method;

    private String uri;

    public HttpFailureException(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpFailureException(int statusCode, String s) {
        super(s);
        this.statusCode = statusCode;
    }

    public HttpFailureException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpFailureException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public String formatted() {
        return String.format("failure with status code: [%d] [%s] %s, %s", statusCode, method, uri, getMessage());
    }

    @Override
    public String getLocalizedMessage() {
        return formatted();
    }
}

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

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-04
 */

public interface RequestConfigurer {

    RequestConfigurer method(String method);

    RequestConfigurer uri(String uri);

    RequestConfigurer addHeader(String key, String value);

    RequestConfigurer setHeaders(Map<String, List<String>> headers);

    RequestConfigurer addParameter(String key, String value);

    RequestConfigurer addParameters(Map<String, List<String>> parameters);

    RequestConfigurer body(Consumer<BodyConfigurer> bodyConfigurer);

}

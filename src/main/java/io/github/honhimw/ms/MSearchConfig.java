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

package io.github.honhimw.ms;

import io.github.honhimw.ms.http.ReactiveHttpUtils;
import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.support.Asserts;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @author hon_him
 * @since 2023-07-24
 */

@Data
@AllArgsConstructor
public final class MSearchConfig {

    private final String serverUrl;
    
    private final String apiKey;

    private final JsonHandler jsonHandler;

    private final ReactiveHttpUtils httpClient;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder withDefault() {
        return new Builder()
            .jsonHandler(new JacksonJsonHandler())
            .httpClient(ReactiveHttpUtils.getInstance());
    }

    public static class Builder {
        private String serverUrl;
        private String apiKey;
        private JsonHandler jsonHandler;
        private ReactiveHttpUtils httpClient;

        public Builder() {
        }

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder apiKey(@Nullable String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder jsonHandler(JsonHandler jsonHandler) {
            this.jsonHandler = jsonHandler;
            return this;
        }

        public Builder httpClient(ReactiveHttpUtils httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public MSearchConfig build() {
            Asserts.status(Objects.nonNull(serverUrl), "serverUrl must not be null");
            Asserts.status(Objects.nonNull(jsonHandler), "jsonHandler must not be null");
            Asserts.status(Objects.nonNull(httpClient), "httpClient must not be null");
            return new MSearchConfig(serverUrl, apiKey, jsonHandler, httpClient);
        }
    }
}

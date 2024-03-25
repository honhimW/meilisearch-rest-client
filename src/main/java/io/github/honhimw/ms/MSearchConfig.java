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
import io.github.honhimw.ms.http.ResponseFilter;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.support.Asserts;
import io.github.honhimw.ms.support.StringUtils;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

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

    private final ResponseFilter responseFilter;

    /**
     * Creates and returns a new instance of the Builder class.
     *
     * @return  a new instance of the Builder class
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates and returns a new instance of the Builder class.
     * With default HttpClient.
     *
     * @return  a new instance of the Builder class
     */
    public static Builder withDefault() {
        return builder()
//            .jsonHandler(new JacksonJsonHandler()) // ClassNotFoundException when exclude com.fasterxml.jackson.*
            .httpClient(ReactiveHttpUtils.getInstance());
    }

    /**
     * {@code MSearchConfig} builder static inner class.
     */
    public static class Builder {
        private String serverUrl;
        private boolean ssl = false;
        private String host;
        private int port = 7700;
        private String apiKey;
        private JsonHandler jsonHandler;
        private ReactiveHttpUtils httpClient;
        private ResponseFilter responseFilter = (response, bytes) -> Mono.just(bytes);

        private Builder() {
        }

        /**
         * Meilisearch server url. If is not empty, it will override {@code enabledSSL} {@code host} and {@code port}
         *
         * @param serverUrl the {@code serverUrl} to set
         * @return a reference to this Builder
         */
        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        /**
         * false: http, true: https
         *
         * @param enabled the {@code enabled} to set
         * @return a reference to this Builder
         */
        public Builder enableSSL(boolean enabled) {
            this.ssl = enabled;
            return this;
        }

        /**
         * Meilisearch server host
         *
         * @param host the {@code host} to set
         * @return a reference to this Builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Meilisearch server port
         *
         * @param port the {@code port} to set
         * @return a reference to this Builder
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Meilisearch server api key
         *
         * @param apiKey the {@code apiKey} to set
         * @return a reference to this Builder
         */
        public Builder apiKey(@Nullable String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * json handler
         *
         * @param jsonHandler the {@code jsonHandler} to set
         * @return a reference to this Builder
         */
        public Builder jsonHandler(JsonHandler jsonHandler) {
            this.jsonHandler = jsonHandler;
            return this;
        }

        /**
         * Configured http client
         *
         * @param httpClient the {@code httpClient} to set
         * @return a reference to this Builder
         */
        public Builder httpClient(ReactiveHttpUtils httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * response custom filter
         *
         * @param responseFilter the {@code responseFilter} to set
         * @return a reference to this Builder
         */
        public Builder responseFilter(ResponseFilter responseFilter) {
            this.responseFilter = responseFilter;
            return this;
        }

        /**
         * Returns a {@code MSearchConfig} built from the parameters previously set.
         *
         * @return a {@code MSearchConfig} built with parameters of this {@code MSearchConfig.Builder}
         */
        public MSearchConfig build() {
            if (StringUtils.isBlank(serverUrl)) {
                serverUrl = String.format("%s://%s:%s", ssl ? "https" : "http", host, port);
            }
            Asserts.status(Objects.nonNull(serverUrl), "serverUrl must not be null");
            Asserts.status(Objects.nonNull(jsonHandler), "jsonHandler must not be null");
            Asserts.status(Objects.nonNull(httpClient), "httpClient must not be null");
            Asserts.status(Objects.nonNull(responseFilter), "responseConsumer must not be null");
            return new MSearchConfig(serverUrl, apiKey, jsonHandler, httpClient, responseFilter);
        }
    }
}

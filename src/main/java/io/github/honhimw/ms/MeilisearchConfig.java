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

import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author hon_him
 * @since 2023-07-24
 */

@Data
@AllArgsConstructor
public final class MeilisearchConfig {

    private final String serverUrl;
    
    private final String apiKey;

    private final JsonHandler jsonHandler;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder withDefault() {
        return new Builder()
            .jsonHandler(new JacksonJsonHandler());
    }

    public static class Builder {
        private String serverUrl;
        private String apiKey;
        private JsonHandler jsonHandler;

        public Builder() {
        }

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder jsonHandler(JsonHandler jsonHandler) {
            this.jsonHandler = jsonHandler;
            return this;
        }

        public MeilisearchConfig build() {
            return new MeilisearchConfig(serverUrl, apiKey, jsonHandler);
        }
    }
}

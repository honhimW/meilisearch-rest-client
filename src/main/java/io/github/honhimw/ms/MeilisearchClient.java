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

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.json.JsonHandler;

/**
 * @author hon_him
 * @since 2023-07-24
 */

public class MeilisearchClient {

    private final String serverUrl;

    private final String apiKey;

    private final JsonHandler jsonHandler;

    public MeilisearchClient(MeilisearchConfig config) {
        this.serverUrl = config.getServerUrl();
        this.apiKey = config.getApiKey();
        this.jsonHandler = config.getJsonHandler();
    }

    /*
    ==========================================================================
    Indexes
    ==========================================================================
     */

    public Indexes index() {
        // TODO
        return null;
    }

}

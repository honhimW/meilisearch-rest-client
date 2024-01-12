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

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public class OfficialClientTests {

    private Client client;

    @BeforeEach
    void init() {
        String hostUrl = String.format("http://%s:%d", MeiliSearchProperties.getHost(), MeiliSearchProperties.getPort());
        Config config = new Config(hostUrl);
        client = new Client(config);
    }

}

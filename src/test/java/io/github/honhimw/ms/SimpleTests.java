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

import io.github.honhimw.ms.api.MSearchClient;
import io.github.honhimw.ms.api.reactive.ReactiveMSearchClient;
import io.github.honhimw.ms.http.ReactiveHttpUtils;
import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.model.SearchResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author hon_him
 * @since 2024-01-09
 */

public class SimpleTests {

    @Test
    @SneakyThrows
    void loadResources() {
        InputStream config = ClassLoader.getSystemResourceAsStream("meili-search.properties");
        Properties properties = new Properties();
        properties.load(config);
        properties.list(System.err);
        assert properties.containsKey("server.host");
        assert properties.containsKey("server.port");
        assert properties.containsKey("server.api-key");
        assert !properties.containsKey("server.api-key2") : "not api-key2 property";
    }

    @Test
    @SneakyThrows
    void httpClient() {
        ReactiveHttpUtils instance = ReactiveHttpUtils.getInstance();
        {
            ReactiveHttpUtils.HttpResult httpResult = instance.get("https://httpbin.org/get?hello=world");
            System.out.println(httpResult.str());
        }
        instance.close();
        try {
            ReactiveHttpUtils.HttpResult httpResult = instance.get("https://httpbin.org/get?hello=world");
            System.out.println(httpResult.str());
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

}

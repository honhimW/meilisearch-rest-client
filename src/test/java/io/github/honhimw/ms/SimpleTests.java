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
import io.github.honhimw.ms.http.URIBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.InputStream;
import java.net.ServerSocket;
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
        ReactiveHttpUtils instance = ReactiveHttpUtils.getInstance(builder -> builder.requestInterceptor(configurer -> configurer.param("foo", "bar")));
        HttpServer httpServer = HttpServer.create();
        ServerSocket serverSocket = new ServerSocket(0);
        int randomPort = serverSocket.getLocalPort();
        serverSocket.close();
        String helloWorld = "hello world";
        DisposableServer disposableServer = httpServer
            .route(httpServerRoutes -> httpServerRoutes
                .get("/api", (req, resp) -> {
                    String uri = req.uri();
                    URIBuilder.from(uri).getQueryParams().forEach(e -> resp.addHeader(e.getKey(), e.getValue()));
                    return resp.sendString(Mono.just(helloWorld));
                }))
            .port(randomPort)
            .bindNow();
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(randomPort).setPath("/api").build().toString();
        {
            ReactiveHttpUtils.HttpResult httpResult = instance.get(uri);
            System.out.println(httpResult.str());
            assert "bar".equals(httpResult.getHeader("foo"));
            assert helloWorld.equals(httpResult.str());
        }
        instance.close();
        try {
            ReactiveHttpUtils.HttpResult httpResult = instance.get(uri);
            System.out.println(httpResult.str());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert e instanceof IllegalStateException;
        }
        disposableServer.disposeNow();
    }

}

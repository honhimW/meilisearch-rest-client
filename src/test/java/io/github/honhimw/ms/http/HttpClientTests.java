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

import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-02-27
 */

public class HttpClientTests {

    private static final String MESSAGE = "hello world";

    @SneakyThrows
    static DisposableServer createClient(Consumer<? super HttpServerRoutes> routesBuilder) {
        HttpServer httpServer = HttpServer.create();
        ServerSocket serverSocket = new ServerSocket(0);
        int randomPort = serverSocket.getLocalPort();
        serverSocket.close();
        return httpServer
            .route(routesBuilder)
            .port(randomPort)
            .bindNow();
    }

    @Test
    @SneakyThrows
    void close() {
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        httpClient.close();
        try {
            httpClient.get("http://localhost/");
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

    @Test
    @SneakyThrows
    void get() {
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.get("/api", (req, resp) -> {
            String uri = req.uri();
            URIBuilder.from(uri).getQueryParams().forEach(e -> resp.addHeader(e.getKey(), e.getValue()));
            return resp.sendString(Mono.just(MESSAGE));
        }));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).setPath("/api").build().toString();
        ReactiveHttpUtils.HttpResult httpResult = httpClient.get(uri, configurer -> configurer.param("foo", "bar"));
        assert "bar".equals(httpResult.getHeader("foo"));
        assert MESSAGE.equals(httpResult.str());
        httpClient.close();
        disposableServer.disposeNow();
    }

    @Test
    @SneakyThrows
    void post() {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.post("/api", (req, resp) ->
            resp.sendString(req.receive().asByteArray().reduce(HttpClientTests::concat)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .map(s -> jsonHandler.fromJson(s, Map.class))
                .doOnNext(map -> map.forEach((o, o2) -> resp.addHeader(String.valueOf(o), String.valueOf(o2))))
                .map(map -> MESSAGE))));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).setPath("/api").build().toString();
        ReactiveHttpUtils.HttpResult httpResult = httpClient.post(uri, configurer -> configurer.body(payload -> payload.raw(raw -> raw.json(jsonQuote("{'foo':'bar'}")))));
        assert "bar".equals(httpResult.getHeader("foo"));
        assert MESSAGE.equals(httpResult.str());
        httpClient.close();
        disposableServer.disposeNow();
    }

    @Test
    @SneakyThrows
    void put() {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.put("/api", (req, resp) ->
            resp.sendString(req.receive().asByteArray().reduce(HttpClientTests::concat)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .map(s -> jsonHandler.fromJson(s, Map.class))
                .doOnNext(map -> map.forEach((o, o2) -> resp.addHeader(String.valueOf(o), String.valueOf(o2))))
                .map(map -> MESSAGE))));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).setPath("/api").build().toString();
        ReactiveHttpUtils.HttpResult httpResult = httpClient.put(uri, configurer -> configurer.body(payload -> payload.raw(raw -> raw.json(jsonQuote("{'foo':'bar'}")))));
        assert "bar".equals(httpResult.getHeader("foo"));
        assert MESSAGE.equals(httpResult.str());
        httpClient.close();
        disposableServer.disposeNow();
    }

    @Test
    @SneakyThrows
    void delete() {
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.delete("/api", (req, resp) ->
            resp.sendString(Mono.just(MESSAGE))));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).setPath("/api").build().toString();
        ReactiveHttpUtils.HttpResult httpResult = httpClient.delete(uri);
        assert MESSAGE.equals(httpResult.str());
        httpClient.close();
        disposableServer.disposeNow();
    }

    private static String jsonQuote(String json) {
        return json.replaceAll("'", "\"");
    }

    private static byte[] concat(byte[] a1, byte[] a2) {
        int length = a1.length + a2.length;
        byte[] result = new byte[length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

}

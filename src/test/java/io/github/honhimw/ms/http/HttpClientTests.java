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
import io.github.honhimw.ms.support.StringUtils;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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
        ReactiveHttpUtils.HttpResult httpResult = httpClient.get(uri, configurer -> configurer.params(Collections.singletonMap("foo", "bar")));
        assert "bar".equals(httpResult.getHeader("foo"));
        assert MESSAGE.equals(httpResult.str());
        httpClient.close();
        disposableServer.disposeNow();
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    @Test
    @SneakyThrows
    void methods() {
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.route(req -> {
            HttpMethod method = req.method();
            String name = method.name();
            return StringUtils.equal(req.requestHeaders().get("method"), name);
        }, (req, resp) -> {
            String charset = "utf-16";
            resp.addHeader("content-type", "text/plain;charset=" + charset);
            return resp.sendString(Mono.just(MESSAGE), Charset.forName(charset));
        }));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).build().toString();
        assert StringUtils.equal(httpClient.get(uri, configurer -> configurer.header("method", "GET")).str(), MESSAGE);
        assert StringUtils.equal(httpClient.post(uri, configurer -> configurer.headers(Collections.singletonMap("method", "POST"))).str(), MESSAGE);
        assert StringUtils.equal(httpClient.put(uri, configurer -> configurer.header("method", "PUT")).str(), MESSAGE);
        assert StringUtils.equal(httpClient.patch(uri, configurer -> configurer.header("method", "PATCH")).str(), MESSAGE);
        assert StringUtils.equal(httpClient.options(uri, configurer -> configurer.header("method", "OPTIONS")).str(), MESSAGE);
        assert httpClient.head(uri, configurer -> configurer.header("method", "HEAD")).isOK();
        assert StringUtils.equal(httpClient.delete(uri, configurer -> configurer.header("method", "DELETE").config(builder -> builder.enableCompress(true))).str(), MESSAGE);
        Exception e = null;
        try {
            httpClient.request("HELLO", uri, configurer -> configurer.header("method", "HELLO"));
        } catch (Exception _e) {
            e = _e;
        }
        assert Objects.nonNull(e);

        assert isOK(httpClient.rGet(uri, configurer -> configurer.header("method", "GET")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rPost(uri, configurer -> configurer.header("method", "POST")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rPut(uri, configurer -> configurer.header("method", "PUT")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rPatch(uri, configurer -> configurer.header("method", "PATCH")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rOptions(uri, configurer -> configurer.header("method", "OPTIONS")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rHead(uri, configurer -> configurer.header("method", "HEAD")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
        assert isOK(httpClient.rDelete(uri, configurer -> configurer.header("method", "DELETE")).response().map(HttpClientResponse::status).map(HttpResponseStatus::code).block());
    }

    @Test
    @SneakyThrows
    void binary() {
        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        DisposableServer disposableServer = createClient(httpServerRoutes -> httpServerRoutes.route(req -> true, (req, resp) -> {
            String _charset = "utf-16";
            Charset charset = Charset.forName(_charset);
            resp.addHeader("content-type", "text/plain;charset=" + charset);
            return resp.sendString(req.receive().asByteArray().reduce(HttpClientTests::concat)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8)), charset);
        }));
        String uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(disposableServer.port()).build().toString();
        byte[] bytes = MESSAGE.getBytes(StandardCharsets.UTF_8);
        assert StringUtils.equal(httpClient.post(uri, configurer -> configurer.body(payload -> payload.binary(binary -> binary.bytes(bytes)))).str(), MESSAGE);
        File tempFile = File.createTempFile("__mrc", "mrc__");
        Files.write(tempFile.toPath(), bytes);

        assert StringUtils.equal(httpClient.post(uri, configurer -> configurer.body(payload -> payload.binary(binary -> binary.file(tempFile)))).str(), MESSAGE);
        try (FileInputStream ips = new FileInputStream(tempFile)) {
            assert StringUtils.equal(httpClient.post(uri, configurer -> configurer.body(payload -> payload.binary(binary -> binary.inputStream(ips)))).str(), MESSAGE);
        }

        tempFile.delete();
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

    private static boolean isOK(Integer code) {
        return Objects.equals(200, code);
    }

}

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

package io.github.honhimw.ms.reactor;

import io.github.honhimw.ms.MeiliSearchProperties;
import io.github.honhimw.ms.api.reactive.ReactiveMSearchClient;
import io.github.honhimw.ms.client.TestBase;
import io.github.honhimw.ms.http.ReactiveHttpUtils;
import io.github.honhimw.ms.model.SearchResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-03-15
 */

public class ContextTests extends TestBase {

    @Test
    void addParam() {
        Mono<String> foo = Mono.just("foo");
        Mono<String> next = foo
            .transformDeferredContextual((stringMono, contextView) -> {
                Object o = contextView.get("hello");
                assert "world".equals(o);
                return stringMono;
            }).contextWrite(context -> context.put("hello", "world"));

        assert "foo".equals(next.block());
    }

    @Test
    @SneakyThrows
    void httpContext() {
        String respCtx = "RESP_CTX";

        ReactiveHttpUtils httpClient = ReactiveHttpUtils.getInstance();
        // print http request
        httpClient.addInterceptor(configurer -> {
            StringBuilder sb = new StringBuilder();
            sb.append(configurer.method()).append(' ').append(configurer.url()).append(System.lineSeparator());
            configurer.headers().forEach((s, strings) -> strings.forEach(s1 -> sb.append(s).append(": ").append(s1).append(System.lineSeparator())));
            sb.append(System.lineSeparator());
            System.out.println(sb);
        });

        reactiveClient = ReactiveMSearchClient.create(builder -> builder
            .enableSSL(false)
            .host(MeiliSearchProperties.getHost())
            .port(MeiliSearchProperties.getPort())
            .apiKey(MeiliSearchProperties.getApiKey())
            .jsonHandler(jsonHandler)
            .httpClient(httpClient)
            .responseFilter((response, bytes) -> Mono.deferContextual(contextView -> {
                // put http response in context and modify response content
                Optional<Map> ctx = contextView.getOrEmpty(respCtx);
                ctx.ifPresent(map -> {
                    map.put("resp", response);
                    map.put("bytes", bytes);
                });
                System.out.println(response.toString());

                // you may also return a Mono.error() to indicate that the result is not as expected
//                return Mono.just("{}".getBytes(StandardCharsets.UTF_8));
                return Mono.just(bytes);
            })));
        Mono<SearchResponse<Map<String, Object>>> searchResponseMono = reactiveClient.indexes().search("movies").find("2");

        Map<Object, Object> _map = new HashMap<>();
        // defer context after search-api request chain
        searchResponseMono = searchResponseMono.transformDeferredContextual((searchResponseMono1, contextView) ->
            searchResponseMono1.map(searchResponse -> {
                Map o = contextView.get(respCtx);
                byte[] o1 = (byte[]) o.get("bytes");
                System.out.println(o1.length);
                return searchResponse;
            }));

        // put context in Reactor context
        searchResponseMono = searchResponseMono.contextWrite(context -> context.put(respCtx, _map));
        System.out.println(searchResponseMono.block());
    }

}

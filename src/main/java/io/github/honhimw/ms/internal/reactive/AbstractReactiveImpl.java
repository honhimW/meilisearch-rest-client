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

package io.github.honhimw.ms.internal.reactive;

import io.github.honhimw.ms.http.HttpFailureException;
import io.github.honhimw.ms.http.ReactiveHttpUtils;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.json.TypeRef;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public abstract class AbstractReactiveImpl {

    protected final ReactiveMSearchClientImpl _client;

    protected final JsonHandler jsonHandler;

    protected AbstractReactiveImpl(ReactiveMSearchClientImpl client) {
        this._client = client;
        this.jsonHandler = _client.jsonHandler;
    }

    protected ReactiveHttpUtils getHttpClient() {
        return _client.httpClient;
    }

    protected String fulfillUri(String path) {
        return _client.serverUrl + path;
    }

    protected <T> Mono<T> get(String path, TypeRef<T> typeRef) {
        return get(path, configurer -> {
        }, typeRef);
    }

    protected <T> Mono<T> post(String path, TypeRef<T> typeRef) {
        return post(path, configurer -> {
        }, typeRef);
    }

    protected <T> Mono<T> put(String path, TypeRef<T> typeRef) {
        return put(path, configurer -> {
        }, typeRef);
    }

    protected <T> Mono<T> patch(String path, TypeRef<T> typeRef) {
        return patch(path, configurer -> {
        }, typeRef);
    }

    protected <T> Mono<T> delete(String path, TypeRef<T> typeRef) {
        return delete(path, configurer -> {
        }, typeRef);
    }

    protected <T> Mono<T> get(String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        return request("GET", path, configurer, typeRef);
    }

    protected <T> Mono<T> post(String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        return request("POST", path, configurer, typeRef);
    }

    protected <T> Mono<T> put(String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        return request("PUT", path, configurer, typeRef);
    }

    protected <T> Mono<T> patch(String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        return request("PATCH", path, configurer, typeRef);
    }

    protected <T> Mono<T> delete(String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        return request("DELETE", path, configurer, typeRef);
    }

    protected <T> Mono<T> request(String method, String path, Consumer<ReactiveHttpUtils.Configurer> configurer, TypeRef<T> typeRef) {
        if (Objects.nonNull(_client.apiKey)) {
            Consumer<ReactiveHttpUtils.Configurer> _apiKey_configurer = configurer1 -> configurer1
                .header("Authorization", String.format("Bearer %s", _client.apiKey));
            configurer = _apiKey_configurer.andThen(configurer);

        }
        ReactiveHttpUtils.ReactiveHttpResult receiver = getHttpClient().receiver(method, fulfillUri(path), configurer);
        return extract(receiver, typeRef);
    }

    protected <T> Mono<T> extract(ReactiveHttpUtils.ReactiveHttpResult receiver, TypeRef<T> typeRef) {
        return receiver.responseSingle((httpClientResponse, byteBufMono) -> byteBufMono.asByteArray()
            .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
            .handle((s, sink) -> {
                int code = httpClientResponse.status().code();
                if (code < 200 || 300 <= code) {
                    HttpFailureException httpFailureException = new HttpFailureException(code, s);
                    httpFailureException.setMethod(httpClientResponse.method().name());
                    httpFailureException.setUri(httpClientResponse.resourceUrl());
                    sink.error(httpFailureException);
                } else {
                    T t = jsonHandler.fromJson(s, typeRef);
                    if (Objects.nonNull(t)) {
                        sink.next(t);
                    }
                }
            }));
    }

    protected void json(ReactiveHttpUtils.Configurer configurer, Object object) {
        configurer.body(payload -> payload.raw(raw -> raw.json(jsonHandler.toJson(object))));
    }

    protected void json(ReactiveHttpUtils.Configurer configurer, String json) {
        configurer.body(payload -> payload.raw(raw -> raw.json(json)));
    }

}

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
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TaskStatus;
import io.github.honhimw.ms.model.TaskType;
import io.github.honhimw.ms.model.TaskView;
import io.github.honhimw.ms.support.ReactorUtils;
import io.github.honhimw.ms.support.TypeRefs;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author hon_him
 * @since 2024-01-03
 */

abstract class AbstractReactiveImpl {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

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
        return receiver.responseSingle((httpClientResponse, byteBufMono) -> {
                HttpResponseStatus status = httpClientResponse.status();
                int code = status.code();
                Charset charset = ReactiveHttpUtils.getCharset(httpClientResponse);
                Mono<String> stringMono = byteBufMono.asByteArray()
                    .flatMap(bytes -> _client.responseFilter.accept(httpClientResponse, bytes))
                    .map(bytes -> new String(bytes, charset));
                if (code < 200 || 300 <= code) {
                    return stringMono
                        .switchIfEmpty(Mono.just(status.reasonPhrase()))
                        .handle((s, sink) -> {
                            HttpFailureException httpFailureException = new HttpFailureException(code, s);
                            httpFailureException.setMethod(httpClientResponse.method().name());
                            httpFailureException.setUri(httpClientResponse.resourceUrl());
                            sink.error(httpFailureException);
                        });
                } else {
                    return stringMono
                        .mapNotNull(s -> jsonHandler.fromJson(s, typeRef));
                }
            })
            .onErrorResume(throwable -> {
                if (log.isDebugEnabled()) {
                    log.debug("Process failed", throwable);
                }
                if (throwable instanceof HttpFailureException) {
                    HttpFailureException httpFailureException = (HttpFailureException) throwable;
                    return httpFailureException.getStatusCode() == 404;
                } else {
                    return false;
                }
            }, throwable -> Mono.empty())
            .map(t -> decorate(t, typeRef));
    }

    protected void json(ReactiveHttpUtils.Configurer configurer, Object object) {
        configurer.body(payload -> payload.raw(raw -> raw.json(jsonHandler.toJson(object))));
    }

    protected void json(ReactiveHttpUtils.Configurer configurer, String json) {
        configurer.body(payload -> payload.raw(raw -> raw.json(json)));
    }

    @SuppressWarnings("unchecked")
    protected <T> T decorate(T delegate, TypeRef<T> typeRef) {
        if (typeRef == TypeRefs.TaskInfoRef.INSTANCE) {
            return (T) delegateTaskInfo((TaskInfo) delegate);
        }
        return delegate;
    }

    protected TaskInfo delegateTaskInfo(TaskInfo taskInfo) {
        return new DelegateTaskInfo(taskInfo) {
            @Override
            public TaskView await(Duration duration) {
                Mono<TaskView> tasks = _client.tasks(reactiveTasks -> reactiveTasks
                    .await(this, _client.config.getAwaitAttempts(), _client.config.getAwaitFixedDelay(), duration));
                return ReactorUtils.blockNonNull(tasks);
            }
        };
    }

    private static abstract class DelegateTaskInfo extends TaskInfo {
        private final TaskInfo taskInfo;

        public DelegateTaskInfo(TaskInfo taskInfo) {
            this.taskInfo = taskInfo;
        }

        @Override
        public Integer getTaskUid() {
            return taskInfo.getTaskUid();
        }

        @Override
        public String getIndexUid() {
            return taskInfo.getIndexUid();
        }

        @Override
        public TaskStatus getStatus() {
            return taskInfo.getStatus();
        }

        @Override
        public TaskType getType() {
            return taskInfo.getType();
        }

        @Override
        public LocalDateTime getEnqueuedAt() {
            return taskInfo.getEnqueuedAt();
        }

        @Override
        public void setTaskUid(Integer taskUid) {
            taskInfo.setTaskUid(taskUid);
        }

        @Override
        public void setIndexUid(String indexUid) {
            taskInfo.setIndexUid(indexUid);
        }

        @Override
        public void setStatus(TaskStatus status) {
            taskInfo.setStatus(status);
        }

        @Override
        public void setType(TaskType type) {
            taskInfo.setType(type);
        }

        @Override
        public void setEnqueuedAt(LocalDateTime enqueuedAt) {
            taskInfo.setEnqueuedAt(enqueuedAt);
        }
    }


}

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

import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.IOUtils;
import io.github.honhimw.ms.support.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.ByteBufMono;
import reactor.netty.Connection;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClient.RequestSender;
import reactor.netty.http.client.HttpClient.ResponseReceiver;
import reactor.netty.http.client.HttpClientForm;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.resources.ConnectionProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <table>
 *     <tr style="color:yellow">
 *         <th>Property</th>
 *         <th>default value</th>
 *     </tr>
 *     <tr>
 *         <td>connect timeout</td>
 *         <td>4s</td>
 *     </tr>
 *     <tr>
 *         <td>read timeout</td>
 *         <td>30s</td>
 *     </tr>
 *     <tr>
 *         <td>HTTP Protocol</td>
 *         <td>HTTP/1.1, HTTP/2</td>
 *     </tr>
 *     <tr>
 *         <td>follow redirect</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>keepalive</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>proxy with system properties</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>compress</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>retry</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>ssl</td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td>max connections</td>
 *         <td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>pending acquire max count</td>
 *         <td>1000</td>
 *     </tr>
 * </table>
 *
 * @author hon_him
 * @see RequestConfig.Builder default request configuration
 * @since 2023-02-22
 */
@SuppressWarnings({
    "unused",
    "UnusedReturnValue",
})
public class ReactiveHttpUtils implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ReactiveHttpUtils.class);

    private ReactiveHttpUtils() {
    }

    @Override
    public void close() {
        synchronized (this) {
            if (!Objects.equals(connectionProvider, NoopConnectProvider.INSTANCE)) {
                connectionProvider.dispose();
                connectionProvider = NoopConnectProvider.INSTANCE;
                httpClient = HttpClient.create(connectionProvider);
            } else {
                throw new IllegalStateException("already closed.");
            }
        }
    }

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_HEAD = "HEAD";

    public static ReactiveHttpUtils getInstance() {
        ReactiveHttpUtils reactiveHttpUtils = new ReactiveHttpUtils();
        reactiveHttpUtils.init();
        return reactiveHttpUtils;
    }

    public static ReactiveHttpUtils getInstance(Consumer<RequestConfig.Builder> configurer) {
        ReactiveHttpUtils reactiveHttpUtils = new ReactiveHttpUtils();
        RequestConfig.Builder copy = RequestConfig.DEFAULT_CONFIG.copy();
        configurer.accept(copy);
        RequestConfig requestConfig = copy.build();
        reactiveHttpUtils.init(requestConfig);
        return reactiveHttpUtils;
    }

    /**
     * max total connections
     */
    public static final int MAX_TOTAL_CONNECTIONS = 1_000;

    /**
     * max connections per route
     */
    public static final int MAX_ROUTE_CONNECTIONS = 200;

    /**
     * connect timeout
     */
    public static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(4);

    /**
     * read timeout
     */
    public static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

    private static final Charset defaultCharset = StandardCharsets.UTF_8;

    private HttpClient httpClient;

    private ConnectionProvider connectionProvider;

    @Getter
    private final List<Consumer<Configurer>> requestInterceptors = new ArrayList<>();

    public ReactiveHttpUtils addInterceptor(Consumer<Configurer> interceptor) {
        Objects.requireNonNull(interceptor);
        requestInterceptors.add(interceptor);
        return this;
    }

    private void init() {
        init(RequestConfig.DEFAULT_CONFIG);
    }

    private void init(RequestConfig requestConfig) {
        connectionProvider = requestConfig.connectionProvider;
        httpClient = HttpClient.create(connectionProvider);
        httpClient = requestConfig.config(httpClient);
        addInterceptor(requestConfig.requestInterceptor);
    }

    public HttpResult get(String url) {
        return request(METHOD_GET, url, configurer -> {
        });
    }

    public HttpResult post(String url) {
        return request(METHOD_POST, url, configurer -> {
        });
    }

    public HttpResult put(String url) {
        return request(METHOD_PUT, url, configurer -> {
        });
    }

    public HttpResult patch(String url) {
        return request(METHOD_PATCH, url, configurer -> {
        });
    }

    public HttpResult delete(String url) {
        return request(METHOD_DELETE, url, configurer -> {
        });
    }

    public HttpResult options(String url) {
        return request(METHOD_OPTIONS, url, configurer -> {
        });
    }

    public HttpResult head(String url) {
        return request(METHOD_HEAD, url, configurer -> {
        });
    }

    public HttpResult get(String url, Consumer<Configurer> configurer) {
        return request(METHOD_GET, url, configurer);
    }

    public HttpResult post(String url, Consumer<Configurer> configurer) {
        return request(METHOD_POST, url, configurer);
    }

    public HttpResult put(String url, Consumer<Configurer> configurer) {
        return request(METHOD_PUT, url, configurer);
    }

    public HttpResult patch(String url, Consumer<Configurer> configurer) {
        return request(METHOD_PATCH, url, configurer);
    }

    public HttpResult delete(String url, Consumer<Configurer> configurer) {
        return request(METHOD_DELETE, url, configurer);
    }

    public HttpResult options(String url, Consumer<Configurer> configurer) {
        return request(METHOD_OPTIONS, url, configurer);
    }

    public HttpResult head(String url, Consumer<Configurer> configurer) {
        return request(METHOD_HEAD, url, configurer);
    }

    public HttpResult request(String method, String url, Consumer<Configurer> configurer) {
        return request(method, url, configurer, httpResult -> httpResult);
    }

    public ReactiveHttpResult rGet(String url) {
        return receiver(METHOD_GET, url, configurer -> {
        });
    }

    public ReactiveHttpResult rPost(String url) {
        return receiver(METHOD_POST, url, configurer -> {
        });
    }

    public ReactiveHttpResult rPut(String url) {
        return receiver(METHOD_PUT, url, configurer -> {
        });
    }

    public ReactiveHttpResult rDelete(String url) {
        return receiver(METHOD_DELETE, url, configurer -> {
        });
    }

    public ReactiveHttpResult rOptions(String url) {
        return receiver(METHOD_OPTIONS, url, configurer -> {
        });
    }

    public ReactiveHttpResult rHead(String url) {
        return receiver(METHOD_HEAD, url, configurer -> {
        });
    }

    public ReactiveHttpResult rGet(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_GET, url, configurer);
    }

    public ReactiveHttpResult rPost(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_POST, url, configurer);
    }

    public ReactiveHttpResult rPut(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_PUT, url, configurer);
    }

    public ReactiveHttpResult rPatch(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_PATCH, url, configurer);
    }

    public ReactiveHttpResult rDelete(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_DELETE, url, configurer);
    }

    public ReactiveHttpResult rOptions(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_OPTIONS, url, configurer);
    }

    public ReactiveHttpResult rHead(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_HEAD, url, configurer);
    }

    /**
     * blocking request
     *
     * @param method       HTTP method
     * @param url          HTTP url
     * @param resultMapper result mapping
     */
    public <T> T request(String method, String url, Consumer<Configurer> configurer,
                         Function<HttpResult, T> resultMapper) {
        _assertState(StringUtils.isNotBlank(url), "URL should not be blank");
        _assertState(Objects.nonNull(configurer), "String should not be null");
        Configurer requestConfigurer = new Configurer()
            .method(method)
            .charset(defaultCharset)
            .url(url);
        for (Consumer<Configurer> requestInterceptor : requestInterceptors) {
            configurer = configurer.andThen(requestInterceptor);
        }
        configurer.accept(requestConfigurer);
        HttpResult httpResult = request(requestConfigurer);
        return resultMapper.apply(httpResult);
    }

    private HttpResult request(Configurer configurer) {
        HttpResult httpResult = new HttpResult();
        long start = System.currentTimeMillis();
        Mono<byte[]> byteMono = _request(configurer).responseSingle((httpClientResponse, byteBufMono) -> {
            httpResult.setHttpClientResponse(httpClientResponse);
            httpResult.init();
            return byteBufMono.asByteArray();
        });
        byte[] content = byteMono.block();
        if (log.isDebugEnabled()) {
            log.debug("response: cost=" + (System.currentTimeMillis() - start) + "ms, code="
                + httpResult.getStatusCode() + ", length=" + httpResult.contentLength);
        }
        httpResult.setContent(content);
        return httpResult;
    }

    /**
     * @param method HTTP method
     * @param url    HTTP url
     */
    public ReactiveHttpResult receiver(String method, String url, Consumer<Configurer> configurer) {
        _assertState(StringUtils.isNotBlank(url), "URL should not be blank");
        _assertState(Objects.nonNull(configurer), "String should not be null");
        Configurer requestConfigurer = new Configurer()
            .method(method)
            .charset(defaultCharset)
            .url(url);
        for (Consumer<Configurer> requestInterceptor : requestInterceptors) {
            configurer = configurer.andThen(requestInterceptor);
        }
        configurer.accept(requestConfigurer);
        ResponseReceiver<?> responseReceiver = _request(requestConfigurer);
        return new ReactiveHttpResult(responseReceiver);
    }

    private ResponseReceiver<?> _request(Configurer configurer) {
        URI uri = null;
        try {
            uri = new URIBuilder(configurer.url, configurer.charset).addParameters(configurer.params).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        HttpClient client = Optional.ofNullable(configurer.config)
            .map(requestConfig -> requestConfig.config(httpClient))
            .orElse(httpClient);
        if (CollectionUtils.isNotEmpty(configurer.headers)) {
            client = client.headers(entries -> configurer.headers.forEach(entries::add));
        }

        Configurer.Body body = Optional.ofNullable(configurer.bodyConfigurer)
            .map(bodyModelConsumer -> {
                Configurer.Payload payload = new Configurer.Payload();
                bodyModelConsumer.accept(payload);
                return payload.getBody();
            }).orElse(null);
        ;
        if (Objects.nonNull(body) && StringUtils.isNotBlank(body.contentType())) {
            client = client.headers(
                entries -> entries.add(HttpHeaderNames.CONTENT_TYPE.toString(), body.contentType()));
        }
        ResponseReceiver<?> responseReceiver;
        switch (configurer.method) {
            case "GET": {
                responseReceiver = client.get();
                break;
            }
            case "DELETE": {
                responseReceiver = client.delete();
                break;
            }
            case "HEAD": {
                responseReceiver = client.head();
                break;
            }
            case "OPTIONS": {
                responseReceiver = client.options();
                break;
            }
            case "POST": {
                responseReceiver = client.post();
                break;
            }
            case "PUT": {
                responseReceiver = client.put();
                break;
            }
            case "PATCH": {
                responseReceiver = client.patch();
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("not support http method [%s]", configurer.method));
            }
        }

        responseReceiver = responseReceiver.uri(uri);

        if (responseReceiver instanceof RequestSender && Objects.nonNull(body)) {
            RequestSender requestSender = (RequestSender) responseReceiver;
            responseReceiver = body.sender(requestSender, configurer.charset);
        }
        return responseReceiver;
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestConfig {

        public static RequestConfig DEFAULT_CONFIG = RequestConfig.builder().build();

        private final Duration connectTimeout;
        private final Duration readTimeout;
        private final HttpProtocol[] httpProtocols;
        private final boolean followRedirect;
        private final boolean keepalive;
        private final boolean proxyWithSystemProperties;
        private final boolean enableCompress;
        private final boolean enableRetry;
        private final boolean noSSL;
        private final ConnectionProvider connectionProvider;
        private final Function<HttpClient, HttpClient> customize;
        private final Consumer<Configurer> requestInterceptor;

        private HttpClient config(HttpClient httpClient) {
            HttpClient client = httpClient;
            client = client
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Long.valueOf(connectTimeout.toMillis()).intValue())
                .option(ChannelOption.SO_KEEPALIVE, keepalive)
                .protocol(httpProtocols)
                .keepAlive(keepalive)
                .followRedirect(followRedirect)
                .compress(enableCompress)
                .disableRetry(!enableRetry)
                .responseTimeout(readTimeout)
            ;
            if (proxyWithSystemProperties) {
                client = client.proxyWithSystemProperties();
            }
            if (noSSL) {
                client = client.noSSL();
            }
            // customize
            client = customize.apply(client);
            Objects.requireNonNull(client);

            return client;
        }

        private Builder copy() {
            return copy(this);
        }

        private static Builder copy(RequestConfig config) {
            Builder builder = RequestConfig.builder();
            builder.connectTimeout(config.connectTimeout);
            builder.readTimeout(config.readTimeout);
            builder.httpProtocol(config.httpProtocols);
            builder.followRedirect(config.followRedirect);
            builder.keepalive(config.keepalive);
            builder.proxyWithSystemProperties(config.proxyWithSystemProperties);
            builder.enableCompress(config.enableCompress);
            builder.enableRetry(config.enableRetry);
            builder.noSSL(config.noSSL);
            builder.connectionProvider(config.connectionProvider);
            builder.customize(config.customize);
            builder.requestInterceptor(config.requestInterceptor);
            return builder;
        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * Request configuration Builder, initial with default setting
         */
        public static class Builder {

            private Duration connectTimeout = CONNECT_TIMEOUT;
            private Duration readTimeout = READ_TIMEOUT;
            private HttpProtocol[] httpProtocols = {HttpProtocol.HTTP11, HttpProtocol.H2C};
            private boolean followRedirect = true;
            private boolean keepalive = true;
            private boolean proxyWithSystemProperties = true;
            private boolean enableCompress = true;
            private boolean enableRetry = true;
            private boolean noSSL = true;
            private ConnectionProvider connectionProvider = ConnectionProvider.builder("ReactiveHttpUtils")
                .maxConnections(MAX_TOTAL_CONNECTIONS)
                .pendingAcquireMaxCount(MAX_TOTAL_CONNECTIONS)
                .build();
            private Function<HttpClient, HttpClient> customize = _httpClient -> _httpClient;
            private Consumer<Configurer> requestInterceptor = configurer -> {
            };

            public Builder connectTimeout(Duration connectTimeout) {
                this.connectTimeout = connectTimeout;
                return this;
            }

            public Builder readTimeout(Duration readTimeout) {
                this.readTimeout = readTimeout;
                return this;
            }

            public Builder httpProtocol(HttpProtocol... httpProtocols) {
                this.httpProtocols = httpProtocols;
                return this;
            }

            public Builder followRedirect(boolean followRedirect) {
                this.followRedirect = followRedirect;
                return this;
            }

            public Builder keepalive(boolean keepalive) {
                this.keepalive = keepalive;
                return this;
            }

            public Builder proxyWithSystemProperties(boolean proxyWithSystemProperties) {
                this.proxyWithSystemProperties = proxyWithSystemProperties;
                return this;
            }

            public Builder enableCompress(boolean enableCompress) {
                this.enableCompress = enableCompress;
                return this;
            }

            public Builder enableRetry(boolean enableRetry) {
                this.enableRetry = enableRetry;
                return this;
            }

            public Builder noSSL(boolean noSSL) {
                this.noSSL = noSSL;
                return this;
            }

            public Builder connectionProvider(ConnectionProvider connectionProvider) {
                this.connectionProvider = connectionProvider;
                return this;
            }

            public Builder customize(Function<HttpClient, HttpClient> customize) {
                this.customize = this.customize.andThen(customize);
                return this;
            }

            public Builder requestInterceptor(Consumer<Configurer> interceptor) {
                this.requestInterceptor = this.requestInterceptor.andThen(interceptor);
                return this;
            }

            public RequestConfig build() {
                return new RequestConfig(
                    connectTimeout,
                    readTimeout,
                    httpProtocols,
                    followRedirect,
                    keepalive,
                    proxyWithSystemProperties,
                    enableCompress,
                    enableRetry,
                    noSSL,
                    connectionProvider,
                    customize,
                    requestInterceptor
                );
            }
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Configurer {

        private String method;

        private Charset charset;

        private String url;

        private final Map<String, List<String>> headers = new HashMap<>();

        private final List<Map.Entry<String, String>> params = new ArrayList<>();

        private Consumer<Payload> bodyConfigurer;

        private RequestConfig config;

        public Configurer method(String method) {
            this.method = method;
            return this;
        }

        public Configurer charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Configurer url(String url) {
            this.url = url;
            return this;
        }

        public Configurer header(String name, String value) {
            List<String> list = this.headers.get(name);
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                this.headers.put(name, list);
            }
            list.add(value);
            return this;
        }

        public Configurer headers(Map<String, String> headers) {
            headers.forEach(this::header);
            return this;
        }

        public Configurer param(String name, String value) {
            params.add(new AbstractMap.SimpleImmutableEntry<>(name, value));
            return this;
        }

        public Configurer params(Map<String, String> headers) {
            headers.forEach(this::param);
            return this;
        }

        public Configurer config(RequestConfig config) {
            this.config = config;
            return this;
        }

        public Configurer config(Consumer<RequestConfig.Builder> consumer) {
            RequestConfig.Builder copy;
            if (Objects.isNull(config)) {
                copy = RequestConfig.DEFAULT_CONFIG.copy();
            } else {
                copy = config.copy();
            }
            consumer.accept(copy);
            this.config = copy.build();
            return this;
        }

        public Configurer body(Consumer<Payload> configurer) {
            bodyConfigurer = configurer;
            return this;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Payload {

            private Body body;

            protected Body getBody() {
                return body;
            }

            public Payload raw(Consumer<Raw> configurer) {
                return type(Raw::new, configurer);
            }

            public Payload formData(Consumer<FormData> configurer) {
                return type(FormData::new, configurer);
            }

            public Payload binary(Consumer<Binary> configurer) {
                return type(Binary::new, configurer);
            }

            public Payload formUrlEncoded(Consumer<FormUrlEncoded> configurer) {
                return type(FormUrlEncoded::new, configurer);
            }

            public <T extends Body> Payload type(
                Supplier<T> buildable, Consumer<T> configurer) {
                Objects.requireNonNull(buildable);
                Objects.requireNonNull(configurer);
                if (Objects.isNull(body)) {
                    T built = buildable.get();
                    if (Objects.nonNull(built)) {
                        configurer.accept(built);
                        body = built;
                    }
                }
                return this;
            }
        }


        public static abstract class Body {

            protected void init() {

            }

            protected abstract String contentType();

            protected abstract ResponseReceiver<?> sender(RequestSender sender, Charset charset);
        }


        public static class Raw extends Body {

            public static final String TEXT_PLAIN = "text/plain";
            public static final String APPLICATION_JSON = "application/json";
            public static final String TEXT_HTML = "text/html";
            public static final String APPLICATION_XML = "text/xml";

            private String raw;

            private String contentType = TEXT_PLAIN;

            @Override
            protected String contentType() {
                return contentType;
            }

            @Override
            protected ResponseReceiver<?> sender(RequestSender sender, Charset charset) {
                return sender.send(Mono.justOrEmpty(raw)
                    .map(s -> s.getBytes(charset))
                    .map(Unpooled::wrappedBuffer));
            }

            public Raw text(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = TEXT_PLAIN;
                }
                return this;
            }

            public Raw json(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = APPLICATION_JSON;
                }
                return this;
            }

            public Raw html(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = TEXT_HTML;
                }
                return this;
            }

            public Raw xml(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = APPLICATION_XML;
                }
                return this;
            }
        }

        public static class Binary extends Body {

            private String contentType;

            private Supplier<byte[]> bytesSupplier;

            private Publisher<? extends ByteBuf> byteBufPublisher;

            @Override
            protected String contentType() {
                return contentType;
            }

            @Override
            protected ResponseReceiver<?> sender(RequestSender sender, Charset charset) {
                if (Objects.nonNull(byteBufPublisher)) {
                    return sender.send(byteBufPublisher);
                }
                if (Objects.nonNull(bytesSupplier)) {
                    return sender.send(Mono.fromSupplier(bytesSupplier)
                        .map(Unpooled::wrappedBuffer));
                }
                return sender;
            }

            public Binary publisher(Publisher<? extends ByteBuf> publisher) {
                if (Objects.isNull(byteBufPublisher)) {
                    this.byteBufPublisher = publisher;
                }
                return this;
            }

            public Binary publisher(Publisher<? extends ByteBuf> publisher, String contentType) {
                if (Objects.isNull(byteBufPublisher)) {
                    this.byteBufPublisher = publisher;
                }
                this.contentType = contentType;
                return this;
            }

            public Binary file(File file) {
                if (Objects.isNull(bytesSupplier)) {
                    bytesSupplier = () -> {
                        try {
                            return IOUtils.readFileToByteArray(file);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    };
                }
                return this;
            }

            public Binary bytes(byte[] bytes) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> bytes;
                }
                return this;
            }

            public Binary inputStream(InputStream ips) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> {
                        try {
                            return IOUtils.toByteArray(ips);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    };
                }
                return this;
            }

            public Binary file(File file, String contentType) {
                if (Objects.isNull(bytesSupplier)) {
                    bytesSupplier = () -> {
                        try {
                            return IOUtils.readFileToByteArray(file);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    };
                    this.contentType = contentType;
                }
                return this;
            }

            public Binary bytes(byte[] bytes, String contentType) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> bytes;
                    this.contentType = contentType;
                }
                return this;
            }

            public Binary inputStream(InputStream ips, String contentType) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> {
                        try {
                            return IOUtils.toByteArray(ips);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    };
                    this.contentType = contentType;
                }
                return this;
            }

        }

        public static class FormData extends Body {

            public static final String MULTIPART_FORM_DATA = "multipart/form-data";

            private final List<Function<HttpClientForm, HttpClientForm>> parts = new ArrayList<>();

            @Override
            protected String contentType() {
                return MULTIPART_FORM_DATA;
            }

            @Override
            protected ResponseReceiver<?> sender(RequestSender sender, Charset charset) {
                return sender.sendForm((httpClientRequest, httpClientForm) -> {
                    HttpClientForm form = httpClientForm
                        .multipart(true)
                        .charset(charset);
                    for (Function<HttpClientForm, HttpClientForm> part : parts) {
                        form = part.apply(form);
                    }
                });
            }

            public FormData text(String name, String value) {
                parts.add(form -> {
                    form.attr(name, value);
                    return form;
                });
                return this;
            }

            public FormData file(String name, File file) {
                return file(name, name, file, MULTIPART_FORM_DATA);
            }

            public FormData bytes(String name, byte[] bytes) {
                return bytes(name, name, bytes, MULTIPART_FORM_DATA);
            }

            public FormData inputStream(String name, InputStream ips) {
                return inputStream(name, name, ips, MULTIPART_FORM_DATA);
            }

            public FormData file(String name, String fileName, File file, String contentType) {
                parts.add(form -> {
                    form.file(name, fileName, file, contentType);
                    return form;
                });
                return this;
            }

            public FormData bytes(String name, String fileName, byte[] bytes, String contentType) {
                parts.add(form -> {
                    form.file(name, fileName, new ByteArrayInputStream(bytes), contentType);
                    return form;
                });
                return this;
            }

            public FormData inputStream(String name, String filename, InputStream ips, String contentType) {
                parts.add(form -> {
                    form.file(name, filename, ips, MULTIPART_FORM_DATA);
                    return form;
                });
                return this;
            }

        }

        public static class FormUrlEncoded extends Body {

            public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

            private final List<Map.Entry<String, String>> pairs = new ArrayList<>();

            @Override
            protected String contentType() {
                return APPLICATION_FORM_URLENCODED;
            }

            @Override
            protected ResponseReceiver<?> sender(RequestSender sender, Charset charset) {
                return sender.sendForm((httpClientRequest, form) -> {
                    form.charset(charset);
                    form.multipart(false);
                    for (Map.Entry<String, String> pair : pairs) {
                        form = form.attr(pair.getKey(), pair.getValue());
                    }
                });
            }

            public FormUrlEncoded text(String name, String value) {
                pairs.add(new AbstractMap.SimpleImmutableEntry<>(name, value));
                return this;
            }

        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class HttpResult {

        @Getter
        private int statusCode;
        @Setter(AccessLevel.PRIVATE)
        private HttpClientResponse httpClientResponse;
        private final Map<String, List<String>> headers = new HashMap<>();
        @Setter(AccessLevel.PRIVATE)
        private String contentType;
        @Setter(AccessLevel.PRIVATE)
        private String contentLength;
        @Getter
        private Charset charset = StandardCharsets.UTF_8;
        @Setter(AccessLevel.PRIVATE)
        private byte[] content;
        @Getter
        @Setter(AccessLevel.PRIVATE)
        private Map<CharSequence, Set<Cookie>> cookies;

        private void init() {
            this.statusCode = httpClientResponse.status().code();
            HttpHeaders entries = httpClientResponse.responseHeaders();
            setHeaders(entries);
            Optional.ofNullable(getHeader(HttpHeaderNames.CONTENT_TYPE.toString()))
                .ifPresent(contentType -> {
                    setContentType(contentType);
                    if (StringUtils.isNotBlank(contentType)) {
                        String[] split = contentType.split(";");
                        Arrays.stream(split).map(String::trim)
                            .filter(StringUtils::isNotBlank)
                            .filter(s -> StringUtils.startsWithIgnoreCase(s, "charset"))
                            .map(s -> StringUtils.removeStartIgnoreCase(s, "charset="))
                            .findFirst()
                            .ifPresent(this::setCharset);
                    }
                });
            Optional.ofNullable(getHeader(HttpHeaderNames.CONTENT_LENGTH.toString()))
                .ifPresent(HttpResult.this::setContentLength);
            cookies = httpClientResponse.cookies();
        }

        @Override
        public String toString() {
            return "HttpResult [statusCode=" + getStatusCode() + ", content-type=" + contentType + ", content-length="
                + contentLength + "]";
        }

        public boolean isOK() {
            return 200 <= this.statusCode && this.statusCode < 300;
        }

        private void setCharset(String charset) {
            try {
                this.charset = Charset.forName(charset);
            } catch (Exception ignored) {
            }
        }

        public Map<String, List<String>> getAllHeaders() {
            return headers;
        }

        public String getHeader(String name) {
            List<String> list = headers.get(name);
            if (CollectionUtils.isNotEmpty(list)) {
                return list.get(0);
            }
            return null;
        }

        public List<String> getHeaders(String name) {
            return headers.get(name);
        }

        private void setHeader(String name, String value) {
            List<String> list = this.headers.get(name);
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                this.headers.put(name, list);
            }
            list.add(value);
        }

        private void setHeader(Entry<String, String> header) {
            setHeader(header.getKey(), header.getValue());
        }

        private void setHeaders(HttpHeaders headers) {
            for (Entry<String, String> header : headers) {
                setHeader(header);
            }
        }

        public Set<Cookie> getCookie(String name) {
            return Optional.ofNullable(cookies).map(_map -> _map.get(name)).orElse(Collections.emptySet());
        }

        public byte[] content() {
            return wrap(bytes -> bytes);
        }

        public String str() {
            return wrap(bytes -> new String(bytes, charset));
        }

        public <T> T wrap(Function<byte[], T> wrapper) {
            Objects.requireNonNull(wrapper, "wrapper should not be null");
            return Optional.ofNullable(content).map(wrapper).orElse(null);
        }

    }

    public static class ReactiveHttpResult {

        private final ResponseReceiver<?> responseReceiver;

        private ReactiveHttpResult(ResponseReceiver<?> responseReceiver) {
            this.responseReceiver = responseReceiver;

        }

        public Mono<HttpClientResponse> response() {
            return responseReceiver.response().map(httpClientResponse1 -> httpClientResponse1);
        }

        public ByteBufFlux responseContent() {
            return responseReceiver.responseContent();
        }

        public <V> Mono<V> responseSingle(
            BiFunction<? super HttpClientResponse, ? super ByteBufMono, ? extends Mono<V>> receiver) {
            return responseReceiver.responseSingle(receiver);
        }

        public <V> Flux<V> response(
            BiFunction<? super HttpClientResponse, ? super ByteBufFlux, ? extends Publisher<V>> receiver) {
            return responseReceiver.response(receiver);
        }

        public <V> Flux<V> responseConnection(
            BiFunction<? super HttpClientResponse, ? super Connection, ? extends Publisher<V>> receiver) {
            return responseReceiver.responseConnection(receiver);
        }

    }

    private void _assertState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

}

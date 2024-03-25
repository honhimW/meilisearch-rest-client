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
import jakarta.annotation.Nullable;
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
 *     <caption>Properties default values</caption>
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

    /**
     * GET
     */
    public static final String METHOD_GET = "GET";
    /**
     * POST
     */
    public static final String METHOD_POST = "POST";
    /**
     * PUT
     */
    public static final String METHOD_PUT = "PUT";
    /**
     * PATCH
     */
    public static final String METHOD_PATCH = "PATCH";
    /**
     * DELETE
     */
    public static final String METHOD_DELETE = "DELETE";
    /**
     * OPTIONS
     */
    public static final String METHOD_OPTIONS = "OPTIONS";
    /**
     * HEAD
     */
    public static final String METHOD_HEAD = "HEAD";

    /**
     * Construct a {@link ReactiveHttpUtils} instance
     *
     * @return {@link ReactiveHttpUtils}
     */
    public static ReactiveHttpUtils getInstance() {
        ReactiveHttpUtils reactiveHttpUtils = new ReactiveHttpUtils();
        reactiveHttpUtils.init();
        return reactiveHttpUtils;
    }

    /**
     * Construct a {@link ReactiveHttpUtils} instance with custom {@link RequestConfig.Builder}
     *
     * @param configurer custom {@link RequestConfig.Builder}
     * @return {@link ReactiveHttpUtils}
     */
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

    /**
     * charset
     */
    private static final Charset defaultCharset = StandardCharsets.UTF_8;

    private HttpClient httpClient;

    private ConnectionProvider connectionProvider;

    private RequestConfig _defaultRequestConfig;

    @Getter
    private final List<Consumer<Configurer>> requestInterceptors = new ArrayList<>();

    /**
     * Register request interceptor, which will be called before each request
     *
     * @param interceptor interceptor
     * @return this
     */
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
        _defaultRequestConfig = requestConfig;
    }

    /**
     * Sends an HTTP GET request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult get(String url) {
        return request(METHOD_GET, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP POST request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult post(String url) {
        return request(METHOD_POST, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP PUT request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult put(String url) {
        return request(METHOD_PUT, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP PATCH request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult patch(String url) {
        return request(METHOD_PATCH, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP DELETE request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult delete(String url) {
        return request(METHOD_DELETE, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP OPTIONS request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult options(String url) {
        return request(METHOD_OPTIONS, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP HEAD request to the specified URL.
     *
     * @param url the URL to send the request to
     * @return the result of the HTTP request
     */
    public HttpResult head(String url) {
        return request(METHOD_HEAD, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP GET request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult get(String url, Consumer<Configurer> configurer) {
        return request(METHOD_GET, url, configurer);
    }

    /**
     * Sends an HTTP POST request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult post(String url, Consumer<Configurer> configurer) {
        return request(METHOD_POST, url, configurer);
    }

    /**
     * Sends an HTTP PUT request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult put(String url, Consumer<Configurer> configurer) {
        return request(METHOD_PUT, url, configurer);
    }

    /**
     * Sends an HTTP PATCH request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult patch(String url, Consumer<Configurer> configurer) {
        return request(METHOD_PATCH, url, configurer);
    }

    /**
     * Sends an HTTP DELETE request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult delete(String url, Consumer<Configurer> configurer) {
        return request(METHOD_DELETE, url, configurer);
    }

    /**
     * Sends an HTTP OPTIONS request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult options(String url, Consumer<Configurer> configurer) {
        return request(METHOD_OPTIONS, url, configurer);
    }

    /**
     * Sends an HTTP HEAD request to the specified URL, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult head(String url, Consumer<Configurer> configurer) {
        return request(METHOD_HEAD, url, configurer);
    }

    /**
     * Sends an HTTP request to the specified URL, with the specified configurer.
     *
     * @param method     the method of the request
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the result of the HTTP request
     */
    public HttpResult request(String method, String url, Consumer<Configurer> configurer) {
        return request(method, url, configurer, httpResult -> httpResult);
    }

    /**
     * Sends an HTTP GET request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rGet(String url) {
        return receiver(METHOD_GET, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP POST request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rPost(String url) {
        return receiver(METHOD_POST, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP PUT request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rPut(String url) {
        return receiver(METHOD_PUT, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP DELETE request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rDelete(String url) {
        return receiver(METHOD_DELETE, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP OPTIONS request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rOptions(String url) {
        return receiver(METHOD_OPTIONS, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP HEAD request to the specified URL in reactive.
     *
     * @param url the URL to send the request to
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rHead(String url) {
        return receiver(METHOD_HEAD, url, configurer -> {
        });
    }

    /**
     * Sends an HTTP GET request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rGet(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_GET, url, configurer);
    }

    /**
     * Sends an HTTP POST request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rPost(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_POST, url, configurer);
    }

    /**
     * Sends an HTTP PUT request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rPut(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_PUT, url, configurer);
    }

    /**
     * Sends an HTTP PATCH request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rPatch(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_PATCH, url, configurer);
    }

    /**
     * Sends an HTTP DELETE request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rDelete(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_DELETE, url, configurer);
    }

    /**
     * Sends an HTTP OPTIONS request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rOptions(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_OPTIONS, url, configurer);
    }

    /**
     * Sends an HTTP HEAD request to the specified URL in reactive, with the specified configurer.
     *
     * @param url        the URL to send the request to
     * @param configurer configurer of the request
     * @return the reactive result of the HTTP request
     */
    public ReactiveHttpResult rHead(String url, Consumer<Configurer> configurer) {
        return receiver(METHOD_HEAD, url, configurer);
    }

    /**
     * blocking request
     *
     * @param method       HTTP method
     * @param url          HTTP url
     * @param configurer   configurer of the request
     * @param resultMapper result mapping
     * @param <T>          result type
     * @return the result
     */
    public <T> T request(String method, String url, Consumer<Configurer> configurer,
                         Function<HttpResult, T> resultMapper) {
        ReactiveHttpResult receiver = receiver(method, url, configurer);
        Configurer _configurer = receiver.getConfigurer();
        HttpResult httpResult = receiver.toHttpResult();
        _configurer.resultHook.accept(httpResult);
        return resultMapper.apply(httpResult);
    }

    /**
     * Reactive request
     *
     * @param method     http method
     * @param url        http url
     * @param configurer configurer of the request
     * @return the reactive result
     */
    public ReactiveHttpResult receiver(String method, String url, Consumer<Configurer> configurer) {
        _assertState(StringUtils.isNotBlank(url), "URL should not be blank");
        _assertState(Objects.nonNull(configurer), "String should not be null");
        Configurer requestConfigurer = new Configurer(_defaultRequestConfig)
            .method(method)
            .charset(defaultCharset)
            .url(url);
        for (Consumer<Configurer> requestInterceptor : requestInterceptors) {
            configurer = configurer.andThen(requestInterceptor);
        }
        configurer.accept(requestConfigurer);
        ResponseReceiver<?> responseReceiver = _request(requestConfigurer);
        ReactiveHttpResult reactiveHttpResult = new ReactiveHttpResult(responseReceiver, requestConfigurer);
        requestConfigurer.reactiveResultHook.accept(reactiveHttpResult);
        return reactiveHttpResult;
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
            body.init();
            RequestSender requestSender = (RequestSender) responseReceiver;
            responseReceiver = body.sender(requestSender, configurer.charset);
        }
        return responseReceiver;
    }

    /**
     * Request config
     */
    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestConfig {

        /**
         * Default config
         */
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

        /**
         * Creates and returns a new instance of the Builder class.
         *
         * @return a new instance of the Builder class
         */
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

            /**
             * Configure the connect timeout
             *
             * @param connectTimeout connect timeout
             * @return this
             */
            public Builder connectTimeout(Duration connectTimeout) {
                this.connectTimeout = connectTimeout;
                return this;
            }

            /**
             * Configure the read timeout
             *
             * @param readTimeout read timeout
             * @return this
             */
            public Builder readTimeout(Duration readTimeout) {
                this.readTimeout = readTimeout;
                return this;
            }

            /**
             * Configure the http protocol
             *
             * @param httpProtocols http protocol
             * @return this
             */
            public Builder httpProtocol(HttpProtocol... httpProtocols) {
                this.httpProtocols = httpProtocols;
                return this;
            }

            /**
             * Configure the follow redirect
             *
             * @param followRedirect follow redirect
             * @return this
             */
            public Builder followRedirect(boolean followRedirect) {
                this.followRedirect = followRedirect;
                return this;
            }

            /**
             * Configure the keepalive
             *
             * @param keepalive keepalive
             * @return this
             */
            public Builder keepalive(boolean keepalive) {
                this.keepalive = keepalive;
                return this;
            }

            /**
             * Configure the proxy with system properties
             *
             * @param proxyWithSystemProperties proxy with system properties
             * @return this
             */
            public Builder proxyWithSystemProperties(boolean proxyWithSystemProperties) {
                this.proxyWithSystemProperties = proxyWithSystemProperties;
                return this;
            }

            /**
             * Configure the enable compress
             *
             * @param enableCompress enable compress
             * @return this
             */
            public Builder enableCompress(boolean enableCompress) {
                this.enableCompress = enableCompress;
                return this;
            }

            /**
             * Configure the enable retry
             *
             * @param enableRetry enable retry
             * @return this
             */
            public Builder enableRetry(boolean enableRetry) {
                this.enableRetry = enableRetry;
                return this;
            }

            /**
             * Configure the no ssl
             *
             * @param noSSL no ssl
             * @return this
             */
            public Builder noSSL(boolean noSSL) {
                this.noSSL = noSSL;
                return this;
            }

            /**
             * Configure the connection provider
             *
             * @param connectionProvider connection provider
             * @return this
             */
            public Builder connectionProvider(ConnectionProvider connectionProvider) {
                this.connectionProvider = connectionProvider;
                return this;
            }

            /**
             * Customize the http client
             *
             * @param customize customize
             * @return this
             */
            public Builder customize(Function<HttpClient, HttpClient> customize) {
                this.customize = this.customize.andThen(customize);
                return this;
            }

            /**
             * Configure the request interceptor
             *
             * @param interceptor request interceptor
             * @return this
             */
            public Builder requestInterceptor(Consumer<Configurer> interceptor) {
                this.requestInterceptor = this.requestInterceptor.andThen(interceptor);
                return this;
            }

            /**
             * Build the RequestConfig
             *
             * @return RequestConfig
             */
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

    /**
     * Configurer
     */
    public final static class Configurer {

        private final RequestConfig currentDefaultConfig;

        private Configurer(RequestConfig currentDefaultConfig) {
            this.currentDefaultConfig = currentDefaultConfig;
        }

        private String method;

        private Charset charset;

        private String url;

        private final Map<String, List<String>> headers = new HashMap<>();

        private final List<Map.Entry<String, String>> params = new ArrayList<>();

        private Consumer<Payload> bodyConfigurer;

        private RequestConfig config;

        private Consumer<HttpResult> resultHook = httpResult -> {
        };

        private Consumer<ReactiveHttpResult> reactiveResultHook = reactiveHttpResult -> {
        };

        /**
         * Configure the method
         *
         * @param method http method
         * @return this
         */
        public Configurer method(String method) {
            this.method = method;
            return this;
        }

        /**
         * Configure the charset
         *
         * @param charset charset
         * @return this
         */
        public Configurer charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        /**
         * Configure the url
         *
         * @param url url
         * @return this
         */
        public Configurer url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Add header pair
         *
         * @param name  header name
         * @param value header value
         * @return this
         */
        public Configurer header(String name, String value) {
            List<String> list = this.headers.get(name);
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                this.headers.put(name, list);
            }
            list.add(value);
            return this;
        }

        /**
         * Override headers
         *
         * @param headers headers
         * @return this
         */
        public Configurer headers(Map<String, String> headers) {
            headers.forEach(this::header);
            return this;
        }

        /**
         * Add query parameter pair
         *
         * @param name  parameter name
         * @param value parameter value
         * @return this
         */
        public Configurer param(String name, String value) {
            params.add(new AbstractMap.SimpleImmutableEntry<>(name, value));
            return this;
        }

        /**
         * Override query parameters
         *
         * @param params parameters
         * @return this
         */
        public Configurer params(Map<String, String> params) {
            params.forEach(this::param);
            return this;
        }

        /**
         * Configure request config
         *
         * @param config request config
         * @return this
         */
        public Configurer config(RequestConfig config) {
            this.config = config;
            return this;
        }

        /**
         * Configure request config
         *
         * @param consumer request config
         * @return this
         */
        public Configurer config(Consumer<RequestConfig.Builder> consumer) {
            RequestConfig.Builder copy;
            if (Objects.isNull(config)) {
                copy = currentDefaultConfig.copy();
            } else {
                copy = config.copy();
            }
            consumer.accept(copy);
            this.config = copy.build();
            return this;
        }

        /**
         * Sets the body configurer for the function.
         *
         * @param configurer the consumer to configure the payload
         * @return the updated Configurer object
         */
        public Configurer body(Consumer<Payload> configurer) {
            bodyConfigurer = configurer;
            return this;
        }

        /**
         * Invoke after request process
         *
         * @param resultHook hook
         * @return this
         */
        public Configurer resultHook(Consumer<HttpResult> resultHook) {
            this.resultHook = this.resultHook.andThen(resultHook);
            return this;
        }

        /**
         * Invoke after request process
         *
         * @param resultHook hook
         * @return this
         */
        public Configurer reactiveResultHook(Consumer<ReactiveHttpResult> resultHook) {
            this.reactiveResultHook = this.reactiveResultHook.andThen(resultHook);
            return this;
        }

        /**
         * Get current method
         *
         * @return http method
         */
        public String method() {
            return this.method;
        }

        /**
         * Get current charset
         *
         * @return charset
         */
        public Charset charset() {
            return this.charset;
        }

        /**
         * Get current url
         *
         * @return http url
         */
        public String url() {
            return this.url;
        }

        /**
         * Get current parameters
         *
         * @return parameters
         */
        public List<Map.Entry<String, String>> params() {
            return this.params;
        }

        /**
         * Get current headers
         *
         * @return headers
         */
        public Map<String, List<String>> headers() {
            return this.headers;
        }

        /**
         * HTTP request payload entity
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Payload {

            private Body body;

            /**
             * Get configured body
             * @return configured body
             */
            protected Body getBody() {
                return body;
            }

            /**
             * Raw type payload
             *
             * @param configurer raw configurer
             * @return this
             */
            public Payload raw(Consumer<Raw> configurer) {
                return type(Raw::new, configurer);
            }

            /**
             * Form Data payload
             *
             * @param configurer form-data configurer
             * @return this
             */
            public Payload formData(Consumer<FormData> configurer) {
                return type(FormData::new, configurer);
            }

            /**
             * Binary payload
             *
             * @param configurer binary configurer
             * @return this
             */
            public Payload binary(Consumer<Binary> configurer) {
                return type(Binary::new, configurer);
            }

            /**
             * Form Url Encoded payload
             *
             * @param configurer form-url-encoded configurer
             * @return this
             */
            public Payload formUrlEncoded(Consumer<FormUrlEncoded> configurer) {
                return type(FormUrlEncoded::new, configurer);
            }

            /**
             * Other type payload
             *
             * @param buildable  body supplier
             * @param configurer body configurer
             * @param <T>        Body type
             * @return this
             */
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

        /**
         * Payload body abstract class
         */
        public static abstract class Body {

            /**
             * Body initialize
             */
            protected void init() {

            }

            /**
             * Body payload content-type define
             *
             * @return content-type
             */
            protected abstract String contentType();

            /**
             * Do on sender.
             *
             * @param sender  sender
             * @param charset charset
             * @return response receiver
             */
            protected abstract ResponseReceiver<?> sender(RequestSender sender, Charset charset);
        }

        /**
         * Raw type support
         */
        public static class Raw extends Body {

            /**
             * text/plain content-type
             */
            public static final String TEXT_PLAIN = "text/plain";
            /**
             * application/json content-type
             */
            public static final String APPLICATION_JSON = "application/json";
            /**
             * text/html content-type
             */
            public static final String TEXT_HTML = "text/html";
            /**
             * text/xml content-type
             */
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

            /**
             * plain text raw request
             *
             * @param text plain
             * @return this
             */
            public Raw text(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = TEXT_PLAIN;
                }
                return this;
            }

            /**
             * json raw request
             *
             * @param text json format string
             * @return this
             */
            public Raw json(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = APPLICATION_JSON;
                }
                return this;
            }

            /**
             * html raw request
             *
             * @param text html
             * @return this
             */
            public Raw html(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = TEXT_HTML;
                }
                return this;
            }

            /**
             * xml raw request
             *
             * @param text xml
             * @return this
             */
            public Raw xml(String text) {
                if (Objects.isNull(raw)) {
                    this.raw = text;
                    this.contentType = APPLICATION_XML;
                }
                return this;
            }
        }

        /**
         * Binary payload body
         */
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

            /**
             * Publisher as data input
             *
             * @param publisher data
             * @return this
             */
            public Binary publisher(Publisher<? extends ByteBuf> publisher) {
                if (Objects.isNull(byteBufPublisher)) {
                    this.byteBufPublisher = publisher;
                }
                return this;
            }

            /**
             * Publisher as data input with specific content-type
             *
             * @param publisher   data
             * @param contentType content-type
             * @return this
             */
            public Binary publisher(Publisher<? extends ByteBuf> publisher, String contentType) {
                if (Objects.isNull(byteBufPublisher)) {
                    this.byteBufPublisher = publisher;
                }
                this.contentType = contentType;
                return this;
            }

            /**
             * File as data input
             *
             * @param file file
             * @return this
             */
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

            /**
             * Bytes array as data input
             *
             * @param bytes data
             * @return this
             */
            public Binary bytes(byte[] bytes) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> bytes;
                }
                return this;
            }

            /**
             * InputStream as data input
             *
             * @param ips data
             * @return this
             */
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

            /**
             * File as data input with specific content-type
             *
             * @param file        data
             * @param contentType content-type
             * @return this
             */
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

            /**
             * Bytes array as data input with specific content-type
             *
             * @param bytes       data
             * @param contentType content-type
             * @return this
             */
            public Binary bytes(byte[] bytes, String contentType) {
                if (Objects.isNull(bytesSupplier)) {
                    this.bytesSupplier = () -> bytes;
                    this.contentType = contentType;
                }
                return this;
            }

            /**
             * InputStream as data input with specific content-type
             *
             * @param ips         data
             * @param contentType content-type
             * @return this
             */
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

        /**
         * FormData payload body
         */
        public static class FormData extends Body {

            /**
             * multipart/form-data content-type
             */
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

            /**
             * Text data
             *
             * @param name  property name
             * @param value property value
             * @return this
             */
            public FormData text(String name, String value) {
                parts.add(form -> {
                    form.attr(name, value);
                    return form;
                });
                return this;
            }

            /**
             * File data
             *
             * @param name property name
             * @param file property content
             * @return this
             */
            public FormData file(String name, File file) {
                return file(name, name, file, MULTIPART_FORM_DATA);
            }

            /**
             * Bytes array data
             *
             * @param name  property name
             * @param bytes property content
             * @return this
             */
            public FormData bytes(String name, byte[] bytes) {
                return bytes(name, name, bytes, MULTIPART_FORM_DATA);
            }

            /**
             * InputStream data
             *
             * @param name property name
             * @param ips  property content
             * @return this
             */
            public FormData inputStream(String name, InputStream ips) {
                return inputStream(name, name, ips, MULTIPART_FORM_DATA);
            }

            /**
             * File data
             *
             * @param name        property name
             * @param fileName    file name
             * @param file        property content
             * @param contentType file content-type
             * @return this
             */
            public FormData file(String name, String fileName, File file, String contentType) {
                parts.add(form -> {
                    form.file(name, fileName, file, contentType);
                    return form;
                });
                return this;
            }

            /**
             * Bytes array data
             *
             * @param name        property name
             * @param fileName    file name
             * @param bytes       property content
             * @param contentType file content-type
             * @return this
             */
            public FormData bytes(String name, String fileName, byte[] bytes, String contentType) {
                parts.add(form -> {
                    form.file(name, fileName, new ByteArrayInputStream(bytes), contentType);
                    return form;
                });
                return this;
            }

            /**
             * InputStream data
             *
             * @param name        property name
             * @param fileName    file name
             * @param ips         property content
             * @param contentType file content-type
             * @return this
             */
            public FormData inputStream(String name, String fileName, InputStream ips, String contentType) {
                parts.add(form -> {
                    form.file(name, fileName, ips, MULTIPART_FORM_DATA);
                    return form;
                });
                return this;
            }

        }

        /**
         * Form url encoded payload body
         */
        public static class FormUrlEncoded extends Body {

            /**
             * application/x-www-form-urlencoded content-type
             */
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

            /**
             * Form url encoded property
             *
             * @param name  property name
             * @param value property value
             * @return this
             */
            public FormUrlEncoded text(String name, String value) {
                pairs.add(new AbstractMap.SimpleImmutableEntry<>(name, value));
                return this;
            }

        }
    }

    /**
     * Blocking http result keeper
     */
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

        /**
         * Is OK
         *
         * @return status code is 2XX
         */
        public boolean isOK() {
            return 200 <= this.statusCode && this.statusCode < 300;
        }

        private void setCharset(String charset) {
            try {
                this.charset = Charset.forName(charset);
            } catch (Exception ignored) {
            }
        }

        /**
         * Get all http response headers
         *
         * @return response headers multi value map
         */
        public Map<String, List<String>> getAllHeaders() {
            return headers;
        }

        /**
         * Get first response header by name
         *
         * @param name header name
         * @return first result
         */
        @Nullable
        public String getHeader(String name) {
            List<String> list = getAllHeaders().get(name);
            if (CollectionUtils.isNotEmpty(list)) {
                return list.get(0);
            }
            return null;
        }

        /**
         * Get all headers by name
         *
         * @param name header name
         * @return header values with name
         */
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

        /**
         * Get cookies by name
         *
         * @param name cookie name
         * @return cookies
         */
        public Set<Cookie> getCookie(String name) {
            return Optional.ofNullable(cookies).map(_map -> _map.get(name)).orElse(Collections.emptySet());
        }

        /**
         * Http response content
         *
         * @return content payload
         */
        public byte[] content() {
            return wrap(bytes -> bytes);
        }

        /**
         * Encode response content with charset
         *
         * @return plain response content
         */
        public String str() {
            return wrap(bytes -> new String(bytes, charset));
        }

        /**
         * Apply data resolver
         *
         * @param wrapper content resolver
         * @param <T>     resolved type
         * @return resolved value
         */
        public <T> T wrap(Function<byte[], T> wrapper) {
            Objects.requireNonNull(wrapper, "wrapper should not be null");
            return Optional.ofNullable(content).map(wrapper).orElse(null);
        }

    }

    /**
     * Reactive http result keeper
     */
    public static class ReactiveHttpResult {

        private final ResponseReceiver<?> responseReceiver;
        @Getter
        private final Configurer configurer;

        private ReactiveHttpResult(ResponseReceiver<?> responseReceiver, Configurer configurer) {
            this.responseReceiver = responseReceiver;
            this.configurer = configurer;
        }

        /**
         * Return the response status and headers as HttpClientResponse
         * Will automatically close the response if necessary.
         * Note: Will automatically close low-level network connection after returned Mono terminates or is being cancelled.
         *
         * @return the response status and headers as HttpClientResponse
         */
        public Mono<HttpClientResponse> response() {
            return responseReceiver.response();
        }

        /**
         * Return the response body chunks as ByteBufFlux.
         * Will automatically close the response if necessary after the returned Flux terminates or is being cancelled.
         *
         * @return the response body chunks as ByteBufFlux.
         */
        public ByteBufFlux responseContent() {
            return responseReceiver.responseContent();
        }

        /**
         * Wrap all response data in to a mono.
         *
         * @param receiver response receiver
         * @param <V>      forwarding type
         * @return a Mono forwarding the returned Mono result
         */
        public <V> Mono<V> responseSingle(
            BiFunction<? super HttpClientResponse, ? super ByteBufMono, ? extends Mono<V>> receiver) {
            return responseReceiver.responseSingle(receiver);
        }

        /**
         * Extract a response flux from the given HttpClientResponse and body ByteBufFlux.
         * Will automatically close the response if necessary after the returned Flux terminates or is being cancelled.
         *
         * @param receiver extracting receiver
         * @param <V>      forwarding type
         * @return a Flux forwarding the returned Publisher sequence
         */
        public <V> Flux<V> response(
            BiFunction<? super HttpClientResponse, ? super ByteBufFlux, ? extends Publisher<V>> receiver) {
            return responseReceiver.response(receiver);
        }

        /**
         * Extract a response flux from the given HttpClientResponse and underlying Connection.
         * The connection will not automatically Connection.dispose() and manual interaction with this method might be necessary if the remote never terminates itself.
         *
         * @param receiver extracting receiver
         * @param <V>      fowarding type
         * @return a Flux forwarding the returned Publisher sequence
         */
        public <V> Flux<V> responseConnection(
            BiFunction<? super HttpClientResponse, ? super Connection, ? extends Publisher<V>> receiver) {
            return responseReceiver.responseConnection(receiver);
        }

        /**
         * Will blocking process a real http request with each-invoked.
         *
         * @return blocking http result
         */
        public HttpResult toHttpResult() {
            HttpResult httpResult = new HttpResult();
            long start = System.currentTimeMillis();
            Mono<byte[]> byteMono = responseReceiver.responseSingle((httpClientResponse, byteBufMono) -> {
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

    }

    private void _assertState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Resolve HttpClientResponse charset
     *
     * @param httpClientResponse httpClientResponse
     * @return charset
     */
    public static Charset getCharset(HttpClientResponse httpClientResponse) {
        HttpHeaders entries = httpClientResponse.responseHeaders();
        return Optional.ofNullable(entries.get(HttpHeaderNames.CONTENT_TYPE.toString()))
            .flatMap(contentType -> {
                String[] split = contentType.split(";");
                return Arrays.stream(split).map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .filter(s -> StringUtils.startsWithIgnoreCase(s, "charset"))
                    .map(s -> StringUtils.removeStartIgnoreCase(s, "charset="))
                    .findFirst()
                    .map(Charset::forName);
            })
            .orElse(StandardCharsets.UTF_8);
    }

}

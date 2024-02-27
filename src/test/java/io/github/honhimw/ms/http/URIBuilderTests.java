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

import io.github.honhimw.ms.support.StringUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class URIBuilderTests {

    @Test
    void create() {
        URIBuilder uriBuilder = URIBuilder.create();
        assert uriBuilder.getPort() == -1;
    }

    @Test
    @SneakyThrows
    void from() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        URI uri = uriBuilder.build();
        assert uri.getScheme().equals("http");
        assert uri.getHost().equals("localhost");
        assert uri.getPort() == 8080;

        uriBuilder = URIBuilder.from(URI.create("http://localhost:8080"));
        uri = uriBuilder.build();
        assert uri.getScheme().equals("http");
        assert uri.getHost().equals("localhost");
        assert uri.getPort() == 8080;
    }

    @Test
    void setCharset() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        uriBuilder.setCharset(StandardCharsets.UTF_8);
        assert uriBuilder.getCharset() == StandardCharsets.UTF_8;
    }

    @Test
    @SneakyThrows
    void build() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        URI uri = uriBuilder.build();
        assert uri.getScheme().equals("http");
        assert uri.getHost().equals("localhost");
        assert uri.getPort() == 8080;
    }

    @Test
    @SneakyThrows
    void setScheme() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        URI uri = uriBuilder.setScheme("https").build();
        assert uri.getScheme().equals("https");
        assert uri.getHost().equals("localhost");
        assert uri.getPort() == 8080;
    }

    @Test
    @SneakyThrows
    void setUserInfo() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        String userInfo = "honhimw";
        URI uri = uriBuilder.setUserInfo(userInfo).build();
        assert uri.getUserInfo().equals(userInfo);
    }

    @SneakyThrows
    @Test
    void setHost() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        String host = "www.meilisearch.com";
        URI uri = uriBuilder.setHost(host).build();
        assert uri.getHost().equals(host);
    }

    @SneakyThrows
    @Test
    void setPort() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        int port = 7700;
        URI uri = uriBuilder.setPort(port).build();
        assert uri.getPort() == port;
    }

    @SneakyThrows
    @Test
    void setPath() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        URI uri = uriBuilder.setPath("/indexes/1").build();
        assert uri.getPath().equals("/indexes/1");
    }

    @SneakyThrows
    @Test
    void setPathSegments() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        URI uri = uriBuilder.setPathSegments("indexes", "1").build();
        assert uri.getPath().equals("/indexes/1");
    }

    @SneakyThrows
    @Test
    void removeQuery() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        URI uri = uriBuilder.build();
        assert uri.getQuery().equals("foo=bar");
        uri = uriBuilder.removeQuery().build();
        assert StringUtils.isBlank(uri.getQuery());
    }

    @SneakyThrows
    @Test
    void setParameters() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        URI uri = uriBuilder.setParameters(new AbstractMap.SimpleEntry<>("foo", "bar")).build();
        assert uri.getQuery().equals("foo=bar");
    }

    @SneakyThrows
    @Test
    void addParameters() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        List<Map.Entry<String, String>> nvps = new ArrayList<>();
        nvps.add(new AbstractMap.SimpleEntry<>("foo", "bar"));
        nvps.add(new AbstractMap.SimpleEntry<>("foo1", "bar1"));
        URI uri = uriBuilder.addParameters(nvps).build();
        assert uri.getQuery().equals("foo=bar&foo1=bar1");
    }

    @SneakyThrows
    @Test
    void addParameter() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        URI uri = uriBuilder.addParameter("foo1", "bar1").build();
        assert uri.getQuery().equals("foo=bar&foo1=bar1");
    }

    @SneakyThrows
    @Test
    void setParameter() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        URI uri = uriBuilder.setParameter("foo", "bar1").build();
        assert uri.getQuery().equals("foo=bar1");
    }

    @SneakyThrows
    @Test
    void clearParameters() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        URI uri = uriBuilder.clearParameters().build();
        assert StringUtils.isBlank(uri.getQuery());
    }

    @SneakyThrows
    @Test
    void setCustomQuery() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        String query = "foo=bar";
        URI uri = uriBuilder.setCustomQuery(query).build();
        assert uri.getQuery().equals(query);
    }

    @SneakyThrows
    @Test
    void setFragment() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        String fragment = "fragment";
        URI uri = uriBuilder.setFragment(fragment).build();
        assert uri.getFragment().equals(fragment);
    }

    @SneakyThrows
    @Test
    void isAbsolute() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        URI uri = uriBuilder.build();
        assert uriBuilder.isAbsolute();
        assert uri.isAbsolute();
        uri = uriBuilder.setScheme(null).build();
        assert !uriBuilder.isAbsolute();
        assert !uri.isAbsolute();
    }

    @SneakyThrows
    @Test
    void isOpaque() {
        URIBuilder uriBuilder = URIBuilder.from("localhost:8080");
        assert uriBuilder.isOpaque();
        URI uri = uriBuilder.build();
        assert uri.isOpaque();
        uri = uriBuilder.setPath("/hello").build();
        assert !uriBuilder.isOpaque();
        assert !uri.isOpaque();
    }

    @SneakyThrows
    @Test
    void isPathEmpty() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080");
        assert uriBuilder.isPathEmpty();
    }

    @Test
    void getPathSegments() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        List<String> pathSegments = uriBuilder.getPathSegments();
        assert pathSegments.size() == 1;
        assert pathSegments.get(0).equals("hello");
    }


    @Test
    void isQueryEmpty() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello");
        assert uriBuilder.isQueryEmpty();
    }

    @Test
    void getQueryParams() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        List<Map.Entry<String, String>> queryParams = uriBuilder.getQueryParams();
        assert queryParams.size() == 1;
        assert queryParams.get(0).getKey().equals("foo");
        assert queryParams.get(0).getValue().equals("bar");
    }

    @Test
    void testToString() {
        URIBuilder uriBuilder = URIBuilder.from("http://localhost:8080/hello?foo=bar");
        assert uriBuilder.toString().equals("http://localhost:8080/hello?foo=bar");
    }

}
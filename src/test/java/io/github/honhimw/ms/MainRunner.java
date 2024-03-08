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
import io.github.honhimw.ms.json.ComplexTypeRef;
import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.SearchResponse;
import lombok.Cleanup;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public class MainRunner {

    public static void main(String[] args) {

        JsonHandler jsonHandler = new JacksonJsonHandler();
        SearchResponse<String> strings = jsonHandler.fromJson("{\"hits\": [\"hello\", \"world\"]}", type(new TypeRef<String>() {
        }));
        List<String> hits = strings.getHits();
        for (String string : hits) {
            System.out.println(string);
        }
    }

    public static <T> TypeRef<SearchResponse<T>> type(TypeRef<T> typeRef) {
        return new ComplexTypeRef<SearchResponse<T>>(typeRef) {
        };
    }

    public static void reactive() {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        @Cleanup
        ReactiveMSearchClient client = ReactiveMSearchClient.create(builder -> builder
            .enableSSL(false)                    // true: https, false: http
            .host("{{meilisearch-server-host}}") // server host
            .port(7700)                          // server port
            .jsonHandler(jsonHandler)
            .httpClient(ReactiveHttpUtils.getInstance(http -> http.readTimeout(Duration.ofMillis(100)))));
        String indexUid = "movies";
        Mono<SearchResponse<Movie>> searchResponse = client.indexes(indexes -> indexes
            .search(indexUid, search -> search
                .find("hello world", Movie.class)));
        List<Movie> hits = searchResponse.block().getHits();
        // or
        List<Movie> hits2 = client.indexes(indexes -> indexes
            .search(indexUid, Movie.class, search -> search
                .find(q -> q
                    .q("hello world")
                    .limit(1)
                )
                .map(SearchResponse::getHits)
            )
        ).block();
    }

    public static void blocking() {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        @Cleanup
        MSearchClient client = MSearchClient.create(builder -> builder
            .enableSSL(false)                    // true: https, false: http
            .host("{{meilisearch-server-host}}") // server host
            .port(7700)                          // server port
            .jsonHandler(jsonHandler)
            .httpClient(ReactiveHttpUtils.getInstance(http -> http.readTimeout(Duration.ofMillis(100)))));
        String indexUid = "movies";
        SearchResponse<Movie> searchResponse = client.indexes(indexes -> indexes
            .search(indexUid, search -> search
                .find("hello world", Movie.class)));
        List<Movie> hits = searchResponse.getHits();
        // or
        List<Movie> hits2 = client.indexes(indexes -> indexes
            .search(indexUid, Movie.class, search -> search
                .find(q -> q
                    .q("hello world")
                    .limit(1)
                )
                .getHits()
            )
        );
    }

}

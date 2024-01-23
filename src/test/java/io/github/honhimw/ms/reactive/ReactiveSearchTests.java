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

package io.github.honhimw.ms.reactive;

import io.github.honhimw.ms.Movie;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.api.reactive.ReactiveSearch;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.SearchResponse;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
public class ReactiveSearchTests extends ReactiveDocumentsTests {

    protected ReactiveIndexes indexes;

    protected String index = "movie_test";

    protected ReactiveSearch search;

    @BeforeEach
    void initIndexes() {
        super.initIndexes();
        indexes = reactiveClient.indexes();
        search = indexes.search(index);
    }

    @Order(103)
    @Test
    void search() {
        StepVerifier.create(search.find("2"))
            .assertNext(searchResponse -> {
                log.info(jsonHandler.toJson(searchResponse.getHits()));
                assert !searchResponse.getHits().isEmpty();
            })
            .verifyComplete();

        Mono<SearchResponse<Map<String, Object>>> indexes2 = reactiveClient.indexes(indexes1 -> indexes1.search("movies", reactiveSearch -> reactiveSearch.find("hello world")));
        List<Map<String, Object>> hits = indexes2.block().getHits();
    }

    @Order(104)
    @Test
    void search2() {
        Mono<List<Movie>> mono = search.find("2", Movie.class)
            .map(SearchResponse::getHits);
        StepVerifier.create(mono)
            .assertNext(movies1 -> {
                for (Movie movie : movies1) {
                    log.info(jsonHandler.toJson(movie));
                }
            })
            .verifyComplete();
    }

    @Order(105)
    @Test
    void search3() {
        Mono<List<Movie>> mono = indexes.search(index, new TypeRef<Movie>() {
            }).find("2")
            .map(SearchResponse::getHits);
        StepVerifier.create(mono)
            .assertNext(movies1 -> {
                for (Movie movie : movies1) {
                    log.info(jsonHandler.toJson(movie));
                }
            })
            .verifyComplete();
    }

}

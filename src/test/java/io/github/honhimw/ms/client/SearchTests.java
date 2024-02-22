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

package io.github.honhimw.ms.client;

import io.github.honhimw.ms.Movie;
import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.Search;
import io.github.honhimw.ms.api.TypedDetailsSearch;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.api.reactive.ReactiveSearch;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.CollectionUtils;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchTests extends TestBase {

    protected ReactiveIndexes reactiveIndexes;
    protected ReactiveSearch reactiveSearch;
    protected Indexes blockingIndexes;
    protected Search blockingSearch;

    @BeforeEach
    void initIndexes() {
        prepareData();
        reactiveIndexes = reactiveClient.indexes();
        reactiveSearch = reactiveIndexes.search(INDEX);
        blockingIndexes = blockingClient.indexes();
        blockingSearch = blockingIndexes.search(INDEX);
    }

    @Order(1)
    @Test
    void search() {
        StepVerifier.create(reactiveSearch.find("2"))
            .assertNext(searchResponse -> {
                log.info(jsonHandler.toJson(searchResponse.getHits()));
                assert !searchResponse.getHits().isEmpty();
            })
            .verifyComplete();
    }

    @Order(2)
    @Test
    void search2() {
        Mono<List<Movie>> mono = reactiveSearch.find("2", Movie.class)
            .map(SearchResponse::getHits);
        StepVerifier.create(mono)
            .assertNext(movies1 -> {
                for (Movie movie : movies1) {
                    log.info(jsonHandler.toJson(movie));
                }
            })
            .verifyComplete();
    }

    @Order(3)
    @Test
    void search3() {
        Mono<List<Movie>> mono = reactiveIndexes.search(INDEX, new TypeRef<Movie>() {
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

    @Order(4)
    @Test
    void searchWithScore() {
        ExperimentalFeatures configure = blockingClient.experimentalFeatures().configure(builder -> builder.scoreDetails(true));
        assert configure.getScoreDetails();

        TaskInfo update = blockingIndexes.settings(INDEX).filterableAttributes().update(toList("title", "genres", "director"));
        await(update);

        TypedDetailsSearch<Movie> movieTypedDetailsSearch = blockingIndexes.searchWithDetails(INDEX, Movie.class);
        SearchDetailsResponse<Movie> response = movieTypedDetailsSearch.find(builder -> builder
            .q("2")
            .showRankingScore(true)
            .showRankingScoreDetails(true)
            .facets(toList("title", "genres", "director"))
        );
        List<HitDetails<Movie>> hits = response.getHits();
        assert CollectionUtils.isNotEmpty(hits);

        Double rankingScore = hits.get(0).getDetails().get_rankingScore();
        assert 0 <= rankingScore && rankingScore <= 1;

    }

    @Order(5)
    @Test
    void searchWithHighlight() {
        ExperimentalFeatures configure = blockingClient.experimentalFeatures().configure(builder -> builder.scoreDetails(true));
        assert configure.getScoreDetails();

        TaskInfo update = blockingIndexes.settings(INDEX).filterableAttributes().update(toList("title", "genres", "director"));
        await(update);

        TypedDetailsSearch<Movie> movieTypedDetailsSearch = blockingIndexes.searchWithDetails(INDEX, Movie.class);
        SearchDetailsResponse<Movie> response = movieTypedDetailsSearch.find(builder -> builder
            .q("2")
            .highlightPreTag("<em>")
            .highlightPostTag("</em>")
            .attributesToHighlight(toList("id", "title"))
        );
        List<HitDetails<Movie>> hits = response.getHits();
        assert Objects.nonNull(hits.get(0).getDetails().get_formatted());
    }

    @AfterEach
    void resetSetting() {
        TaskInfo reset = blockingIndexes.settings(INDEX).reset();
        await(reset);
    }

}

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
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.CollectionUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchTests extends TestBase {

    protected Indexes indexes;
    protected Search search;

    @BeforeEach
    void initIndexes() {
        prepareData();
        indexes = blockingClient.indexes();
        search = indexes.search(INDEX);
    }

    @Order(1)
    @Test
    void search() {
        assert !search.find("2").getHits().isEmpty();
    }

    @Order(1)
    @Test
    void search2() {
        SearchResponse<Movie> movieSearchResponse = search.find("2", Movie.class);
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    @Order(1)
    @Test
    void search3() {
        SearchResponse<Movie> movieSearchResponse = search.find("2", new TypeRef<Movie>() {
        });
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    @Order(1)
    @Test
    void search4() {
        SearchRequest searchRequest = SearchRequest.builder().q("2").build();
        SearchResponse<Map<String, Object>> movieSearchResponse = search.find(searchRequest);
        assert (Integer) movieSearchResponse.getHits().get(0).get("id") > 0;
    }

    @Order(1)
    @Test
    void search5() {
        SearchResponse<Map<String, Object>> movieSearchResponse = search.find(builder -> builder.q("2"));
        assert (Integer) movieSearchResponse.getHits().get(0).get("id") > 0;
    }

    @Order(1)
    @Test
    void search6() {
        SearchRequest searchRequest = SearchRequest.builder().q("2").build();
        SearchResponse<Movie> movieSearchResponse = search.find(searchRequest, Movie.class);
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    @Order(1)
    @Test
    void search7() {
        SearchRequest searchRequest = SearchRequest.builder().q("2").build();
        SearchResponse<Movie> movieSearchResponse = search.find(searchRequest, new TypeRef<Movie>() {
        });
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    @Order(1)
    @Test
    void search8() {
        SearchResponse<Movie> movieSearchResponse = search.find(builder -> builder.q("2"), new TypeRef<Movie>() {
        });
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    @Order(1)
    @Test
    void search9() {
        SearchResponse<Movie> movieSearchResponse = search.find(builder -> builder.q("2"), Movie.class);
        assert movieSearchResponse.getHits().get(0).getId() > 0;
    }

    /**
     * TODO
     */
    @Order(2)
    @Test
    void facetSearch() {
    }

    @Order(4)
    @Test
    void searchWithScore() {
        TaskInfo update = indexes.settings(INDEX).filterableAttributes().update(toList("title", "genres", "director"));
        await(update);

        TypedDetailsSearch<Movie> movieTypedDetailsSearch = indexes.searchWithDetails(INDEX, Movie.class);
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
        TaskInfo update = indexes.settings(INDEX).filterableAttributes().update(toList("title", "genres", "director"));
        await(update);

        TypedDetailsSearch<Movie> movieTypedDetailsSearch = indexes.searchWithDetails(INDEX, Movie.class);
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
        TaskInfo reset = indexes.settings(INDEX).reset();
        await(reset);
    }

}

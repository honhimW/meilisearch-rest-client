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
import io.github.honhimw.ms.api.TypedDetailsSearch;
import io.github.honhimw.ms.model.HitDetails;
import io.github.honhimw.ms.model.SearchDetailsResponse;
import io.github.honhimw.ms.model.SearchRequest;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.CollectionUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TypedDetailsSearchTests extends TestBase {

    protected Indexes indexes;
    protected TypedDetailsSearch<Movie> typedDetailsSearch;

    @BeforeEach
    void initIndexes() {
        prepareData();
        indexes = blockingClient.indexes();
        typedDetailsSearch = indexes.searchWithDetails(INDEX, Movie.class);
    }

    @Order(1)
    @Test
    void search() {
        assert !typedDetailsSearch.find("2").getHits().isEmpty();
    }

    @Order(1)
    @Test
    void search2() {
        SearchDetailsResponse<Movie> movies = typedDetailsSearch.find("2");
        assert movies.getHits().get(0).getSource().getId() > 0;
        assert Objects.isNull(movies.getHits().get(0).getDetails().get_formatted());
    }

    @Order(1)
    @Test
    void search3() {
        SearchRequest searchRequest = SearchRequest.builder().q("2").build();
        SearchDetailsResponse<Movie> movieSearchResponse = typedDetailsSearch.find(searchRequest);
        assert movieSearchResponse.getHits().get(0).getSource().getId() > 0;
    }

    @Order(4)
    @Test
    void searchWithScore() {
        TaskInfo update = indexes.settings(INDEX).filterableAttributes().update(toList("title", "genres", "director"));
        await(update);

        SearchDetailsResponse<Movie> response = typedDetailsSearch.find(builder -> builder
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

        SearchDetailsResponse<Movie> response = typedDetailsSearch.find(builder -> builder
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

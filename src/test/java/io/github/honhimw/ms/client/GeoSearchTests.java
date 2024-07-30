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
import io.github.honhimw.ms.model.HitDetails;
import io.github.honhimw.ms.model.SearchDetails;
import io.github.honhimw.ms.model.SearchDetailsResponse;
import io.github.honhimw.ms.model.SearchResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeoSearchTests extends TestBase {

    protected Indexes indexes;
    protected Search search;

    @BeforeEach
    void initIndexes() {
        INDEX = "cities_test";
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

    @AfterEach
    void resetSetting() {
//        TaskInfo reset = indexes.settings(INDEX).reset();
//        await(reset);
    }

    @Test
    @SneakyThrows
    void save() {
        TypedDetailsSearch<GeoInfo> geoInfoTypedDetailsSearch = indexes.searchWithDetails(INDEX, GeoInfo.class);
        SearchDetailsResponse<GeoInfo> geoInfoSearchDetailsResponse = geoInfoTypedDetailsSearch.find(builder -> builder
            .filter("_geoRadius(37,115, 1000000)")
            .sort(toList("_geoPoint(37,115):asc"))
        );

        List<HitDetails<GeoInfo>> hits = geoInfoSearchDetailsResponse.getHits();
        for (HitDetails<GeoInfo> hit : hits) {
            SearchDetails details = hit.getDetails();
            SearchDetails.Geo geo = details.get_geo();
            System.out.println(geo);
            System.out.println(details.get_geoDistance());
        }
    }

    @Getter
    @Setter
    public static class GeoInfo implements Serializable {
        private String id;
        private String name;
        private SearchDetails.Geo _geo;
        private String country;
    }

}

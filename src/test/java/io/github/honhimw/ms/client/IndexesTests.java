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

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.IndexStats;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IndexesTests extends TestBase {

    protected Indexes indexes;

    @BeforeEach
    void initIndexes() {
        indexes = blockingClient.indexes();
    }

    @Order(-1)
    @Test
    void delete() {
        TaskInfo delete = indexes.delete(INDEX);
        await(delete);
        assert !indexes.get(INDEX).isPresent();
    }

    @Order(0)
    @Test
    void create() {
        TaskInfo taskInfo = indexes.create(INDEX, "id");
        await(taskInfo);
        assert taskInfo.getIndexUid().equals(INDEX);
    }

    @Order(1)
    @Test
    void listIndexes() {
        Page<Index> indexPage = indexes.list(null, null);
        assert indexPage.getOffset() == 0;
        assert indexPage.getLimit() == 20;
        assert indexPage.getResults().stream().map(Index::getUid).collect(Collectors.toList()).contains(INDEX);
    }

    @Order(1)
    @Test
    void listIndexesByPageRequest() {
        Page<Index> indexPage = indexes.list(pageRequest -> pageRequest.no(0).size(10));
        assert indexPage.getOffset() == 0;
        assert indexPage.getLimit() == 10;
        assert indexPage.getResults().stream().map(Index::getUid).collect(Collectors.toList()).contains(INDEX);
    }

    @Order(2)
    @Test
    void getOne() {
        Optional<Index> index = indexes.get(INDEX);
        assert index.isPresent();
    }

    @Order(3)
    @Test
    void update() {
        TaskInfo update = indexes.update(INDEX, "title");
        await(update);
        Optional<Index> index = indexes.get(INDEX);
        assert index.isPresent();
        assert index.get().getPrimaryKey().equals("title");
        TaskInfo update1 = indexes.update(INDEX, "id");
        await(update1);
        Optional<Index> index1 = indexes.get(INDEX);
        assert index1.isPresent();
        assert index1.get().getPrimaryKey().equals("id");
    }

    @Order(4)
    @Test
    void stats() {
        Mono<Boolean> indexing = reactiveClient.indexes().stats(INDEX).map(IndexStats::getIsIndexing);
        StepVerifier.create(indexing).assertNext(aBoolean -> {
                assert !aBoolean : "currently should not in indexing stats.";
            })
            .verifyComplete();
    }

    @Order(5)
    @Test
    void swap() {
        TaskInfo save = indexes.documents(INDEX).save(movies);
        await(save);
        IndexStats stats = indexes.stats(INDEX);
        assert stats.getNumberOfDocuments() > 0;
        String ANOTHER_INDEX = "movie_test2";
        TaskInfo taskInfo = indexes.create(ANOTHER_INDEX);
        await(taskInfo);
        TaskInfo swap = indexes.swap(entryList -> entryList.add(INDEX, ANOTHER_INDEX));
        await(swap);
        stats = indexes.stats(INDEX);
        assert stats.getNumberOfDocuments() == 0;
        TaskInfo delete = indexes.delete(ANOTHER_INDEX);
        await(delete);
    }

}

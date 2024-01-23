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

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class ReactiveIndexesTests extends ReactiveClientTests {

    protected ReactiveIndexes reactiveIndexes;
    protected Indexes blockingIndexes;

    protected String index = "movie_test";

    @BeforeEach
    void initIndexes() {
        reactiveIndexes = reactiveClient.indexes();
        blockingIndexes = blockingClient.indexes();
    }

    @Order(-1)
    @Test
    void delete() {
        Duration duration = StepVerifier.create(reactiveIndexes.delete(index)
                .map(TaskInfo::getTaskUid)
                .flatMap(integer -> reactiveTasks.waitForTask(integer)))
            .verifyComplete();
        log.info("delete index task wait for: {}", duration);
    }

    @Order(0)
    @Test
    void create() {
        Mono<TaskInfo> id = reactiveIndexes.create(index, "id");
        Mono<Void> voidMono = id
            .map(TaskInfo::getTaskUid)
            .flatMap(integer -> reactiveTasks.waitForTask(integer));
        Duration duration = StepVerifier.create(voidMono)
            .verifyComplete();
        log.info("create task wait for: {}", duration);
    }

    @Order(1)
    @Test
    void listIndexes() {
        Mono<Page<Index>> list = reactiveIndexes.list(null, null);
        StepVerifier.create(list)
            .assertNext(indexPage -> {
                assert indexPage.getLimit() == 20;
                assert indexPage.getOffset() == 0;
                assert indexPage.getResults().stream().map(Index::getUid).collect(Collectors.toList()).contains(index);
                if (log.isDebugEnabled()) {
                    log.debug(jsonHandler.toJson(indexPage));
                }
            })
            .verifyComplete();
    }

    @Order(1)
    @Test
    void listIndexesByPageRequest() {
        Mono<Page<Index>> list = reactiveIndexes.list(pageRequest -> pageRequest.no(0).size(10));
        StepVerifier.create(list)
            .assertNext(indexPage -> {
                assert indexPage.getLimit() == 10;
                assert indexPage.getOffset() == 0;
                assert indexPage.getResults().stream().map(Index::getUid).collect(Collectors.toList()).contains(index);
                if (log.isDebugEnabled()) {
                    log.debug(jsonHandler.toJson(indexPage));
                }
            })
            .verifyComplete();
    }

    @Order(2)
    @Test
    void getOne() {
        Mono<Index> movies = reactiveIndexes.get(index);
        StepVerifier.create(movies)
            .assertNext(index -> {
                assert index != null;
                log.info(jsonHandler.toJson(index));
            })
            .verifyComplete();
    }

    @Order(2)
    @Test
    void getOne2() {
        Mono<Index> movies = reactiveClient.indexes(indexes1 -> indexes1.get(index));
        StepVerifier.create(movies)
            .assertNext(index -> {
                assert index != null;
                log.info(jsonHandler.toJson(index));
            })
            .verifyComplete();
    }

    @Order(3)
    @Test
    void update() {
        List<String> pks = Stream.of("release_date", "id").collect(Collectors.toList());
        for (String newPk : pks) {
            Mono<String> updateTask = reactiveClient.indexes().update(index, newPk)
                .flatMap(taskInfo -> reactiveClient.tasks().waitForTask(taskInfo.getTaskUid()))
                .then(reactiveClient.indexes().get(index).map(Index::getPrimaryKey));

            StepVerifier.create(updateTask)
                .assertNext(pk -> {
                    assert StringUtils.equal(pk, pk);
                })
                .verifyComplete();
        }
    }

    @Order(4)
    @Test
    void stats() {
        Mono<Boolean> indexing = reactiveClient.indexes().stats(index).map(IndexStats::getIsIndexing);
        StepVerifier.create(indexing).assertNext(aBoolean -> {
                assert !aBoolean : "currently should not in indexing stats.";
            })
            .verifyComplete();
    }

}

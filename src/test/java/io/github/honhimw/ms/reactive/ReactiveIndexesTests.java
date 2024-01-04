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

import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class ReactiveIndexesTests extends ReactiveClientTests {

    protected ReactiveIndexes indexes;

    protected String index = "movie_test";

    @BeforeEach
    void initIndexes() {
        indexes = client.indexes();
    }

    @Order(-1)
    @Test
    void delete() {
        Duration duration = StepVerifier.create(indexes.delete(index)
                .map(TaskInfo::getTaskUid)
                .flatMap(integer -> tasks.waitForTask(integer)))
            .verifyComplete();
        log.info("delete index task wait for: {}", duration);
    }

    @Order(0)
    @Test
    void create() {
        Mono<TaskInfo> id = indexes.create(index, "id");
        Mono<Void> voidMono = id
            .map(TaskInfo::getTaskUid)
            .flatMap(integer -> tasks.waitForTask(integer));
        Duration duration = StepVerifier.create(voidMono)
            .verifyComplete();
        log.info("create task wait for: {}", duration);
    }

    @Order(1)
    @Test
    void listIndexes() {
        Mono<Page<Index>> list = indexes.list(null, null);
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

    @Order(2)
    @Test
    void getOne() {
        Mono<Index> movies = indexes.get(index);
        StepVerifier.create(movies)
            .assertNext(index -> {
                assert index != null;
                log.info(jsonHandler.toJson(index));
            })
            .verifyComplete();
    }

}

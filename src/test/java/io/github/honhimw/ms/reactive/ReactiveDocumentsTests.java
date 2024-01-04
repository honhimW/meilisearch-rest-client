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

import io.github.honhimw.ms.api.reactive.ReactiveDocuments;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
public class ReactiveDocumentsTests extends ReactiveIndexesTests {

    @Order(100)
    @Test
    void save() {
        Mono<TaskInfo> save = indexes.documents(index).save(movies);
        Duration duration = StepVerifier.create(save
                .flatMap(taskInfo -> tasks.waitForTask(taskInfo.getTaskUid())))
            .verifyComplete();
        log.info("save document task wait for: {}", duration);
    }

    @Order(101)
    @Test
    void listDocuments() {
        ReactiveDocuments movies = indexes.documents(index);
        StepVerifier.create(movies.list(null, null))
            .assertNext(mapPage -> {
                assert mapPage.getLimit() == 20;
                assert mapPage.getOffset() == 0;
                if (log.isDebugEnabled()) {
                    log.debug(jsonHandler.toJson(mapPage));
                }
            })
            .verifyComplete()
        ;
    }

    @Order(102)
    @Test
    void getOne() {
        ReactiveDocuments movies = indexes.documents(index);
        StepVerifier.create(movies.get("2"))
            .assertNext(map -> {
                assert map != null;
                log.info(jsonHandler.toJson(map));
            })
            .verifyComplete();
    }

    @Order(103)
    @Test
    void g() {
        StepVerifier.create(indexes.search(index).find("2"))
            .assertNext(searchResponse -> {
                log.info(jsonHandler.toJson(searchResponse.getHits()));
                assert !searchResponse.getHits().isEmpty();
            })
            .verifyComplete();
    }

}

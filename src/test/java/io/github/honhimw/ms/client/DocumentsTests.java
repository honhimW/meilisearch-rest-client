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
import io.github.honhimw.ms.api.reactive.ReactiveDocuments;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentsTests extends TestBase {

    protected ReactiveIndexes indexes;

    protected ReactiveDocuments documents;

    @BeforeEach
    void initIndexes() {
        indexes = reactiveClient.indexes();
        documents = indexes.documents(INDEX);
    }

    @Order(100)
    @Test
    void save() {
        Mono<TaskInfo> save = documents.save(movies);
        Duration duration = StepVerifier.create(await(save))
            .verifyComplete();
        log.info("save document task wait for: {}", duration);
    }

    @Order(100)
    @Test
    void save2() {
        Movie one = new Movie();
        one.setId(30);
        one.setTitle("hello world");
        Mono<TaskInfo> save = documents.save(one);
        Duration duration = StepVerifier.create(await(save))
            .verifyComplete();
        log.info("save document task wait for: {}", duration);
    }

    @Order(101)
    @Test
    void listDocuments() {
        StepVerifier.create(documents.list(null, null))
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
        StepVerifier.create(documents.get("2", "title"))
            .assertNext(map -> {
                assert map != null;
                assert map.get("title").equals("Ariel");
                assert Objects.isNull(map.get("poster"));
            })
            .verifyComplete();
    }

    @Order(102)
    @Test
    void getOne2() {
        StepVerifier.create(documents.get("2", Movie.class, "title"))
            .assertNext(map -> {
                assert map != null;
                assert map.getTitle().equals("Ariel");
                assert Objects.isNull(map.getPoster());
            })
            .verifyComplete();
    }

    @Order(102)
    @Test
    void getOne3() {
        StepVerifier.create(documents.get("2", new TypeRef<Movie>() {
            }, "title"))
            .assertNext(map -> {
                assert map != null;
                assert map.getTitle().equals("Ariel");
                assert Objects.isNull(map.getPoster());
            })
            .verifyComplete();
    }

    @Order(103)
    @Test
    void update() {
        Mono<Movie> movieMono = documents.get("30", Movie.class);
        StepVerifier.create(movieMono)
            .assertNext(movie -> {
                assert movie.getTitle().equals("hello world");
            })
            .verifyComplete();
        Movie one = new Movie();
        one.setId(30);
        one.setTitle("foo bar");
        Mono<TaskInfo> update = documents.update(one);
        StepVerifier.create(await(update))
            .verifyComplete();
        Mono<Movie> movieMono2 = documents.get("30", Movie.class);
        StepVerifier.create(movieMono2)
            .assertNext(movie -> {
                assert movie.getTitle().equals("foo bar");
            })
            .verifyComplete();

    }

}

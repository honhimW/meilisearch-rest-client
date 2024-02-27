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
import io.github.honhimw.ms.api.TypedDocuments;
import io.github.honhimw.ms.api.reactive.ReactiveDocuments;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.api.reactive.ReactiveTypedDocuments;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.BatchGetDocumentsRequest;
import io.github.honhimw.ms.model.FilterableAttributesRequest;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentsTests extends TestBase {

    protected ReactiveIndexes indexes;

    protected ReactiveDocuments documents;

    protected TypedDocuments<Movie> typedDocuments;

    @BeforeEach
    void initIndexes() {
        indexes = reactiveClient.indexes();
        documents = indexes.documents(INDEX);
        typedDocuments = blockingClient.indexes().documents(INDEX, Movie.class);
    }

    @Order(1)
    @Test
    void save() {
        Mono<TaskInfo> save = documents.save(movies);
        Duration duration = StepVerifier.create(await(save))
            .verifyComplete();
        log.info("save document task wait for: {}", duration);
    }

    @Order(2)
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

    @Order(3)
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

    @Order(4)
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

    @Order(5)
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

    @Order(6)
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

    @Order(7)
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

    @Order(8)
    @Test
    void typedSave() {
        Movie movie = new Movie();
        movie.setId(40);
        String typedMovie = "typed movie";
        movie.setTitle(typedMovie);
        TaskInfo save = typedDocuments.save(movie);
        await(save);
        String title = typedDocuments.get("40", "title").map(Movie::getTitle).orElse(null);
        assert StringUtils.equal(title, typedMovie);
    }

    @Order(9)
    @Test
    void typedList() {
        Page<Movie> list = typedDocuments.list(pageRequest -> pageRequest.no(0).size(20));
        assert CollectionUtils.isNotEmpty(list.getResults());
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(9)
    @Test
    void typedBatchGet() {
        Page<Movie> list = typedDocuments.batchGet(BatchGetDocumentsRequest.builder().offset(0).limit(20)
            .fields(toList("title"))
            .build());
        assert CollectionUtils.isNotEmpty(list.getResults());
        list.getResults().forEach(movie -> {
            assert StringUtils.isNotEmpty(movie.getTitle());
            assert movie.getId() == null;
        });
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(10)
    @Test
    void typedUpdate() {
        Movie movie = new Movie();
        movie.setId(40);
        String typedMovie = "update typed movie";
        movie.setTitle(typedMovie);
        TaskInfo update = typedDocuments.update(movie);
        await(update);
        String title = typedDocuments.get("40", "title").map(Movie::getTitle).orElse(null);
        assert StringUtils.equal(title, typedMovie);
    }

    @Order(11)
    @Test
    void typedDelete() {
        Movie movie = new Movie();
        movie.setId(40);
        TaskInfo delete = typedDocuments.delete("40");
        TaskInfo batchDelete = typedDocuments.batchDelete(Collections.singleton("41"));
        TaskInfo deleteByFilter = typedDocuments.delete(FilterableAttributesRequest.builder(filterBuilder -> filterBuilder.base(expression -> expression.isNull("poster"))));
        await(delete);
        await(batchDelete);
        await(deleteByFilter);
        Optional<Movie> movie40 = typedDocuments.get("40");
        assert !movie40.isPresent();
        Optional<Movie> movie41 = typedDocuments.get("41");
        assert !movie41.isPresent();
    }

    @Order(100)
    @Test
    void deleteAll() {
        TaskInfo deleteAll = typedDocuments.deleteAll();
        await(deleteAll);
        Page<Movie> list = typedDocuments.list(0, 20);
        assert list.getTotal() == 0;
    }
}

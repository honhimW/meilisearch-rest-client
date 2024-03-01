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
import io.github.honhimw.ms.api.Documents;
import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.Settings;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.CollectionUtils;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentsTests extends TestBase {

    protected Indexes indexes;

    protected Documents documents;

    @BeforeEach
    void initIndexes() {
        indexes = blockingClient.indexes();
        documents = indexes.documents(INDEX);

        Settings settings = indexes.settings(INDEX);
        TaskInfo makeFilterable = settings.filterableAttributes().update(toList("id", "poster"));
        await(makeFilterable);
    }

    @Order(1)
    @Test
    void save() {
        TaskInfo save = documents.save(movies);
        await(save);
        TaskInfo taskInfo = getBlockingTasks().get(save.getTaskUid());
        assert taskInfo.getStatus() == TaskStatus.SUCCEEDED;
    }

    @Order(2)
    @Test
    void save2() {
        Movie one = new Movie();
        one.setId(30);
        String helloWorld = "hello world";
        one.setTitle(helloWorld);
        TaskInfo save = documents.save(one);
        await(save);
        assert documents.get("30", "title").map(map -> map.get("title")).filter(s -> s.equals(helloWorld)).isPresent();
    }

    @Order(2)
    @Test
    void save3() {
        String json = jsonQuote("[{'id':31,'title':'foo bar'}]");
        TaskInfo save = documents.save(json);
        await(save);
        assert documents.get("31", "title").map(map -> map.get("title")).filter(s -> s.equals("foo bar")).isPresent();
    }

    @Order(2)
    @Test
    void save4() {
        Movie one = new Movie();
        one.setId(32);
        one.setTitle("foo");
        Movie two = new Movie();
        two.setId(33);
        two.setTitle("bar");

        TaskInfo save = documents.save(toList(one, two));
        await(save);
        assert documents.get("32", "title").map(map -> map.get("title")).filter(s -> s.equals("foo")).isPresent();
        assert documents.get("33", "title").map(map -> map.get("title")).filter(s -> s.equals("bar")).isPresent();
    }

    @Order(3)
    @Test
    void listDocuments() {
        Page<?> list = documents.list(0, null);
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;

    }

    @Order(3)
    @Test
    void listDocuments2() {
        Page<Movie> list = documents.list(0, null, Movie.class);
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;

    }

    @Order(3)
    @Test
    void listDocuments3() {
        Page<?> list = documents.list(pageRequest -> {
            pageRequest.filter("id EXISTS");
            pageRequest.fields(toList("id"));
        });
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(3)
    @Test
    void listDocuments4() {
        Page<?> list = documents.list(new GetDocumentRequest());
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(4)
    @Test
    void getOne() {
        Optional<Map<String, Object>> title = documents.get("2", "title");
        assert title.isPresent();
        Map<String, Object> movie = title.get();
        assert movie.get("title").equals("Ariel");
        assert Objects.isNull(movie.get("poster"));
    }

    @Order(4)
    @Test
    void getOne2() {
        Optional<Movie> title = documents.get("2", Movie.class, "title");
        assert title.isPresent();
        Movie movie = title.get();
        assert movie.getTitle().equals("Ariel");
        assert Objects.isNull(movie.getPoster());
    }

    @Order(5)
    @Test
    void update() {
        Movie movie = new Movie();
        movie.setId(30);
        movie.setTitle("hello world");
        TaskInfo save = documents.save(movie);
        await(save);
        movie.setTitle("foo bar");
        TaskInfo update = documents.update(movie);
        await(update);
        Optional<Map<String, Object>> result = documents.get("30", "title");
        assert result.isPresent();
        Map<String, Object> _result = result.get();
        assert _result.get("title").equals("foo bar");
    }

    @Order(5)
    @Test
    void update2() {
        Movie movie = new Movie();
        movie.setId(30);
        movie.setTitle("hello world");
        TaskInfo save = documents.save(movie);
        await(save);
        movie.setTitle("foo bar");
        TaskInfo update = documents.update(movie);
        await(update);
        Optional<Movie> result = documents.get("30", Movie.class, "title");
        assert result.isPresent();
        Movie _result = result.get();
        assert _result.getTitle().equals("foo bar");
    }

    @Order(5)
    @Test
    void update3() {
        Movie movie = new Movie();
        movie.setId(31);
        movie.setTitle("hello world");
        TaskInfo save = documents.save(movie);
        await(save);
        movie.setTitle("foo bar");
        String json = jsonHandler.toJson(toList(movie));
        TaskInfo update = documents.update(json);
        await(update);
        Optional<Movie> result = documents.get("31", Movie.class, "title");
        assert result.isPresent();
        Movie _result = result.get();
        assert _result.getTitle().equals("foo bar");
    }

    @Order(6)
    @Test
    void batchGet() {
        Page<Map<String, Object>> list = documents.batchGet(BatchGetDocumentsRequest.builder().offset(0).limit(20)
            .fields(toList("title"))
            .build());
        assert CollectionUtils.isNotEmpty(list.getResults());
        list.getResults().forEach(movie -> {
            assert StringUtils.isNotEmpty((CharSequence) movie.get("title"));
            assert movie.get("id") == null;
        });
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(6)
    @Test
    void batchGet2() {
        Page<Movie> list = documents.batchGet(BatchGetDocumentsRequest.builder().offset(0).limit(20)
            .fields(toList("title"))
            .build(), Movie.class);
        assert CollectionUtils.isNotEmpty(list.getResults());
        list.getResults().forEach(movie -> {
            assert StringUtils.isNotEmpty(movie.getTitle());
            assert movie.getId() == null;
        });
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(6)
    @Test
    void batchGet3() {
        Page<Movie> list = documents.batchGet(builder -> builder.offset(0).limit(20)
            .fields(toList("title"))
            .build(), Movie.class);
        assert CollectionUtils.isNotEmpty(list.getResults());
        list.getResults().forEach(movie -> {
            assert StringUtils.isNotEmpty(movie.getTitle());
            assert movie.getId() == null;
        });
        assert list.getOffset() == 0;
        assert list.getLimit() == 20;
    }

    @Order(7)
    @Test
    void delete() {
        Movie movie = new Movie();
        movie.setId(40);
        TaskInfo save = documents.save(movie);
        await(save);
        assert documents.get("40").isPresent();
        TaskInfo delete = documents.delete("40");
        await(delete);
        Optional<?> movie40 = documents.get("40");
        assert !movie40.isPresent();
    }

    @Order(8)
    @Test
    void batchDelete() {
        TaskInfo save = documents.save(jsonQuote("[{'id':40},{'id':41}]"));
        await(save);
        BatchGetDocumentsRequest batchGetDocumentsRequest = new BatchGetDocumentsRequest();
        batchGetDocumentsRequest.filter(filterBuilder -> filterBuilder.base(expression -> expression.in("id", "40", "41")));
        assert documents.batchGet(batchGetDocumentsRequest).getTotal() == 2;
        TaskInfo batchDelete = documents.batchDelete(toList("40", "41"));
        await(batchDelete);
        assert documents.batchGet(batchGetDocumentsRequest).getTotal() == 0;
    }

    @Order(9)
    @Test
    void deleteByFilter() {
        TaskInfo save = documents.save(jsonQuote("[{'id':40,'poster':'http://0.0.0.0/poster'},{'id':41}]"));
        await(save);
        BatchGetDocumentsRequest batchGetDocumentsRequest = new BatchGetDocumentsRequest();
        batchGetDocumentsRequest.filter(filterBuilder -> filterBuilder.base(expression -> expression.isNullOrNotExists("poster")));
        assert documents.batchGet(batchGetDocumentsRequest).getTotal() > 0;
        TaskInfo deleteByFilter = documents.delete(batchGetDocumentsRequest);
        await(deleteByFilter);
        assert documents.batchGet(batchGetDocumentsRequest).getTotal() == 0;
    }

    @Order(100)
    @Test
    void deleteAll() {
        TaskInfo deleteAll = documents.deleteAll();
        await(deleteAll);
        Page<?> list = documents.list(0, 20);
        assert list.getTotal() == 0;
    }
}

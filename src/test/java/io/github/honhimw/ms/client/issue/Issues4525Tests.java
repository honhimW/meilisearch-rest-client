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

package io.github.honhimw.ms.client.issue;

import io.github.honhimw.ms.api.SingleIndex;
import io.github.honhimw.ms.api.TypedDocuments;
import io.github.honhimw.ms.client.TestBase;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TaskStatus;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://github.com/meilisearch/meilisearch/issues/4525">https://github.com/meilisearch/meilisearch/issues/4525</a>
 * <p>
 * The test will fail from version v1.7.1 to v1.7.3.
 *
 * @author hon_him
 * @since 2024-04-07
 */

public class Issues4525Tests extends TestBase {

    @Test
    @SneakyThrows
    void nestedField() {
        String indexUid = "person-test";
        List<Person> people = getDatas();
        SingleIndex single = blockingClient.indexes().single(indexUid);
        TaskInfo createIndex = single.create("id");
        await(createIndex);
        TypedDocuments<Person> documents = single.documents(Person.class);
        TaskInfo save = documents.save(people);
        TaskInfo await = await(save);
        assert await.getStatus().equals(TaskStatus.SUCCEEDED);
        {
            SearchResponse<Person> personSearchResponse = single.search(Person.class).find("");
            List<Person> hits = personSearchResponse.getHits();
            assert hits.size() == 3;
            assert hits.get(0).getId().equals("1");
            assert hits.get(1).getId().equals("2");
            assert hits.get(2).getId().equals("3");
        }

        TaskInfo update = single.settings().sortableAttributes().update(toList("pet.age"));
        await(update);

        {
            SearchResponse<Person> personSearchResponse = single.search(Person.class).find(builder -> builder.sort(toList("pet.age:desc")));
            List<Person> hits = personSearchResponse.getHits();
            assert hits.size() == 3;
            assert hits.get(0).getId().equals("2");
            assert hits.get(1).getId().equals("3");
            assert hits.get(2).getId().equals("1");
        }
        TaskInfo delete = single.delete();
        await(delete);
    }

    private List<Person> getDatas() {
        List<Person> people = new ArrayList<>();
        {
            Person person = new Person();
            person.setId("1");
            person.setAge(18);
            person.setPet(new Pet());
            person.getPet().setName("Pikachu");
            person.getPet().setAge(2);
            person.getPet().setType("cat");
            person.setName(new Name());
            person.getName().setFirstName("John");
            person.getName().setLastName("Snow");
            people.add(person);
        }
        {
            Person person = new Person();
            person.setId("2");
            person.setAge(13);
            person.setPet(new Pet());
            person.getPet().setName("Blastoise");
            person.getPet().setAge(889);
            person.getPet().setType("turtle");
            person.setName(new Name());
            person.getName().setFirstName("John");
            person.getName().setLastName("Snow");
            people.add(person);
        }
        {
            Person person = new Person();
            person.setId("3");
            person.setAge(22);
            person.setPet(new Pet());
            person.getPet().setName("Charizard");
            person.getPet().setAge(91);
            person.getPet().setType("dinosaur");
            person.setName(new Name());
            person.getName().setFirstName("John");
            person.getName().setLastName("Snow");
            people.add(person);
        }
        return people;
    }

    @Data
    public static final class Person implements Serializable {
        private String id;
        private Integer age;
        private Pet pet;
        private Name name;
    }

    @Data
    public static final class Pet implements Serializable {
        private String name;
        private Integer age;
        private String type;
    }

    @Data
    public static final class Name implements Serializable {
        private String firstName;
        private String lastName;
    }
}

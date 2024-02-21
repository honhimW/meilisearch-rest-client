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

package io.github.honhimw.ms;

import io.github.honhimw.ms.support.FilterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author hon_him
 * @since 2024-01-05
 */

@Slf4j
public class FilterBuilderTests {

    @Test
    void stringEqual() {
        String filter = FilterBuilder.builder(expression -> expression.equal("genres", "action")).build();
        log.info(filter);
        assert filter.equals("genres = 'action'");
    }

    @Test
    void numberEqual() {
        String filter = FilterBuilder.builder(expression -> expression.equal("id", 1)).build();
        log.info(filter);
        assert filter.equals("id = 1");
    }

    @Test
    void stringUnequal() {
        String filter = FilterBuilder.builder(expression -> expression.unequal("genres", "action")).build();
        log.info(filter);
        assert filter.equals("genres != 'action'");
    }

    @Test
    void numberUnequal() {
        String filter = FilterBuilder.builder(expression -> expression.unequal("id", 1)).build();
        log.info(filter);
        assert filter.equals("id != 1");
    }

    @Test
    void gt() {
        String filter = FilterBuilder.builder(expression -> expression.gt("rating.users", 85)).build();
        log.info(filter);
        assert filter.equals("rating.users > 85");
    }

    @Test
    void ge() {
        String filter = FilterBuilder.builder(expression -> expression.ge("rating.users", 85)).build();
        log.info(filter);
        assert filter.equals("rating.users >= 85");
    }

    @Test
    void lt() {
        String filter = FilterBuilder.builder(expression -> expression.lt("rating.users", 90)).build();
        log.info(filter);
        assert filter.equals("rating.users < 90");
    }

    @Test
    void le() {
        String filter = FilterBuilder.builder(expression -> expression.le("rating.users", 90)).build();
        log.info(filter);
        assert filter.equals("rating.users <= 90");
    }

    @Test
    void to() {
        String filter = FilterBuilder.builder(expression -> expression.to("rating.users", 80, 89)).build();
        log.info(filter);
        assert filter.equals("rating.users 80 TO 89");
    }

    @Test
    void exists() {
        String filter = FilterBuilder.builder(expression -> expression.exists("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date EXISTS");
    }

    @Test
    void notExists() {
        String filter = FilterBuilder.builder(expression -> expression.notExists("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date NOT EXISTS");
    }

    @Test
    void isEmpty() {
        String filter = FilterBuilder.builder(expression -> expression.isEmpty("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date IS EMPTY");
    }

    @Test
    void notEmpty() {
        String filter = FilterBuilder.builder(expression -> expression.notEmpty("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date NOT EMPTY");
    }

    @Test
    void isNull() {
        String filter = FilterBuilder.builder(expression -> expression.isNull("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date IS NULL");
    }

    @Test
    void notNull() {
        String filter = FilterBuilder.builder(expression -> expression.notNull("release_date")).build();
        log.info(filter);
        assert filter.equals("release_date NOT NULL");
        filter = FilterBuilder.builder(expression -> expression.isNull("release_date").not()).build();
        log.info(filter);
        assert filter.equals("NOT release_date IS NULL");
    }

    @Test
    void in() {
        String filter = FilterBuilder.builder(expression -> expression.in("genres", "horror", "comedy")).build();
        log.info(filter);
        assert filter.equals("genres IN ['horror', 'comedy']");
        filter = FilterBuilder.builder(expression -> expression.in("id", 1, 10)).build();
        log.info(filter);
        assert filter.equals("id IN [1, 10]");
    }

    @Test
    void not() {
        String filter = FilterBuilder.builder(expression -> expression.in("genres", "horror", "comedy").not()).build();
        log.info(filter);
        assert filter.equals("NOT genres IN ['horror', 'comedy']");
        filter = FilterBuilder.builder(expression -> expression.in("genres", "horror", "comedy").not().not()).build();
        log.info(filter);
        assert filter.equals("genres IN ['horror', 'comedy']");
    }

    @Test
    void and() {
        String filter = FilterBuilder.builder(expression -> expression.in("genres", "horror", "comedy"))
            .and(expression -> expression.equal("director", "Jordan Peele"))
            .build();
        log.info(filter);
        assert filter.equals("genres IN ['horror', 'comedy'] AND director = 'Jordan Peele'");
    }

    @Test
    void or() {
        String filter = FilterBuilder.builder(expression -> expression.in("genres", "horror", "comedy"))
            .or(expression -> expression.equal("director", "Jordan Peele"))
            .build();
        log.info(filter);
        assert filter.equals("genres IN ['horror', 'comedy'] OR director = 'Jordan Peele'");
    }

    @Test
    void andGroup() {
        String filter = FilterBuilder.builder()
            .baseGroup(filterBuilder -> filterBuilder
                .base(expression -> expression.equal("genres", "comedy"))
                .or(expression -> expression.equal("genres", "horror")))
            .and(expression -> expression.unequal("director", "Jordan Peele"))
            .build();
        log.info(filter);
        assert filter.equals("(genres = 'comedy' OR genres = 'horror') AND director != 'Jordan Peele'");
    }

    @Test
    void orGroup() {
        String filter = FilterBuilder.builder()
            .baseGroup(filterBuilder -> filterBuilder
                .base(expression -> expression.equal("genres", "comedy"))
                .or(expression -> expression.equal("genres", "horror")))
            .orGroup(filterBuilder -> filterBuilder
                .base(expression -> expression.unequal("director", "Jordan Peele"))
                .or(expression -> expression.unequal("director", "honhimw")))
            .build();
        log.info(filter);
        assert filter.equals("(genres = 'comedy' OR genres = 'horror') OR (director != 'Jordan Peele' OR director != 'honhimw')");
    }

}

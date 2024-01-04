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

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public class IndexesTests extends ClientTests {

    protected Indexes indexes;

    @BeforeEach
    void initIndexes() {
        indexes = client.indexes();
    }

    @Test
    void listIndexes() {
        Page<Index> list = indexes.list(null, null);
        assert list.getLimit() == 20;
        assert list.getOffset() == 0;
        if (log.isDebugEnabled()) {
            log.debug(jsonHandler.toJson(list));
        }
    }

    @Test
    void getOne() {
        Index movies = indexes.get("movies");
        System.out.println(movies);
    }

}

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

import io.github.honhimw.ms.api.Documents;
import io.github.honhimw.ms.model.Page;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public class DocumentesTests extends IndexesTests {

    protected Documents documents;

    @Test
    void listDocuments() {
        Documents movies = indexes.documents("movies");
        Page<Map<String, Object>> list = movies.list(pageRequest -> {});
        assert list.getLimit() == 20;
        assert list.getOffset() == 0;
        if (log.isDebugEnabled()) {
            log.debug(jsonHandler.toJson(list));
        }
    }

    @Test
    void getOne() {
        Documents movies = indexes.documents("movies");
        Map<String, Object> document = movies.get("233");
        System.out.println(jsonHandler.toJson(document));
    }

}

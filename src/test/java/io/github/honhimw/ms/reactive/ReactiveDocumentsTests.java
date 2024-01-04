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
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public class ReactiveDocumentsTests extends ReactiveIndexesTests {

    @Test
    void listDocuments() {
        ReactiveDocuments movies = indexes.documents("movies");
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

    @Test
    void getOne() {
        ReactiveDocuments movies = indexes.documents("movies");
        StepVerifier.create(movies.get("233"))
            .assertNext(map -> {
                assert map != null;
                log.info(jsonHandler.toJson(map));
            })
            .verifyComplete();
    }

}

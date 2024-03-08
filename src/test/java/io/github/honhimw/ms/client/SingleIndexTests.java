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

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.SingleIndex;
import io.github.honhimw.ms.model.Index;
import io.github.honhimw.ms.model.IndexStats;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;

import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleIndexTests extends TestBase {

    protected Indexes indexes;
    protected SingleIndex singleIndex;

    @BeforeEach
    void initIndexes() {
        indexes = blockingClient.indexes();
        singleIndex = indexes.single(INDEX);
    }

    @Order(-1)
    @Test
    void delete() {
        TaskInfo delete = singleIndex.delete();
        await(delete);
        assert !singleIndex.get().isPresent();
    }

    @Order(0)
    @Test
    void create() {
        TaskInfo taskInfo = singleIndex.create("id");
        await(taskInfo);
        assert taskInfo.getIndexUid().equals(INDEX);
    }
    @Order(2)
    @Test
    void getOne() {
        Optional<Index> index = singleIndex.get();
        assert index.isPresent();
    }

    @Order(3)
    @Test
    void update() {
        TaskInfo update = singleIndex.update("title");
        await(update);
        Optional<Index> index = singleIndex.get();
        assert index.isPresent();
        assert index.get().getPrimaryKey().equals("title");
        TaskInfo update1 = singleIndex.update("id");
        await(update1);
        Optional<Index> index1 = singleIndex.get();
        assert index1.isPresent();
        assert index1.get().getPrimaryKey().equals("id");
    }

    @Order(4)
    @Test
    void stats() {
        IndexStats stats = singleIndex.stats();
        assert !stats.getIsIndexing(): "currently should not in indexing stats.";
    }

}

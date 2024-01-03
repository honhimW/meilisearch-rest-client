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

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.CancelTasksQuery;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.TaskInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public class OfficialClientTests {

    private Client client;

    @BeforeEach
    void init() {
        Config config = new Config("http://10.37.1.132:7700", "key");
        client = new Client(config);
    }

    @Test
    @SneakyThrows
    void createIndex() {
        TaskInfo index = client.createIndex("");
    }

    @Test
    @SneakyThrows
    void getIndex() {
        Index index = client.getIndex("");
        TaskInfo taskInfo = index.addDocuments("");
        int taskUid = taskInfo.getTaskUid();
        client.waitForTask(taskUid);
    }

    @Test
    @SneakyThrows
    void search() {
        Index index = client.index("");
        SearchResult search = index.search("");
        CancelTasksQuery param = new CancelTasksQuery();
        client.cancelTasks(param);
    }

}

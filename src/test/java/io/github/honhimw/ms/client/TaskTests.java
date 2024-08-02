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

import io.github.honhimw.ms.api.Documents;
import io.github.honhimw.ms.api.Tasks;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TaskStatus;
import io.github.honhimw.ms.model.TaskView;
import io.github.honhimw.ms.support.TestSupport;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-04-18
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskTests extends TestBase {

    @Order(0)
    @Test
    void taskView() {
        Documents documents = blockingClient.indexes().documents(INDEX);
        TaskInfo save = documents.save(jsonQuote("{'id':-1}"));
        assert Objects.nonNull(save.getTaskUid());
        Tasks tasks = getBlockingTasks();
        TaskView taskView = tasks.get(save.getTaskUid());
        assert Objects.nonNull(taskView.getUid());
    }


    @Order(1)
    @Test
    void awaitOnTaskInfo() {
        TaskInfo saveTask = blockingClient.indexes(indexes -> indexes.documents(INDEX).save(TestSupport.jsonQuote("{'id':100}")));
        TaskStatus status = saveTask.await().getStatus();
        assert status == TaskStatus.SUCCEEDED || status == TaskStatus.FAILED;
    }

    @Test
    void timeout() {
        TestSupport.assertError(() -> getBlockingTasks().await(1, 1, Duration.ofSeconds(1), Duration.ZERO));
    }

}

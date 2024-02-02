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

package io.github.honhimw.ms.json;

import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TaskStatus;
import io.github.honhimw.ms.model.TaskType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * @author hon_him
 * @since 2024-01-19
 */

public class EnumTests {

    private TaskInfo taskInfo;

    @BeforeEach
    void mockTask() {
        taskInfo = new TaskInfo();
        taskInfo.setTaskUid(1);
        taskInfo.setType(TaskType.DOCUMENT_ADDITION_OR_UPDATE);
        taskInfo.setStatus(TaskStatus.ENQUEUED);
        taskInfo.setEnqueuedAt(LocalDateTime.now());
        taskInfo.setIndexUid("1");
    }

    @Test
    @SneakyThrows
    void jackson() {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        String json = jsonHandler.toJson(taskInfo);
        TaskInfo _info = jsonHandler.fromJson(json, TaskInfo.class);
        assert _info.getType() == TaskType.DOCUMENT_ADDITION_OR_UPDATE;
        assert _info.getStatus() == TaskStatus.ENQUEUED;
    }

    @Test
    @SneakyThrows
    void gson() {
        JsonHandler jsonHandler = new GsonJsonHandler();
        String json = jsonHandler.toJson(taskInfo);
        TaskInfo _info = jsonHandler.fromJson(json, TaskInfo.class);
        assert _info.getType() == TaskType.DOCUMENT_ADDITION_OR_UPDATE;
        assert _info.getStatus() == TaskStatus.ENQUEUED;
    }


}

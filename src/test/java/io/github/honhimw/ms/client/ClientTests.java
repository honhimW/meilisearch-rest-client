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

import io.github.honhimw.ms.api.reactive.Logs;
import io.github.honhimw.ms.model.*;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientTests extends TestBase {

    @Order(-1)
    @Test
    void healthy() {
        blockingClient.healthy();
    }

    @Order(0)
    @Test
    void version() {
        Version version = blockingClient.version();
        assert StringUtils.startsWith(version.getPkgVersion(), "1.7");
    }

    @Order(1)
    @Test
    void resetLogs() {
        ExperimentalFeatures experimentalFeatures = blockingClient.experimentalFeatures(experimentalFeaturesSettings -> experimentalFeaturesSettings.configure(builder -> builder.logsRoute(true)));
        assert experimentalFeatures.getLogsRoute();
        Logs logs = blockingClient.logs();
        logs.reset();
    }

    @Order(2)
    @Test
    void keys() {
        Boolean result = blockingClient.keys(keys -> {
            LocalDateTime now = LocalDateTime.now();
            Key key = keys.create(builder -> builder
                .name("rest-client-test")
                .actions(toList(KeyAction.SEARCH))
                .indexes(toList(INDEX))
                .expiresAt(now.plusHours(1)));
            assert key.getExpiresAt().isEqual(now.plusHours(1));
            Page<Key> list = keys.list(null, null);
            assert list.getTotal() > 0;
            Optional<Key> optionalKey = keys.get(key.getKey());
            assert optionalKey.isPresent();
            assert optionalKey.get().getUid().equals(key.getUid());
            Key update = keys.update(key.getUid(), builder -> builder.name("rest-client-tests"));
            assert update.getName().equals("rest-client-tests");
            keys.delete(key.getUid());
            assert !keys.get(key.getUid()).isPresent();
            return true;
        });
        assert result;
    }

    @Order(3)
    @Test
    void tasks() {
        TaskInfo deleteSucceededTasks = blockingClient.tasks(tasks -> tasks.delete(builder -> builder.statuses(toList(TaskStatus.SUCCEEDED))));
        await(deleteSucceededTasks);
        Page<TaskInfo> getSucceededTasks = blockingClient.tasks(tasks -> tasks.list(builder -> builder.statuses(toList(TaskStatus.SUCCEEDED))));
        assert getSucceededTasks.getTotal() == 1;
        assert getSucceededTasks.getLimit() == 20;
    }


}

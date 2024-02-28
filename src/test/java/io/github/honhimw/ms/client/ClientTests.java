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

import io.github.honhimw.ms.api.ExperimentalFeaturesSettings;
import io.github.honhimw.ms.api.reactive.Logs;
import io.github.honhimw.ms.model.ExperimentalFeatures;
import io.github.honhimw.ms.model.Version;
import io.github.honhimw.ms.support.StringUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
        ExperimentalFeaturesSettings experimentalFeaturesSettings = blockingClient.experimentalFeatures();
        ExperimentalFeatures configure = experimentalFeaturesSettings.configure(builder -> builder.logsRoute(true));
        assert configure.getLogsRoute();
        Logs logs = blockingClient.logs();
        logs.reset();
    }

}

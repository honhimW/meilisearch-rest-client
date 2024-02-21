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

package io.github.honhimw.ms.client.setting;

import io.github.honhimw.ms.api.Settings;
import io.github.honhimw.ms.api.reactive.ReactiveSettings;
import io.github.honhimw.ms.client.TestBase;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author hon_him
 * @since 2024-02-21
 */

public class SettingTestBase extends TestBase {

    protected static ReactiveSettings reactiveSettings;

    protected static Settings blockingSettings;

    @BeforeAll
    public static void init() {
        TestBase.init();
        reactiveSettings = reactiveClient.indexes().settings(INDEX);
        blockingSettings = blockingClient.indexes().settings(INDEX);
        cleanData();
        prepareIndex();
    }

}

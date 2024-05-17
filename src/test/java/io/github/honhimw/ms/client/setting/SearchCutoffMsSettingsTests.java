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

import io.github.honhimw.ms.api.CutoffSettings;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-04-17
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchCutoffMsSettingsTests extends SettingTestBase {

    private CutoffSettings _blokcing;

    @BeforeEach
    protected void initIndexes() {
        _blokcing = blockingSettings.cutoff();
    }

    @Order(0)
    @Test
    void get() {
        Optional<Duration> duration = _blokcing.get();
        assert !duration.isPresent() : duration.get();
    }

    @Order(1)
    @Test
    void update() {
        int ms = 30;
        TaskInfo update = _blokcing.update(Duration.ofMillis(ms));
        await(update);
        Optional<Duration> duration = _blokcing.get();
        assert duration.isPresent() && duration.get().toMillis() == 30;
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        Optional<Duration> duration = _blokcing.get();
        assert !duration.isPresent();
    }

}

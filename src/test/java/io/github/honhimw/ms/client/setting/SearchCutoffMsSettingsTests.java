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

import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-04-17
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchCutoffMsSettingsTests extends SettingTestBase {

    @Order(0)
    @Test
    void get() {
        Setting setting = blockingSettings.get();
        Integer searchCutoffMs = setting.getSearchCutoffMs();
        assert Objects.isNull(searchCutoffMs) : searchCutoffMs;
    }

    @Order(1)
    @Test
    void update() {
        int ms = 30;
        TaskInfo update = blockingSettings.update(Setting.builder().searchCutoffMs(ms).build());
        await(update);
        Integer searchCutoffMs = blockingSettings.get().getSearchCutoffMs();
        assert Objects.nonNull(searchCutoffMs) && searchCutoffMs == ms;
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = blockingSettings.reset();
        await(reset);
        Setting setting = blockingSettings.get();
        Integer ms = setting.getSearchCutoffMs();
        assert Objects.isNull(ms);
    }

}

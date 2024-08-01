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

import io.github.honhimw.ms.api.LocalizedAttributesSettings;
import io.github.honhimw.ms.model.LocalizedAttribute;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-08-01
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocalizedAttributesSettingsTests extends SettingTestBase {

    private LocalizedAttributesSettings _blocking;

    @BeforeEach
    protected void initIndexes() {
        _blocking = blockingSettings.localizedAttributes();
    }

    @Order(0)
    @Test
    void get() {
        List<LocalizedAttribute> current = _blocking.get();
        assert current == null;
    }

    @Order(1)
    @Test
    void update() {
        List<LocalizedAttribute> newSetting = toList(LocalizedAttribute.builder()
            .locales(toList())
            .attributePatterns(toList("*"))
            .build());
        TaskInfo update = _blocking.update(newSetting);
        await(update);
        List<LocalizedAttribute> current = _blocking.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blocking.reset();
        await(reset);
        List<LocalizedAttribute> current = _blocking.get();
        assert current == null;
    }

}

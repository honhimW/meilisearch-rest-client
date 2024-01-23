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

package io.github.honhimw.ms.reactive.settings;

import io.github.honhimw.ms.api.DisplayedAttributesSettings;
import io.github.honhimw.ms.api.reactive.ReactiveDisplayedAttributesSettings;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.reactive.ReactiveSettingsTests;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(6)
public class DisplayAttributesSettingsTests extends ReactiveSettingsTests {

    private ReactiveDisplayedAttributesSettings _reactive;
    private DisplayedAttributesSettings _blocking;
    private Object _DEFAULT;

    @BeforeEach
    @Override
    protected void initIndexes() {
        super.initIndexes();
        _reactive = reactiveSettings.displayedAttributes();
        _blocking = blockingSettings.displayedAttributes();
        _DEFAULT = Setting.defaultObject().getDisplayedAttributes();
    }

    @Order(0)
    @Test
    void get() {
        List<String> current = _blocking.get();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(1)
    @Test
    void update() {
        List<String> newSetting = toList("id", "release_date", "title", "poster", "overview", "genre");
        TaskInfo update = _blocking.update(newSetting);
        await(update);
        List<String> current = _blocking.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blocking.reset();
        await(reset);
        List<String> current = _blocking.get();
        assert Objects.equals(current, _DEFAULT);
    }

}

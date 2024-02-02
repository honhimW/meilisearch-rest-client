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

import io.github.honhimw.ms.api.FilterableAttributesSettings;
import io.github.honhimw.ms.api.reactive.ReactiveFilterableAttributesSettings;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.reactive.ReactiveSettingsTests;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
public class FilterableAttributesSettingsTests extends ReactiveSettingsTests {

    private ReactiveFilterableAttributesSettings _reactive;
    private FilterableAttributesSettings _blokcing;
    private Object _DEFAULT;

    @BeforeEach
    @Override
    protected void initIndexes() {
        super.initIndexes();
        _reactive = reactiveSettings.filterableAttributes();
        _blokcing = blockingSettings.filterableAttributes();
        _DEFAULT = Setting.defaultObject().getFilterableAttributes();
    }

    @Order(0)
    @Test
    void get() {
        List<String> current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(1)
    @Test
    void update() {
        List<String> newSetting = toList("title", "release_date");
        TaskInfo update = _blokcing.update(newSetting);
        await(update);
        List<String> current = _blokcing.get();
        Collections.sort(current);
        Collections.sort(newSetting);
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        List<String> current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

}

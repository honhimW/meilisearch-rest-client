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

import io.github.honhimw.ms.api.SynonymsSettings;
import io.github.honhimw.ms.api.reactive.ReactiveSynonymsSettings;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.reactive.ReactiveSettingsTests;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
public class SynonymsSettingsTests extends ReactiveSettingsTests {

    private ReactiveSynonymsSettings _reactive;
    private SynonymsSettings _blokcing;
    private Object _DEFAULT;

    @BeforeEach
    @Override
    protected void initIndexes() {
        super.initIndexes();
        _reactive = reactiveSettings.synonyms();
        _blokcing = blockingSettings.synonyms();
        _DEFAULT = Setting.defaultObject().getSynonyms();
    }

    @Order(0)
    @Test
    void get() {
        Map<String, List<String>> current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(1)
    @Test
    void update() {
        Map<String, List<String>> newSetting = new HashMap<>();
        newSetting.put("hp", toList("harry potter"));
        TaskInfo update = _blokcing.update(newSetting);
        await(update);
        Map<String, List<String>> current = _blokcing.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        Map<String, List<String>> current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

}

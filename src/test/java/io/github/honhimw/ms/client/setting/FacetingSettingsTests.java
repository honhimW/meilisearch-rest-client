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

import io.github.honhimw.ms.api.FacetingSettings;
import io.github.honhimw.ms.api.reactive.ReactiveFacetingSettings;
import io.github.honhimw.ms.model.Faceting;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;

import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FacetingSettingsTests extends SettingTestBase {

    private ReactiveFacetingSettings _reactive;
    private FacetingSettings _blokcing;
    private Object _DEFAULT;

    @BeforeEach
    protected void initIndexes() {
        _reactive = reactiveSettings.faceting();
        _blokcing = blockingSettings.faceting();
        _DEFAULT = Setting.defaultObject().getFaceting();
    }

    @Order(0)
    @Test
    void get() {
        Faceting current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(1)
    @Test
    void update() {
        Faceting newSetting = new Faceting();
        newSetting.setMaxValuesPerFacet(200);
        newSetting.setSortFacetValuesBy(
            toMap(
                toList("*"),
                toList("count")
            ));
        TaskInfo update = _blokcing.update(newSetting);
        await(update);
        Faceting current = _blokcing.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        Faceting current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

}

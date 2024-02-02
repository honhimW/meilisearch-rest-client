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

import io.github.honhimw.ms.api.TypoToleranceSettings;
import io.github.honhimw.ms.api.reactive.ReactiveTypoToleranceSettings;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.TypoTolerance;
import io.github.honhimw.ms.reactive.ReactiveSettingsTests;
import org.junit.jupiter.api.*;

import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
public class TypoToleranceSettingsTests extends ReactiveSettingsTests {

    private ReactiveTypoToleranceSettings _reactive;
    private TypoToleranceSettings _blokcing;
    private Object _DEFAULT;

    @BeforeEach
    @Override
    protected void initIndexes() {
        super.initIndexes();
        _reactive = reactiveSettings.typoTolerance();
        _blokcing = blockingSettings.typoTolerance();
        _DEFAULT = Setting.defaultObject().getTypoTolerance();
    }

    @Order(0)
    @Test
    void get() {
        TypoTolerance current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(1)
    @Test
    void update() {
        TypoTolerance newSetting = TypoTolerance.builder()
                .enabled(true)
                    .minWordSizeForTypos(TypoTolerance.MinWordSizeForTypos.builder()
                        .oneTypo(6)
                        .twoTypos(10)
                        .build())
            .disableOnWords(toList("r18"))
            .disableOnAttributes(toList("title"))
            .build();
        TaskInfo update = _blokcing.update(newSetting);
        await(update);
        TypoTolerance current = _blokcing.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        TypoTolerance current = _blokcing.get();
        assert Objects.equals(current, _DEFAULT);
    }

}

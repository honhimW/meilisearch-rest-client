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

import io.github.honhimw.ms.Experimental;
import io.github.honhimw.ms.api.EmbeddersSettings;
import io.github.honhimw.ms.api.reactive.ReactiveEmbeddersSettings;
import io.github.honhimw.ms.model.Embedder;
import io.github.honhimw.ms.model.ExperimentalFeatures;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.reactive.ReactiveSettingsTests;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@Experimental(features = Experimental.Features.VECTOR_SEARCH)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
public class EmbeddersSettingsTests extends ReactiveSettingsTests {

    private ReactiveEmbeddersSettings _reactive;
    private EmbeddersSettings _blokcing;
    private Object _DEFAULT;

    @BeforeEach
    @Override
    protected void initIndexes() {
        super.initIndexes();
        _reactive = reactiveSettings.embedders();
        _blokcing = blockingSettings.embedders();
        _DEFAULT = Setting.defaultObject().getEmbedders();
        ExperimentalFeatures configure = blockingClient.experimentalFeatures().configure(builder -> builder.vectorStore(true));
        assert configure.getVectorStore();
    }

    @Order(0)
    @Test
    void get() {
        Map<String, ? extends Embedder> current = _blokcing.get();
        assert Objects.isNull(current);
    }

    @Order(1)
    @Test
    void update() {
        Map<String, Embedder> newSetting = new HashMap<>();
        newSetting.put("cs", new Embedder.Custom(3));
        TaskInfo update = _blokcing.update(newSetting);
        await(update);
        Map<String, ? extends Embedder> current = _blokcing.get();
        assert Objects.equals(current, newSetting);
    }

    @Order(2)
    @Test
    void reset() {
        TaskInfo reset = _blokcing.reset();
        await(reset);
        Setting setting = blockingSettings.get();
        Map<String, ? extends Embedder> current = setting.getEmbedders();
        assert Objects.equals(current, _DEFAULT);
    }

    @Order(3)
    @Test
    void disableFeature() {
        ExperimentalFeatures configure = blockingClient.experimentalFeatures().configure(builder -> builder.vectorStore(false));
    }

}

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

package io.github.honhimw.ms.reactive;

import io.github.honhimw.ms.api.Indexes;
import io.github.honhimw.ms.api.Settings;
import io.github.honhimw.ms.api.reactive.ReactiveIndexes;
import io.github.honhimw.ms.api.reactive.ReactiveSettings;
import io.github.honhimw.ms.model.RankingRule;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hon_him
 * @since 2024-01-03
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
public class ReactiveSettingsTests extends ReactiveIndexesTests {

    protected ReactiveIndexes reactiveIndexes;
    protected Indexes blokcingIndexes;

    protected String index = "movie_test";

    protected ReactiveSettings reactiveSettings;
    protected Settings blockingSettings;

    @BeforeEach
    protected void initIndexes() {
        super.initIndexes();
        reactiveIndexes = reactiveClient.indexes();
        reactiveSettings = reactiveIndexes.settings(index);

        blokcingIndexes = blockingClient.indexes();
        blockingSettings = blokcingIndexes.settings(index);
    }

    @Order(0)
    @Test
    void get() {
        Setting current = reactiveSettings.get().block();
        assert Objects.equals(current, Setting.defaultObject());
    }

    @Order(1)
    @Test
    void update() {
        TaskInfo block = reactiveSettings.update(builder -> builder
                .rankingRules(Stream.of(RankingRule.TYPO, RankingRule.WORDS).collect(Collectors.toList()))
                .searchableAttributes(Stream.of("title", "overview").collect(Collectors.toList())))
            .block();
        reactiveClient.tasks().waitForTask(block.getTaskUid()).block();
        Setting current = reactiveSettings.get().block();
        List<RankingRule> rankingRules = current.getRankingRules();
        assert rankingRules.size() == 2;
        assert rankingRules.contains(RankingRule.TYPO);
        assert rankingRules.contains(RankingRule.WORDS);

        List<String> searchableAttributes = current.getSearchableAttributes();
        assert searchableAttributes.size() == 2;
        assert searchableAttributes.contains("title");
        assert searchableAttributes.contains("overview");
    }

    @Order(2)
    @Test
    void reset() {
        Setting current = reactiveSettings.get().block();
        assert !Objects.equals(current, Setting.defaultObject());
        TaskInfo block = reactiveSettings.reset().block();
        await(block);
        current = reactiveSettings.get().block();
        assert Objects.equals(current, Setting.defaultObject());
    }

}

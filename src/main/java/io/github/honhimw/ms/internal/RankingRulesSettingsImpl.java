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

package io.github.honhimw.ms.internal;

import io.github.honhimw.ms.api.RankingRulesSettings;
import io.github.honhimw.ms.api.reactive.ReactiveRankingRulesSettings;
import io.github.honhimw.ms.model.RankingRule;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.ReactorUtils;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class RankingRulesSettingsImpl implements RankingRulesSettings {
    
    private final ReactiveRankingRulesSettings _reactive;

    RankingRulesSettingsImpl(ReactiveRankingRulesSettings reactive) {
        _reactive = reactive;
    }

    @Override
    public List<RankingRule> get() {
        return ReactorUtils.blockNonNull(_reactive.get());
    }

    @Override
    public TaskInfo update(List<RankingRule> rankingRules) {
        return ReactorUtils.blockNonNull(_reactive.update(rankingRules));
    }

    @Override
    public TaskInfo reset() {
        return ReactorUtils.blockNonNull(_reactive.reset());
    }
}

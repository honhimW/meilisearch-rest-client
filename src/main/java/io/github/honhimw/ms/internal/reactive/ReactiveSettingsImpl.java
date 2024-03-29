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

package io.github.honhimw.ms.internal.reactive;

import io.github.honhimw.ms.api.reactive.*;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.support.TypeRefs;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-04
 */

class ReactiveSettingsImpl extends AbstractReactiveImpl implements ReactiveSettings {

    protected final String indexUid;

    protected ReactiveSettingsImpl(ReactiveIndexesImpl indexes, String indexUid) {
        super(indexes._client);
        this.indexUid = indexUid;
    }

    @Override
    public Mono<Setting> get() {
        return get(String.format("/indexes/%s/settings", indexUid), TypeRefs.of(Setting.class));
    }

    @Override
    public Mono<TaskInfo> update(@Nullable Setting setting) {
        if (Objects.isNull(setting)) {
            return reset();
        }
        return patch(String.format("/indexes/%s/settings", indexUid), configurer -> json(configurer, jsonHandler.toJson(setting)), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public Mono<TaskInfo> reset() {
        return delete(String.format("/indexes/%s/settings", indexUid), TypeRefs.TaskInfoRef.INSTANCE);
    }

    @Override
    public ReactiveDictionarySettings dictionary() {
        return new ReactiveDictionarySettingsImpl(this);
    }

    @Override
    public ReactiveDisplayedAttributesSettings displayedAttributes() {
        return new ReactiveDisplayedAttributesSettingsImpl(this);
    }

    @Override
    public ReactiveDistinctAttributeSettings distinctAttribute() {
        return new ReactiveDistinctAttributeSettingsImpl(this);
    }

    @Override
    public ReactiveFacetingSettings faceting() {
        return new ReactiveFacetingSettingsImpl(this);
    }

    @Override
    public ReactiveFilterableAttributesSettings filterableAttributes() {
        return new ReactiveFilterableAttributesSettingsImpl(this);
    }

    @Override
    public ReactivePaginationSettings pagination() {
        return new ReactivePaginationSettingsImpl(this);
    }

    @Override
    public ReactiveProximityPrecisionSettings proximityPrecision() {
        return new ReactiveProximityPrecisionSettingsImpl(this);
    }

    @Override
    public ReactiveRankingRulesSettings rankingRules() {
        return new ReactiveRankingRulesSettingsImpl(this);
    }

    @Override
    public ReactiveSearchableAttributesSettings searchAttributes() {
        return new ReactiveSearchableAttributesSettingsImpl(this);
    }

    @Override
    public ReactiveSeparatorTokensSettings separatorTokens() {
        return new ReactiveSeparatorTokensSettingsImpl(this);
    }

    @Override
    public ReactiveNonSeparatorTokensSettings nonSeparatorTokens() {
        return new ReactiveNonSeparatorTokensSettingsImpl(this);
    }

    @Override
    public ReactiveSortableAttributesSettings sortableAttributes() {
        return new ReactiveSortableAttributesSettingsImpl(this);
    }

    @Override
    public ReactiveStopWordsSettings stopWords() {
        return new ReactiveStopWordsSettingsImpl(this);
    }

    @Override
    public ReactiveSynonymsSettings synonyms() {
        return new ReactiveSynonymsSettingsImpl(this);
    }

    @Override
    public ReactiveTypoToleranceSettings typoTolerance() {
        return new ReactiveTypoToleranceSettingsImpl(this);
    }

    @Override
    public ReactiveEmbeddersSettings embedders() {
        return new ReactiveEmbeddersSettingsImpl(this);
    }
}

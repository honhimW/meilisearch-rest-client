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

import io.github.honhimw.ms.api.*;
import io.github.honhimw.ms.api.reactive.ReactiveSettings;
import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import jakarta.annotation.Nullable;

/**
 * @author hon_him
 * @since 2024-01-04
 */

public class SettingsImpl implements Settings {

    private final ReactiveSettings _settings;

    public SettingsImpl(ReactiveSettings settings) {
        _settings = settings;
    }

    @Override
    public Setting get() {
        return _settings.get().block();
    }

    @Override
    public TaskInfo update(@Nullable Setting setting) {
        return _settings.update(setting).block();
    }

    @Override
    public TaskInfo reset() {
        return _settings.reset().block();
    }

    @Override
    public DictionarySettings dictionary() {
        return new DictionarySettingsImpl(_settings.dictionary());
    }

    @Override
    public DisplayedAttributesSettings displayedAttributes() {
        return new DisplayedAttributesSettingsImpl(_settings.displayedAttributes());
    }

    @Override
    public DistinctAttributeSettings distinctAttribute() {
        return new DistinctAttributeSettingsImpl(_settings.distinctAttribute());
    }

    @Override
    public FacetingSettings faceting() {
        return new FacetingSettingsImpl(_settings.faceting());
    }

    @Override
    public FilterableAttributesSettings filterableAttributes() {
        return new FilterableAttributesSettingsImpl(_settings.filterableAttributes());
    }

    @Override
    public PaginationSettings pagination() {
        return new PaginationSettingsImpl(_settings.pagination());
    }

    @Override
    public RankingRulesSettings rankingRules() {
        return new RankingRulesSettingsImpl(_settings.rankingRules());
    }

    @Override
    public SearchableAttributesSettings searchAttributes() {
        return new SearchableAttributesSettingsImpl(_settings.searchAttributes());
    }

    @Override
    public SeparatorTokensSettings separatorTokens() {
        return new SeparatorTokensSettingsImpl(_settings.separatorTokens());
    }

    @Override
    public NonSeparatorTokensSettings nonSeparatorTokens() {
        return new NonSeparatorTokensSettingsImpl(_settings.nonSeparatorTokens());
    }

    @Override
    public SortableAttributesSettings sortableAttributes() {
        return new SortableAttributesSettingsImpl(_settings.sortableAttributes());
    }

    @Override
    public StopWordsSettings stopWords() {
        return new StopWordsSettingsImpl(_settings.stopWords());
    }

    @Override
    public SynonymsSettings synonyms() {
        return new SynonymsSettingsImpl(_settings.synonyms());
    }

    @Override
    public TypoToleranceSettings typoTolerance() {
        return new TypoToleranceSettingsImpl(_settings.typoTolerance());
    }
}

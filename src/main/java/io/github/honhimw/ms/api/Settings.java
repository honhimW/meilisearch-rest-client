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

package io.github.honhimw.ms.api;

import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;

import java.util.function.Consumer;

/**
 * Settings is a list of all the customization possible for an index.
 * It is possible to update all the settings in one go or individually with the dedicated routes.
 * Updates in the settings route are partial.
 * This means that any parameters not provided in the body will be left unchanged.
 * Updating the settings means overwriting the default settings of Meilisearch.
 * You can reset to default values using the DELETE routes.
 *
 * @author hon_him
 * @since 2024-01-02
 */

public interface Settings {

    /**
     * Get the settings of an index.
     *
     * @return settings detail of current index
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/settings")
    Setting get();

    /**
     * Update the settings of an index.
     * Passing null to an index setting will reset it to its default value.
     * Updates in the settings route are partial. This means that any parameters not provided in the body will be left unchanged.
     * If the provided index does not exist, it will be created.
     *
     * @param setting reset current index setting if null, any parameters not provided in the body will be left unchanged.
     * @return update task
     */
    @Operation(method = "PATCH", tags = "/indexes/{indexUid}/settings")
    TaskInfo update(@Nullable Setting setting);

    @Operation(method = "PATCH", tags = "/indexes/{indexUid}/settings")
    default TaskInfo update(Consumer<Setting.Builder> builder) {
        Setting.Builder _builder = Setting.builder();
        builder.accept(_builder);
        return update(_builder.build());
    }

    /**
     * Reset all the settings of an index to their default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/settings")
    TaskInfo reset();

    /**
     * @return {@link DictionarySettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/dictionary")
    DictionarySettings dictionary();

    /**
     * @return {@link DisplayedAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/displayed-attributes")
    DisplayedAttributesSettings displayedAttributes();

    /**
     * @return {@link DistinctAttributeSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/distinct-attribute")
    DistinctAttributeSettings distinctAttribute();

    /**
     * @return {@link FacetingSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/faceting")
    FacetingSettings faceting();

    /**
     * @return {@link FilterableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/filterable-attributes")
    FilterableAttributesSettings filterableAttributes();

    /**
     * @return {@link PaginationSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    PaginationSettings pagination();

    /**
     * @return {@link RankingRulesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/ranking-rules")
    RankingRulesSettings rankingRules();

    /**
     * @return {@link SearchableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/searchable-attributes")
    SearchableAttributesSettings searchAttributes();

    /**
     * @return {@link SeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/separator-tokens")
    SeparatorTokensSettings separatorTokens();

    /**
     * @return {@link NonSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    NonSeparatorTokensSettings nonSeparatorTokens();

    /**
     * @return {@link SortableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/sortable-attributes")
    SortableAttributesSettings sortableAttributes();

    /**
     * @return {@link StopWordsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/stop-words")
    StopWordsSettings stopWords();

    /**
     * @return {@link SynonymsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/synonyms")
    SynonymsSettings synonyms();

    /**
     * @return {@link TypoToleranceSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/typo-tolerance")
    TypoToleranceSettings typoTolerance();

}

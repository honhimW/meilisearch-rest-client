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

package io.github.honhimw.ms.api.reactive;

import io.github.honhimw.ms.model.Setting;
import io.github.honhimw.ms.model.TaskInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

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

public interface ReactiveSettings {

    /**
     * Get the settings of an index.
     *
     * @return settings detail of current index
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/settings")
    Mono<Setting> get();

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
    Mono<TaskInfo> update(@Nullable Setting setting);

    /**
     * Reset all the settings of an index to their default value.
     *
     * @return reset task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/settings")
    Mono<TaskInfo> reset();

    /**
     * @return {@link ReactiveDictionarySettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/dictionary")
    ReactiveDictionarySettings dictionary();

    /**
     * @return {@link ReactiveDisplayedAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/displayed-attributes")
    ReactiveDisplayedAttributesSettings displayedAttributes();

    /**
     * @return {@link ReactiveDistinctAttributeSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/distinct-attribute")
    ReactiveDistinctAttributeSettings distinctAttribute();

    /**
     * @return {@link ReactiveFacetingSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/faceting")
    ReactiveFacetingSettings faceting();

    /**
     * @return {@link ReactiveFilterableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/filterable-attributes")
    ReactiveFilterableAttributesSettings filterableAttributes();

    /**
     * @return {@link ReactivePaginationSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    ReactivePaginationSettings pagination();

    /**
     * @return {@link ReactiveRankingRulesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/ranking-rules")
    ReactiveRankingRulesSettings rankingRules();

    /**
     * @return {@link ReactiveSearchableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/searchable-attributes")
    ReactiveSearchableAttributesSettings searchAttributes();

    /**
     * @return {@link ReactiveSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/separator-tokens")
    ReactiveSeparatorTokensSettings separatorTokens();

    /**
     * @return {@link ReactiveNonSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    ReactiveNonSeparatorTokensSettings nonSeparatorTokens();

    /**
     * @return {@link ReactiveSortableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/sortable-attributes")
    ReactiveSortableAttributesSettings sortableAttributes();

    /**
     * @return {@link ReactiveStopWordsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/stop-words")
    ReactiveStopWordsSettings stopWords();

    /**
     * @return {@link ReactiveSynonymsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/synonyms")
    ReactiveSynonymsSettings synonyms();

    /**
     * @return {@link ReactiveTypoToleranceSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/typo-tolerance")
    ReactiveTypoToleranceSettings typoTolerance();

}

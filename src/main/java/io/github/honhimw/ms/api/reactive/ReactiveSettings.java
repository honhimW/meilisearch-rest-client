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

import java.util.function.Consumer;
import java.util.function.Function;

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

    @Operation(method = "PATCH", tags = "/indexes/{indexUid}/settings")
    default Mono<TaskInfo> update(Consumer<Setting.Builder> builder) {
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
    Mono<TaskInfo> reset();

    /**
     * @return {@link ReactiveDictionarySettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/dictionary")
    ReactiveDictionarySettings dictionary();

    default <R> R dictionary(Function<ReactiveDictionarySettings, R> operation) {
        return operation.apply(dictionary());
    }

    /**
     * @return {@link ReactiveDisplayedAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/displayed-attributes")
    ReactiveDisplayedAttributesSettings displayedAttributes();

    default <R> R displayedAttributes(Function<ReactiveDisplayedAttributesSettings, R> operation) {
        return operation.apply(displayedAttributes());
    }

    /**
     * @return {@link ReactiveDistinctAttributeSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/distinct-attribute")
    ReactiveDistinctAttributeSettings distinctAttribute();

    default <R> R distinctAttribute(Function<ReactiveDistinctAttributeSettings, R> operation) {
        return operation.apply(distinctAttribute());
    }

    /**
     * @return {@link ReactiveFacetingSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/faceting")
    ReactiveFacetingSettings faceting();

    default <R> R faceting(Function<ReactiveFacetingSettings, R> operation) {
        return operation.apply(faceting());
    }

    /**
     * @return {@link ReactiveFilterableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/filterable-attributes")
    ReactiveFilterableAttributesSettings filterableAttributes();

    default <R> R filterableAttributes(Function<ReactiveFilterableAttributesSettings, R> operation) {
        return operation.apply(filterableAttributes());
    }

    /**
     * @return {@link ReactivePaginationSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    ReactivePaginationSettings pagination();

    default <R> R pagination(Function<ReactivePaginationSettings, R> operation) {
        return operation.apply(pagination());
    }

    /**
     * @return {@link ReactiveProximityPrecisionSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/proximity-precision")
    ReactiveProximityPrecisionSettings proximityPrecision();

    default <R> R proximityPrecision(Function<ReactiveProximityPrecisionSettings, R> operation) {
        return operation.apply(proximityPrecision());
    }

    /**
     * @return {@link ReactiveRankingRulesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/ranking-rules")
    ReactiveRankingRulesSettings rankingRules();

    default <R> R rankingRules(Function<ReactiveRankingRulesSettings, R> operation) {
        return operation.apply(rankingRules());
    }

    /**
     * @return {@link ReactiveSearchableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/searchable-attributes")
    ReactiveSearchableAttributesSettings searchAttributes();

    default <R> R searchAttributes(Function<ReactiveSearchableAttributesSettings, R> operation) {
        return operation.apply(searchAttributes());
    }

    /**
     * @return {@link ReactiveSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/separator-tokens")
    ReactiveSeparatorTokensSettings separatorTokens();

    default <R> R separatorTokens(Function<ReactiveSeparatorTokensSettings, R> operation) {
        return operation.apply(separatorTokens());
    }

    /**
     * @return {@link ReactiveNonSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    ReactiveNonSeparatorTokensSettings nonSeparatorTokens();

    default <R> R nonSeparatorTokens(Function<ReactiveNonSeparatorTokensSettings, R> operation) {
        return operation.apply(nonSeparatorTokens());
    }

    /**
     * @return {@link ReactiveSortableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/sortable-attributes")
    ReactiveSortableAttributesSettings sortableAttributes();

    default <R> R sortableAttributes(Function<ReactiveSortableAttributesSettings, R> operation) {
        return operation.apply(sortableAttributes());
    }

    /**
     * @return {@link ReactiveStopWordsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/stop-words")
    ReactiveStopWordsSettings stopWords();

    default <R> R stopWords(Function<ReactiveStopWordsSettings, R> operation) {
        return operation.apply(stopWords());
    }

    /**
     * @return {@link ReactiveSynonymsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/synonyms")
    ReactiveSynonymsSettings synonyms();

    default <R> R synonyms(Function<ReactiveSynonymsSettings, R> operation) {
        return operation.apply(synonyms());
    }

    /**
     * @return {@link ReactiveTypoToleranceSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/typo-tolerance")
    ReactiveTypoToleranceSettings typoTolerance();

    default <R> R typoTolerance(Function<ReactiveTypoToleranceSettings, R> operation) {
        return operation.apply(typoTolerance());
    }

    /**
     * @return {@link ReactiveEmbeddersSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/embedders")
    ReactiveEmbeddersSettings embedders();

    default <R> R embedders(Function<ReactiveEmbeddersSettings, R> operation) {
        return operation.apply(embedders());
    }



}

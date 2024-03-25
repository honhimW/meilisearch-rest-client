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

    /**
     * Update the settings of an index.
     *
     * @param builder setting builder
     * @return update task
     */
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
     * Get the dictionary settings of current index.
     *
     * @return {@link ReactiveDictionarySettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/dictionary")
    ReactiveDictionarySettings dictionary();

    /**
     * Applies the given operation to the dictionary settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R dictionary(Function<ReactiveDictionarySettings, R> operation) {
        return operation.apply(dictionary());
    }

    /**
     * Get the displayed attributes settings of current index.
     *
     * @return {@link ReactiveDisplayedAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/displayed-attributes")
    ReactiveDisplayedAttributesSettings displayedAttributes();

    /**
     * Applies the given operation to the displayed attributes settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R displayedAttributes(Function<ReactiveDisplayedAttributesSettings, R> operation) {
        return operation.apply(displayedAttributes());
    }

    /**
     * Get the distinct attribute settings of current index.
     *
     * @return {@link ReactiveDistinctAttributeSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/distinct-attribute")
    ReactiveDistinctAttributeSettings distinctAttribute();

    /**
     * Applies the given operation to the distinct attribute settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R distinctAttribute(Function<ReactiveDistinctAttributeSettings, R> operation) {
        return operation.apply(distinctAttribute());
    }

    /**
     * Get the faceting settings of current index.
     *
     * @return {@link ReactiveFacetingSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/faceting")
    ReactiveFacetingSettings faceting();

    /**
     * Applies the given operation to the faceting settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R faceting(Function<ReactiveFacetingSettings, R> operation) {
        return operation.apply(faceting());
    }

    /**
     * Get the filterable attributes settings of current index.
     *
     * @return {@link ReactiveFilterableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/filterable-attributes")
    ReactiveFilterableAttributesSettings filterableAttributes();

    /**
     * Applies the given operation to the filterable attributes settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R filterableAttributes(Function<ReactiveFilterableAttributesSettings, R> operation) {
        return operation.apply(filterableAttributes());
    }

    /**
     * Get the pagination settings of current index.
     *
     * @return {@link ReactivePaginationSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    ReactivePaginationSettings pagination();

    /**
     * Applies the given operation to the pagination settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R pagination(Function<ReactivePaginationSettings, R> operation) {
        return operation.apply(pagination());
    }

    /**
     * Get the proximity precision settings of current index.
     *
     * @return {@link ReactiveProximityPrecisionSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/proximity-precision")
    ReactiveProximityPrecisionSettings proximityPrecision();

    /**
     * Applies the given operation to the proximity precision settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R proximityPrecision(Function<ReactiveProximityPrecisionSettings, R> operation) {
        return operation.apply(proximityPrecision());
    }

    /**
     * Get the ranking rules settings of current index.
     *
     * @return {@link ReactiveRankingRulesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/ranking-rules")
    ReactiveRankingRulesSettings rankingRules();

    /**
     * Applies the given operation to the ranking rules settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R rankingRules(Function<ReactiveRankingRulesSettings, R> operation) {
        return operation.apply(rankingRules());
    }

    /**
     * Get the searchable attributes settings of current index.
     *
     * @return {@link ReactiveSearchableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/searchable-attributes")
    ReactiveSearchableAttributesSettings searchAttributes();

    /**
     * Applies the given operation to the searchable attributes settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R searchAttributes(Function<ReactiveSearchableAttributesSettings, R> operation) {
        return operation.apply(searchAttributes());
    }

    /**
     * Get the separator tokens settings of current index.
     *
     * @return {@link ReactiveSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/separator-tokens")
    ReactiveSeparatorTokensSettings separatorTokens();

    /**
     * Applies the given operation to the separator tokens settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R separatorTokens(Function<ReactiveSeparatorTokensSettings, R> operation) {
        return operation.apply(separatorTokens());
    }

    /**
     * Get the non separator tokens settings of current index.
     *
     * @return {@link ReactiveNonSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    ReactiveNonSeparatorTokensSettings nonSeparatorTokens();

    /**
     * Applies the given operation to the non separator tokens settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R nonSeparatorTokens(Function<ReactiveNonSeparatorTokensSettings, R> operation) {
        return operation.apply(nonSeparatorTokens());
    }

    /**
     * Get the sortable attributes settings of current index.
     *
     * @return {@link ReactiveSortableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/sortable-attributes")
    ReactiveSortableAttributesSettings sortableAttributes();

    /**
     * Applies the given operation to the sortable attributes settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R sortableAttributes(Function<ReactiveSortableAttributesSettings, R> operation) {
        return operation.apply(sortableAttributes());
    }

    /**
     * Get the stop words settings of current index.
     *
     * @return {@link ReactiveStopWordsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/stop-words")
    ReactiveStopWordsSettings stopWords();

    /**
     * Applies the given operation to the stop words settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R stopWords(Function<ReactiveStopWordsSettings, R> operation) {
        return operation.apply(stopWords());
    }

    /**
     * Get the synonyms settings of current index.
     *
     * @return {@link ReactiveSynonymsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/synonyms")
    ReactiveSynonymsSettings synonyms();

    /**
     * Applies the given operation to the synonyms settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R synonyms(Function<ReactiveSynonymsSettings, R> operation) {
        return operation.apply(synonyms());
    }

    /**
     * Get the typo tolerance settings of current index.
     *
     * @return {@link ReactiveTypoToleranceSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/typo-tolerance")
    ReactiveTypoToleranceSettings typoTolerance();

    /**
     * Applies the given operation to the typo tolerance settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R typoTolerance(Function<ReactiveTypoToleranceSettings, R> operation) {
        return operation.apply(typoTolerance());
    }

    /**
     * Get the embedders settings of current index.
     *
     * @return {@link ReactiveEmbeddersSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/embedders")
    ReactiveEmbeddersSettings embedders();

    /**
     * Applies the given operation to the embedders settings of the index.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the result of the operation
     */
    default <R> R embedders(Function<ReactiveEmbeddersSettings, R> operation) {
        return operation.apply(embedders());
    }


}

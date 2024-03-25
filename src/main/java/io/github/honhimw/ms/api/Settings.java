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

    /**
     * Update the settings of an index.
     *
     * @param builder setting builder
     * @return update task
     */
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
     * Get the settings of an index.
     *
     * @return {@link DictionarySettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/dictionary")
    DictionarySettings dictionary();

    /**
     * Applies the given operation to the dictionary settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R dictionary(Function<DictionarySettings, R> operation) {
        return operation.apply(dictionary());
    }

    /**
     * Get the displayed attributes settings of an index.
     *
     * @return {@link DisplayedAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/displayed-attributes")
    DisplayedAttributesSettings displayedAttributes();

    /**
     * Applies the given operation to the displayed attributes settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R displayedAttributes(Function<DisplayedAttributesSettings, R> operation) {
        return operation.apply(displayedAttributes());
    }

    /**
     * Get the distinct attribute settings of an index.
     *
     * @return {@link DistinctAttributeSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/distinct-attribute")
    DistinctAttributeSettings distinctAttribute();

    /**
     * Applies the given operation to the distinct attribute settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R distinctAttribute(Function<DistinctAttributeSettings, R> operation) {
        return operation.apply(distinctAttribute());
    }

    /**
     * Get the faceting settings of an index.
     *
     * @return {@link FacetingSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/faceting")
    FacetingSettings faceting();

    /**
     * Applies the given operation to the faceting settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R faceting(Function<FacetingSettings, R> operation) {
        return operation.apply(faceting());
    }

    /**
     * Get the filterable attributes settings of an index.
     *
     * @return {@link FilterableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/filterable-attributes")
    FilterableAttributesSettings filterableAttributes();

    /**
     * Applies the given operation to the filterable attributes settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R filterableAttributes(Function<FilterableAttributesSettings, R> operation) {
        return operation.apply(filterableAttributes());
    }

    /**
     * Get the pagination settings of an index.
     *
     * @return {@link PaginationSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    PaginationSettings pagination();

    /**
     * Applies the given operation to the pagination settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R pagination(Function<PaginationSettings, R> operation) {
        return operation.apply(pagination());
    }

    /**
     * Get the proximity precision settings of an index.
     *
     * @return {@link ProximityPrecisionSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/pagination")
    ProximityPrecisionSettings proximityPrecision();

    /**
     * Applies the given operation to the proximity precision settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R proximityPrecision(Function<ProximityPrecisionSettings, R> operation) {
        return operation.apply(proximityPrecision());
    }

    /**
     * Get the ranking rules settings of an index.
     *
     * @return {@link RankingRulesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/ranking-rules")
    RankingRulesSettings rankingRules();

    /**
     * Applies the given operation to the ranking rules settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R rankingRules(Function<RankingRulesSettings, R> operation) {
        return operation.apply(rankingRules());
    }

    /**
     * Get the searchable attributes settings of an index.
     *
     * @return {@link SearchableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/searchable-attributes")
    SearchableAttributesSettings searchAttributes();

    /**
     * Applies the given operation to the searchable attributes settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R searchAttributes(Function<SearchableAttributesSettings, R> operation) {
        return operation.apply(searchAttributes());
    }

    /**
     * Get the separator tokens settings of an index.
     *
     * @return {@link SeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/separator-tokens")
    SeparatorTokensSettings separatorTokens();

    /**
     * Applies the given operation to the separator tokens settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R separatorTokens(Function<SeparatorTokensSettings, R> operation) {
        return operation.apply(separatorTokens());
    }

    /**
     * Get the non separator tokens settings of an index.
     *
     * @return {@link NonSeparatorTokensSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/non-separator-tokens")
    NonSeparatorTokensSettings nonSeparatorTokens();

    /**
     * Applies the given operation to the non separator tokens settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R nonSeparatorTokens(Function<NonSeparatorTokensSettings, R> operation) {
        return operation.apply(nonSeparatorTokens());
    }

    /**
     * Get the sortable attributes settings of an index.
     *
     * @return {@link SortableAttributesSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/sortable-attributes")
    SortableAttributesSettings sortableAttributes();

    /**
     * Applies the given operation to the sortable attributes settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R sortableAttributes(Function<SortableAttributesSettings, R> operation) {
        return operation.apply(sortableAttributes());
    }

    /**
     * Get the stop words settings of an index.
     *
     * @return {@link StopWordsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/stop-words")
    StopWordsSettings stopWords();

    /**
     * Applies the given operation to the stop words settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R stopWords(Function<StopWordsSettings, R> operation) {
        return operation.apply(stopWords());
    }

    /**
     * Get the synonyms settings of an index.
     *
     * @return {@link SynonymsSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/synonyms")
    SynonymsSettings synonyms();

    /**
     * Applies the given operation to the synonyms settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R synonyms(Function<SynonymsSettings, R> operation) {
        return operation.apply(synonyms());
    }

    /**
     * Get the typo tolerance settings of an index.
     *
     * @return {@link TypoToleranceSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/typo-tolerance")
    TypoToleranceSettings typoTolerance();

    /**
     * Applies the given operation to the typo tolerance settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R typoTolerance(Function<TypoToleranceSettings, R> operation) {
        return operation.apply(typoTolerance());
    }

    /**
     * Get the embedders settings of an index.
     *
     * @return {@link EmbeddersSettings} operator
     */
    @Operation(tags = "/indexes/{index_uid}/settings/embedders")
    EmbeddersSettings embedders();

    /**
     * Applies the given operation to the embedders settings.
     *
     * @param operation operation
     * @param <R>       return type
     * @return the operation result
     */
    default <R> R embedders(Function<EmbeddersSettings, R> operation) {
        return operation.apply(embedders());
    }

}

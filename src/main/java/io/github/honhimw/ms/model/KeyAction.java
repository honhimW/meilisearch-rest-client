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

package io.github.honhimw.ms.model;

import io.github.honhimw.ms.json.EnumValue;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public enum KeyAction implements EnumValue<KeyAction> {
    SEARCH("search", "Provides access to both POST and GET search endpoints"),
    DOCUMENTS_ADD("documents.add", "Provides access to the add documents and update documents endpoints"),
    DOCUMENTS_GET("documents.get", "Provides access to the get one document, get documents with POST, and get documents with GET endpoints endpoints"),
    DOCUMENTS_DELETE("documents.delete", "Provides access to the delete one document, delete all documents, batch delete, and delete by filter endpoints"),
    INDEXES_CREATE("indexes.create", "Provides access to the create index endpoint"),
    INDEXES_GET("indexes.get", "Provides access to the get one index and list all indexes endpoints. Non-authorized indexes will be omitted from the response"),
    INDEXES_UPDATE("indexes.update", "Provides access to the update index endpoint"),
    INDEXES_DELETE("indexes.delete", "Provides access to the delete index endpoint"),
    INDEXES_SWAP("indexes.swap", "Provides access to the swap indexes endpoint. Non-authorized indexes will not be swapped"),
    TASKS_GET("tasks.get", "Provides access to the get one task and get tasks endpoints. Tasks from non-authorized indexes will be omitted from the response"),
    TASKS_CANCEL("tasks.cancel", "Provides access to the cancel tasks endpoint. Tasks from non-authorized indexes will not be canceled"),
    TASKS_DELETE("tasks.delete", "Provides access to the delete tasks endpoint. Tasks from non-authorized indexes will not be deleted"),
    SETTINGS_GET("settings.get", "Provides access to the get settings endpoint and equivalents for all subroutes"),
    SETTINGS_UPDATE("settings.update", "Provides access to the update settings and reset settings endpoints and equivalents for all subroutes"),
    STATS_GET("stats.get", "Provides access to the get stats of an index endpoint and the get stats of all indexes endpoint. For the latter, non-authorized indexes are omitted from the response"),
    DUMPS_CREATE("dumps.create", "Provides access to the create dump endpoint. Not restricted by indexes"),
    SNAPSHOTS_CREATE("snapshots.create", "Provides access to the create snapshot endpoint. Not restricted by indexes"),
    VERSION("version", "Provides access to the get Meilisearch version endpoint"),
    KEYS_GET("keys.get", "Provides access to the get all keys endpoint"),
    KEYS_CREATE("keys.create", "Provides access to the create key endpoint"),
    KEYS_UPDATE("keys.update", "Provides access to the update key endpoint"),
    KEYS_DELETE("keys.delete", "Provides access to the delete key endpoint"),
    ;

    public final String keyAction;
    private final String description;

    KeyAction(String keyAction, String description) {
        this.keyAction = keyAction;
        this.description = description;
    }

    @Override
    public String value() {
        return this.keyAction;
    }

}

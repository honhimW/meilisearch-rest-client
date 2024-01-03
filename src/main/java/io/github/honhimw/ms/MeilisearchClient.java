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

package io.github.honhimw.ms;

import io.github.honhimw.ms.api.*;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * @author hon_him
 * @since 2023-07-24
 */

public class MeilisearchClient {

    private final String serverUrl;

    private final String apiKey;

    private final JsonHandler jsonHandler;

    public MeilisearchClient(MeilisearchConfig config) {
        this.serverUrl = config.getServerUrl();
        this.apiKey = config.getApiKey();
        this.jsonHandler = config.getJsonHandler();
    }

    /*
    ==========================================================================
    Indexes
    ==========================================================================
     */

    /**
     * TODO
     * @return {@link Indexes} operator
     */
    public Indexes indexes() {
        return null;
    }

    /**
     * TODO
     * @return {@link Tasks} operator
     */
    public Tasks tasks() {
        return null;
    }

    /**
     * TODO
     * @return {@link Keys} operator
     */
    public Keys keys() {
        // TODO
        return null;
    }

    public List<SearchResponse> multiSearch(MultiSearchRequest request) {
        // TODO
        return null;
    }

    public void healthy() {
        // TODO
        throw new IllegalStateException("server status not ['available']");
    }

    public Version version() {
        // TODO
        return null;
    }

    /**
     * <a href="https://www.meilisearch.com/docs/reference/api/dump"><h1>Dumps</h1></a>
     * The /dumps route allows the creation of database dumps. Dumps are .dump files that can be used to restore Meilisearch data or migrate between different versions.
     * <p>
     * Triggers a dump creation task. Once the process is complete, a dump is created in the dump directory. If the dump directory does not exist yet, it will be created.
     * <p>
     * Dump tasks take priority over all other tasks in the queue. This means that a newly created dump task will be processed as soon as the current task is finished.
     *
     */
    @Operation(method = "POST", tags = "/dumps")
    public TaskInfo dumps() {
        // TODO
        return null;
    }

    /**
     * <a href="https://www.meilisearch.com/docs/reference/api/snapshots"><h1>Snapshots</h1></a>
     * The /snapshot route allows you to create database snapshots. Snapshots are .snapshot files that can be used to make quick backups of Meilisearch data.
     * <p>
     * <a style="color:red" href="https://www.meilisearch.com/docs/learn/advanced/snapshots">Learn more about snapshots.</a>
     * <p>
     * Snapshot tasks take priority over other tasks in the queue.
     */
    @Operation(method = "POST", tags = "/snapshots")
    public TaskInfo snapshots() {
        // TODO
        return null;
    }

    /**
     * @return {@link ExperimentalFeaturesSettings} operator
     */
    public ExperimentalFeaturesSettings experimentalFeatures() {
        // TODO
        return null;
    }

}

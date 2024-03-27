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

import io.github.honhimw.ms.MSearchConfig;
import io.github.honhimw.ms.api.reactive.Logs;
import io.github.honhimw.ms.internal.MSearchClientImpl;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public interface MSearchClient extends AutoCloseable {

    /**
     * Get the indexes instance, hold and share the client inside, will not cache the instance by default.
     *
     * @return {@link Indexes}
     */
    Indexes indexes();

    /**
     * Function to apply on {@link Indexes}. Get the result fluently.
     *
     * @param operation fluent operation
     * @param <R>       return type
     * @return fluent operation return
     */
    default <R> R indexes(Function<Indexes, R> operation) {
        return operation.apply(indexes());
    }

    /**
     * Get the tasks instance, hold and share the client inside, will not cache the instance by default.
     *
     * @return {@link Tasks}
     */
    Tasks tasks();

    /**
     * Function to apply on {@link Tasks}. Get the result fluently.
     *
     * @param operation fluent operation
     * @param <R>       return type
     * @return fluent operation return
     */
    default <R> R tasks(Function<Tasks, R> operation) {
        return operation.apply(tasks());
    }

    /**
     * Get the keys instance, hold and share the client inside, will not cache the instance by default.
     *
     * @return {@link Keys}
     */
    Keys keys();

    /**
     * Function to apply on {@link Keys}. Get the result fluently.
     *
     * @param operation fluent operation
     * @param <R>       return type
     * @return fluent operation return
     */
    default <R> R keys(Function<Keys, R> operation) {
        return operation.apply(keys());
    }

    /**
     * The /multi-search route allows you
     * to perform multiple search queries on one or more indexes by bundling them into a single HTTP request.
     * Multi-search is also known as federated search.
     * <p>
     * Perform a multi-search
     * <p>
     * Bundle multiple search queries in a single API request. Use this endpoint to search through multiple indexes at once.
     * <p style="color:orange;font-size:large">WARNING</p>
     * <pre>
     * If Meilisearch encounters an error when handling any of the queries in a multi-search request, it immediately stops processing the request and returns an error message. The returned message will only address the first error encountered.
     * </pre>
     *
     * @param request multi-search request
     * @return multi-search result
     */
    @Operation(method = "POST", tags = "/multi-search")
    List<SearchResponse<Map<String, Object>>> multiSearch(MultiSearchRequest request);

    /**
     * The /health route allows you to verify the status and availability of a Meilisearch instance.
     * <p>
     * Get health of Meilisearch server.
     */
    default void healthy() {
        throw new IllegalStateException("server status not ['available']");
    }

    /**
     * The /version route allows you to check the version of a running Meilisearch instance.
     *
     * @return Meilisearch server version information.
     */
    Version version();

    /**
     * <a style="font-size:large" href="https://www.meilisearch.com/docs/reference/api/dump">Dumps</a>
     * <p>
     * The /dumps route allows the creation of database dumps. Dumps are .dump files that can be used to restore Meilisearch data or migrate between different versions.
     * <p>
     * Triggers a dump creation task. Once the process is complete, a dump is created in the dump directory. If the dump directory does not exist yet, it will be created.
     * <p>
     * Dump tasks take priority over all other tasks in the queue. This means that a newly created dump task will be processed as soon as the current task is finished.
     *
     * @return TaskInfo dumps task info
     */
    TaskInfo dumps();

    /**
     * <a style="font-size:large" href="https://www.meilisearch.com/docs/reference/api/snapshots">Snapshots</a>
     * <p>
     * The /snapshot route allows you to create database snapshots. Snapshots are .snapshot files that can be used to make quick backups of Meilisearch data.
     * <p>
     * <a style="color:red" href="https://www.meilisearch.com/docs/learn/advanced/snapshots">Learn more about snapshots.</a>
     * <p>
     * Snapshot tasks take priority over other tasks in the queue.
     *
     * @return TaskInfo snapshot task info
     */
    TaskInfo snapshots();

    /**
     * Get the logs instance, hold and share the client inside, will not cache the instance by default.
     *
     * @return {@link Logs}
     */
    Logs logs();

    /**
     * Get the experimentalFeatures instance, hold and share the client inside, will not cache the instance by default.
     *
     * @return {@link ExperimentalFeaturesSettings}
     */
    ExperimentalFeaturesSettings experimentalFeatures();

    /**
     * Function to apply on {@link ExperimentalFeaturesSettings}. Get the result fluently.
     *
     * @param operation fluent operation
     * @param <R>       return type
     * @return fluent operation return
     */
    default <R> R experimentalFeatures(Function<ExperimentalFeaturesSettings, R> operation) {
        return operation.apply(experimentalFeatures());
    }

    /**
     * Create a blocking-client.
     *
     * @param config client configuration
     * @return blocking-client
     */
    static MSearchClient create(MSearchConfig config) {
        return new MSearchClientImpl(config);
    }

    /**
     * Create a blocking-client with default config.
     *
     * @param configure client configure
     * @return blocking-client
     */
    static MSearchClient create(Consumer<MSearchConfig.Builder> configure) {
        MSearchConfig.Builder builder = MSearchConfig.withDefault();
        configure.accept(builder);
        MSearchConfig config = builder.build();
        return new MSearchClientImpl(config);
    }

    /**
     * Close current client.
     */
    @Override
    void close();
}

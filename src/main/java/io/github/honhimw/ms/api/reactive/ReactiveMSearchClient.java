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

import io.github.honhimw.ms.MSearchConfig;
import io.github.honhimw.ms.internal.reactive.ReactiveMSearchClientImpl;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public interface ReactiveMSearchClient {

    ReactiveIndexes indexes();

    default <R> R indexes(Function<ReactiveIndexes, R> operation) {
        return operation.apply(indexes());
    }

    ReactiveTasks tasks();

    default <R> R tasks(Function<ReactiveTasks, R> operation) {
        return operation.apply(tasks());
    }

    ReactiveKeys keys();

    default <R> R keys(Function<ReactiveKeys, R> operation) {
        return operation.apply(keys());
    }

    @Operation(method = "POST", tags = "/multi-search")
    Mono<List<SearchResponse>> multiSearch(MultiSearchRequest request);

    @Operation(method = "GET", tags = "/health")
    default Mono<Void> healthy() {
        throw new IllegalStateException("server status not ['available']");
    }

    @Operation(method = "GET", tags = "/version")
    Mono<Version> version();

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
    Mono<TaskInfo> dumps();

    /**
     * <a href="https://www.meilisearch.com/docs/reference/api/snapshots"><h1>Snapshots</h1></a>
     * The /snapshot route allows you to create database snapshots. Snapshots are .snapshot files that can be used to make quick backups of Meilisearch data.
     * <p>
     * <a style="color:red" href="https://www.meilisearch.com/docs/learn/advanced/snapshots">Learn more about snapshots.</a>
     * <p>
     * Snapshot tasks take priority over other tasks in the queue.
     */
    @Operation(method = "POST", tags = "/snapshots")
    Mono<TaskInfo> snapshots();

    ReactiveExperimentalFeaturesSettings experimentalFeatures();

    default <R> R experimentalFeatures(Function<ReactiveExperimentalFeaturesSettings, R> operation) {
        return operation.apply(experimentalFeatures());
    }

    static ReactiveMSearchClient create(MSearchConfig config) {
        return new ReactiveMSearchClientImpl(config);
    }

    static ReactiveMSearchClient create(Consumer<MSearchConfig.Builder> configure) {
        MSearchConfig.Builder builder = MSearchConfig.withDefault();
        configure.accept(builder);
        MSearchConfig config = builder.build();
        return new ReactiveMSearchClientImpl(config);
    }

}

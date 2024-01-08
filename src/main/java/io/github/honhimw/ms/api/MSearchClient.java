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
import io.github.honhimw.ms.internal.MSearchClientImpl;
import io.github.honhimw.ms.model.MultiSearchRequest;
import io.github.honhimw.ms.model.SearchResponse;
import io.github.honhimw.ms.model.TaskInfo;
import io.github.honhimw.ms.model.Version;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public interface MSearchClient {

    Indexes indexes();

    default <R> R indexes(Function<Indexes, R> operation) {
        return operation.apply(indexes());
    }

    Tasks tasks();

    default <R> R tasks(Function<Tasks, R> operation) {
        return operation.apply(tasks());
    }

    Keys keys();

    default <R> R keys(Function<Keys, R> operation) {
        return operation.apply(keys());
    }

    List<SearchResponse> multiSearch(MultiSearchRequest request);

    default void healthy() {
        throw new IllegalStateException("server status not ['available']");
    }

    Version version();

    /**
     * <a href="https://www.meilisearch.com/docs/reference/api/dump"><h1>Dumps</h1></a>
     * The /dumps route allows the creation of database dumps. Dumps are .dump files that can be used to restore Meilisearch data or migrate between different versions.
     * <p>
     * Triggers a dump creation task. Once the process is complete, a dump is created in the dump directory. If the dump directory does not exist yet, it will be created.
     * <p>
     * Dump tasks take priority over all other tasks in the queue. This means that a newly created dump task will be processed as soon as the current task is finished.
     *
     */
    TaskInfo dumps();

    /**
     * <a href="https://www.meilisearch.com/docs/reference/api/snapshots"><h1>Snapshots</h1></a>
     * The /snapshot route allows you to create database snapshots. Snapshots are .snapshot files that can be used to make quick backups of Meilisearch data.
     * <p>
     * <a style="color:red" href="https://www.meilisearch.com/docs/learn/advanced/snapshots">Learn more about snapshots.</a>
     * <p>
     * Snapshot tasks take priority over other tasks in the queue.
     */
    TaskInfo snapshots();

    ExperimentalFeaturesSettings experimentalFeatures();

    default <R> R experimentalFeatures(Function<ExperimentalFeaturesSettings, R> operation) {
        return operation.apply(experimentalFeatures());
    }

    static MSearchClient create(MSearchConfig config) {
        return new MSearchClientImpl(config);
    }

    static MSearchClient create(Consumer<MSearchConfig.Builder> configure) {
        MSearchConfig.Builder builder = MSearchConfig.withDefault();
        configure.accept(builder);
        MSearchConfig config = builder.build();
        return new MSearchClientImpl(config);
    }

}

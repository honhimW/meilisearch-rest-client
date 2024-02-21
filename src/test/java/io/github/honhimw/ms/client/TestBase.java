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

package io.github.honhimw.ms.client;

import io.github.honhimw.ms.MeiliSearchProperties;
import io.github.honhimw.ms.api.MSearchClient;
import io.github.honhimw.ms.api.Tasks;
import io.github.honhimw.ms.api.reactive.ReactiveMSearchClient;
import io.github.honhimw.ms.api.reactive.ReactiveTasks;
import io.github.honhimw.ms.json.JacksonJsonHandler;
import io.github.honhimw.ms.json.JsonHandler;
import io.github.honhimw.ms.model.TaskInfo;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author hon_him
 * @since 2023-12-29
 */

public class TestBase {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final String INDEX = "movie_test";
    protected static ReactiveMSearchClient reactiveClient;
    protected static MSearchClient blockingClient;

    private static ReactiveTasks reactiveTasks;
    private static Tasks blockingTasks;

    protected static JsonHandler jsonHandler;

    @BeforeAll
    public static void init() {
        jsonHandler = new JacksonJsonHandler();
        reactiveClient = ReactiveMSearchClient.create(builder -> builder
            .host(MeiliSearchProperties.getHost())
            .port(MeiliSearchProperties.getPort())
            .apiKey(MeiliSearchProperties.getApiKey())
            .jsonHandler(jsonHandler)
        );
        blockingClient = MSearchClient.create(builder -> builder
            .host(MeiliSearchProperties.getHost())
            .port(MeiliSearchProperties.getPort())
            .apiKey(MeiliSearchProperties.getApiKey())
            .jsonHandler(jsonHandler)
        );
    }

    protected static Mono<Void> await(Mono<TaskInfo> taskInfo) {
        return taskInfo.flatMap(taskInfo1 -> getReactiveTasks().waitForTask(taskInfo1.getTaskUid()));
    }

    protected static void await(TaskInfo taskInfo) {
        getBlockingTasks().waitForTask(taskInfo.getTaskUid());
    }

    @SafeVarargs
    protected static <T> List<T> toList(T... elements) {
        if (Objects.isNull(elements) || elements.length == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(elements));
    }

    protected static <K, V> Map<K, V> toMap(List<K> keys, List<V> values) {
        Objects.requireNonNull(keys);
        Objects.requireNonNull(values);
        assert keys.size() == values.size();
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = values.get(i);
            map.put(key, value);
        }
        return map;
    }

    protected static ReactiveTasks getReactiveTasks() {
        if (Objects.isNull(reactiveTasks)) {
            reactiveTasks = reactiveClient.tasks();
        }
        return reactiveTasks;
    }

    protected static Tasks getBlockingTasks() {
        if (Objects.isNull(blockingTasks)) {
            blockingTasks = blockingClient.tasks();
        }
        return blockingTasks;
    }

    protected static void cleanData() {
        TaskInfo delete = blockingClient.indexes().delete(INDEX);
        await(delete);
    }

    protected static void prepareIndex() {
        TaskInfo createIndex = blockingClient.indexes().create(INDEX);
        await(createIndex);
    }

    protected static void prepareData() {
        cleanData();
        prepareIndex();
        TaskInfo task = blockingClient.indexes().documents(INDEX).save(movies);
        await(task);
    }

    public static final String movies = "[\n" +
        "{\"id\":2,\"title\":\"Ariel\",\"overview\":\"Taisto Kasurinen is a Finnish coal miner whose father has just committed suicide and who is framed for a crime he did not commit. In jail, he starts to dream about leaving the country and starting a new life. He escapes from prison but things don't go as planned...\",\"genres\":[\"Drama\",\"Crime\",\"Comedy\"],\"poster\":\"https://image.tmdb.org/t/p/w500/ojDg0PGvs6R9xYFodRct2kdI6wC.jpg\",\"release_date\":593395200},\n" +
        "{\"id\":5,\"title\":\"Four Rooms\",\"overview\":\"It's Ted the Bellhop's first night on the job...and the hotel's very unusual guests are about to place him in some outrageous predicaments. It seems that this evening's room service is serving up one unbelievable happening after another.\",\"genres\":[\"Crime\",\"Comedy\"],\"poster\":\"https://image.tmdb.org/t/p/w500/75aHn1NOYXh4M7L5shoeQ6NGykP.jpg\",\"release_date\":818467200},\n" +
        "{\"id\":6,\"title\":\"Judgment Night\",\"overview\":\"While racing to a boxing match, Frank, Mike, John and Rey get more than they bargained for. A wrong turn lands them directly in the path of Fallon, a vicious, wise-cracking drug lord. After accidentally witnessing Fallon murder a disloyal henchman, the four become his unwilling prey in a savage game of cat & mouse as they are mercilessly stalked through the urban jungle in this taut suspense drama\",\"genres\":[\"Action\",\"Thriller\",\"Crime\"],\"poster\":\"https://image.tmdb.org/t/p/w500/rYFAvSPlQUCebayLcxyK79yvtvV.jpg\",\"release_date\":750643200},\n" +
        "{\"id\":11,\"title\":\"Star Wars\",\"overview\":\"Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire.\",\"genres\":[\"Adventure\",\"Action\",\"Science Fiction\"],\"poster\":\"https://image.tmdb.org/t/p/w500/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg\",\"release_date\":233366400},\n" +
        "{\"id\":12,\"title\":\"Finding Nemo\",\"overview\":\"Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist's office aquarium. It's up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way.\",\"genres\":[\"Animation\",\"Family\"],\"poster\":\"https://image.tmdb.org/t/p/w500/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg\",\"release_date\":1054252800},\n" +
        "{\"id\":13,\"title\":\"Forrest Gump\",\"overview\":\"A man with a low IQ has accomplished great things in his life and been present during significant historic events—in each case, far exceeding what anyone imagined he could do. But despite all he has achieved, his one true love eludes him.\",\"genres\":[\"Comedy\",\"Drama\",\"Romance\"],\"poster\":\"https://image.tmdb.org/t/p/w500/h5J4W4veyxMXDMjeNxZI46TsHOb.jpg\",\"release_date\":773452800},\n" +
        "{\"id\":14,\"title\":\"American Beauty\",\"overview\":\"Lester Burnham, a depressed suburban father in a mid-life crisis, decides to turn his hectic life around after developing an infatuation with his daughter's attractive friend.\",\"genres\":[\"Drama\"],\"poster\":\"https://image.tmdb.org/t/p/w500/wby9315QzVKdW9BonAefg8jGTTb.jpg\",\"release_date\":937353600},\n" +
        "{\"id\":15,\"title\":\"Citizen Kane\",\"overview\":\"Newspaper magnate, Charles Foster Kane is taken from his mother as a boy and made the ward of a rich industrialist. As a result, every well-meaning, tyrannical or self-destructive move he makes for the rest of his life appears in some way to be a reaction to that deeply wounding event.\",\"genres\":[\"Mystery\",\"Drama\"],\"poster\":\"https://image.tmdb.org/t/p/w500/zO5OI25cieQ6ncpvGOD4U72vi1o.jpg\",\"release_date\":-905990400},\n" +
        "{\"id\":16,\"title\":\"Dancer in the Dark\",\"overview\":\"Selma, a Czech immigrant on the verge of blindness, struggles to make ends meet for herself and her son, who has inherited the same genetic disorder and will suffer the same fate without an expensive operation. When life gets too difficult, Selma learns to cope through her love of musicals, escaping life's troubles - even if just for a moment - by dreaming up little numbers to the rhythmic beats of her surroundings.\",\"genres\":[\"Drama\",\"Crime\"],\"poster\":\"https://image.tmdb.org/t/p/w500/9rsivF4sWfmBzrNr4LPu6TNJhXX.jpg\",\"release_date\":958521600},\n" +
        "{\"id\":17,\"title\":\"The Dark\",\"overview\":\"Adèle and her daughter Sarah are traveling on the Welsh coastline to see her husband James when Sarah disappears. A different but similar looking girl appears who says she died in a past time. Adèle tries to discover what happened to her daughter as she is tormented by Celtic mythology from the past.\",\"genres\":[\"Horror\",\"Thriller\",\"Mystery\"],\"poster\":\"https://image.tmdb.org/t/p/w500/wZeBHVnCvaS2bwkb8jFQ0PwZwXq.jpg\",\"release_date\":1127865600},\n" +
        "{\"id\":18,\"title\":\"The Fifth Element\",\"overview\":\"In 2257, a taxi driver is unintentionally given the task of saving a young girl who is part of the key that will ensure the survival of humanity.\",\"genres\":[\"Adventure\",\"Fantasy\",\"Action\",\"Thriller\",\"Science Fiction\"],\"poster\":\"https://image.tmdb.org/t/p/w500/fPtlCO1yQtnoLHOwKtWz7db6RGU.jpg\",\"release_date\":862531200},\n" +
        "{\"id\":19,\"title\":\"Metropolis\",\"overview\":\"In a futuristic city sharply divided between the working class and the city planners, the son of the city's mastermind falls in love with a working class prophet who predicts the coming of a savior to mediate their differences.\",\"genres\":[\"Drama\",\"Science Fiction\"],\"poster\":\"https://image.tmdb.org/t/p/w500/hUK9rewffKGqtXynH5SW3v9hzcu.jpg\",\"release_date\":-1353888000},\n" +
        "{\"id\":20,\"title\":\"My Life Without Me\",\"overview\":\"A fatally ill mother with only two months to live creates a list of things she wants to do before she dies without telling her family of her illness.\",\"genres\":[\"Drama\",\"Romance\"],\"poster\":\"https://image.tmdb.org/t/p/w500/sFSkn5rrQqXJkRNa2rMWqzmEuhR.jpg\",\"release_date\":1046995200}\n" +
        "]\n";

}

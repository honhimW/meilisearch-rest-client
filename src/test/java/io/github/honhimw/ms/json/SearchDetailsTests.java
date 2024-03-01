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

package io.github.honhimw.ms.json;

import io.github.honhimw.ms.client.TestBase;
import io.github.honhimw.ms.model.SearchDetails;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author hon_him
 * @since 2024-02-22
 */

public class SearchDetailsTests {

    @Test
    void serialize() {
        List<JsonHandler> handlers = TestBase.toList(new JacksonJsonHandler(), new GsonJsonHandler());
        for (JsonHandler jsonHandler : handlers) {
            SearchDetails searchDetails = jsonHandler.fromJson(JSON, SearchDetails.class);
            System.out.println(searchDetails);
            assert searchDetails.get_rankingScoreDetails().getAttribute().getAttributes_ranking_order() == 0.8;
        }
    }

    public static final String JSON = "{\"_rankingScoreDetails\": {\n" +
        "  \"words\": {\n" +
        "    \"order\": 0,\n" +
        "    \"matchingWords\": 1,\n" +
        "    \"maxMatchingWords\": 1,\n" +
        "    \"score\": 1.0\n" +
        "  },\n" +
        "  \"typo\": {\n" +
        "    \"order\": 1,\n" +
        "    \"typoCount\": 0,\n" +
        "    \"maxTypoCount\": 1,\n" +
        "    \"score\": 1.0\n" +
        "  },\n" +
        "  \"proximity\": {\n" +
        "    \"order\": 2,\n" +
        "    \"score\": 1.0\n" +
        "  },\n" +
        "  \"attribute\": {\n" +
        "    \"order\": 3,\n" +
        "    \"attributes_ranking_order\": 0.8,\n" +
        "    \"attributes_query_word_order\": 0.6363636363636364,\n" +
        "    \"score\": 0.7272727272727273\n" +
        "  },\n" +
        "  \"exactness\": {\n" +
        "    \"order\": 4,\n" +
        "    \"matchType\": \"noExactMatch\",\n" +
        "    \"matchingWords\": 0,\n" +
        "    \"maxMatchingWords\": 1,\n" +
        "    \"score\": 0.3333333333333333\n" +
        "  }\n" +
        "}}";

}

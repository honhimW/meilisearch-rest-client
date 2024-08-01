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

import io.github.honhimw.ms.support.FilterBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <a style="font-weight:bold;font-size:x-large" href="https://meilisearch.notion.site/Update-Documents-by-Function-0cff8fea7655436592e7c8a6de932062#2e859108257d4e0191ce7b655f4dc169">Update Documents by Function</a>
 * <p>
 * You can edit documents by executing a Rhai function on all the documents of your database or a subset of them that you can select by a Meilisearch filter.
 * <p style="color:orange;font-weight:bold;font-size:large">WARNING</p>
 * <pre>
 * {@code Running scripts on all documents is discouraged. Where possible, we recommend pre-filtering using a Meilisearch filter.}
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Upper Case and Add Sparkles around Movie Titles</p>
 * <pre>
 * By indexing the movies dataset you can then run the following Rhai function on all of them.
 * This function will uppercase the titles of the movies with an {@code id > 3000} and add sparkles around it.
 * All of that by leveraging the Rhai templating syntax.
 * {@code
 * POST /indexes/movies/documents/edit
 * content-type: application/json
 *
 * {
 *     "filter": "id > 3000",
 *     "function": "doc.title = `* ${doc.title.to_upper()} *`"
 * }
 * }
 * Titles will go from Ariel to * ARIEL * or Star Wars to * STAR WARS *.
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Delete documents</p>
 * <pre>
 * By setting the doc variable to () (also known as NULL or nil), Meilisearch will delete the associated document.
 * The following example will delete every document with an even id.
 * {@code
 * if doc.id % 2 == 0 {
 * 	doc = ()
 * }
 * }
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">User-defined Context</p>
 * <pre>
 * Sometimes, it's useful to maintain the same function but introduce parameters.
 * You can do this using the context parameter.
 * For example, in this scenario, we delete all documents with an ID greater than the context idmax and add sparkles to the titles of the remaining documents.
 * {@code
 * POST /indexes/movies/documents/edit
 * content-type: application/json
 *
 * {
 *     "context": { "idmax": 50 },
 *     "function": "
 * 	    if doc.id >= context.idmax {
 * 		    doc = ()
 * 		} else {
 * 			  doc.title = `* ${doc.title} *`
 * 		}
 * 		"
 * }
 * }
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Decaying Ranking Strategy</p>
 * <pre>
 * Some users want to implement a decay ranking system, often referred to as the Reddit-style or HackerNews-style post ranking.
 * By following the formula found in this comprehensive article, you can achieve the same with Meilisearch.
 * Sorting by ranking_score:desc on the first page of your website will provide the desired ranking.
 * However, remember that the score remains static.
 * Therefore, depending on your needs, you should set up this function in a cron job to run every minute, hour, or day.
 * {@code
 * # posted_at and now are Unix Epoch timestamps in seconds
 * # and must, therefore, be converted to hours (/ 60 / 60).
 * POST /indexes/movies/documents/edit
 * content-type: application/json
 *
 * {
 *     "context": { "now": 1715423249 },
 *     "function": "
 * 	    let age_hours = (context.now - doc.posted_at) / 60 / 60;
 * 	    doc.ranking_score = doc.upvotes ** 0.8 / (age_hours + 2) ** 1.8;
 * 		"
 * }
 * }
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Create Nested Objects</p>
 * <pre>
 * In Rhai, accessing the field of an object doesn’t create it.
 * To create a nested object in a document, you must use the #{} object syntax to create the objects individually.
 * {@code
 * doc._vectors = #{
 *   "default": #{
 *     embeddings: [1, 2, 3, 4]
 *   }
 * }
 * }
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Format in Title Case</p>
 * <pre>
 * Always using the movies dataset from above.
 * You can define and use functions in your scripts to modify the document.
 * You can find this script on a GitHub gist and copy it on the Rhai playground.
 * {@code
 * POST /indexes/movies/documents/edit
 * content-type: application/json
 *
 * {
 *     "function": "
 * 	    fn to_title_case() {
 * 	      let title_cased = \"\";
 * 	      let must_upper_case = true;
 * 	      for c in this.to_chars() {
 * 	        if c == \" \" {
 * 	          must_upper_case = true;
 * 	          title_cased += \" \";
 * 	        } else if must_upper_case {
 * 	          title_cased += c.to_upper();
 * 	          must_upper_case = false;
 * 	        } else {
 * 	          title_cased += c.to_lower();
 * 	        }
 * 	      }
 * 	      this = title_cased;
 * 	    }
 *
 * 	    doc.title.to_title_case();
 * 		"
 * }
 * }
 * </pre>
 * <p style="color:green;font-weight:bold;font-size:large">Group Documents Scores</p>
 * <pre>
 * Meilisearch uses Ranking Rules to sort documents.
 * It groups them by score, and when a rule determines that multiple documents are equal, it gives the whole group the next ranking rule.
 * However, users often sort their documents by price or date before the default relevancy ranking rules, e.g., `words`, `proximity`, and `attributes`.
 * This is bad practice as the documents will always end up in different groups, often with a single document.
 * Relevancy ranking rules are useless as sorting a single document has no effect.
 *
 * In this example, the Rhai function shows that you can create a dynamic group according to the actual price of your documents.
 * You'll get better and more relevant results by sorting your documents using the `price_group` field before using the relevancy rules.
 * {@code
 * // This function groups prices (this) by bigger groups.
 * fn group_price() {
 *   return if this > 1000 {
 *     return 1000
 *   } else if this > 500 {
 *     return 500
 *   } else if this > 250 {
 *     return 250
 *   } else if this > 100 {
 *     return 100
 *   } else if this > 50 {
 *     return 50
 *   } else {
 *     return 0
 *   }
 * }
 *
 * doc.price_group = doc.price.group_price();
 * }
 * </pre>
 *
 *
 * @author hon_him
 * @since 2024-08-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EditRequest extends FilterableAttributesRequest implements Serializable {

    /**
     * a Rhai function
     */
    @Schema(description = "a Rhai function script")
    private String function;

    /**
     * a map of context variables
     */
    @Schema(description = "a map of context variables")
    private Map<String, Object> context;

    @Override
    public EditRequest filter(Consumer<FilterBuilder> consumer) {
        super.filter(consumer);
        return this;
    }

    @Override
    public EditRequest filter(String filter) {
        super.filter(filter);
        return this;
    }

}

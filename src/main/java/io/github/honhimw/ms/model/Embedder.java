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

import io.github.honhimw.ms.Experimental;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hon_him
 * @since 2024-01-18
 */

@Experimental(feature = "vector-search")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Embedder implements Serializable {

    @Schema(description = "Embedders generate vector data from your documents.")
    private EmbedderSource source;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    public static class OpenAI extends Embedder {

        public OpenAI() {
            super(EmbedderSource.OPEN_AI);
        }

        @Schema(description = "It is mandatory to pass an OpenAI API key through the OPENAI_API_KEY environment variable or the apiKey field when using an OpenAI embedder. Generate an API key from your OpenAI account. Use tier 2 keys or above for optimal performance.")
        private String apiKey;
        
        @Schema(description = "model", example = "text-embedding-ada-002")
        private String model;

        /**
         * <h2 style="color:green">`documentTemplate` usage</h2>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * <p>
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    public static class HuggingFace extends Embedder {

        public HuggingFace() {
            super(EmbedderSource.HUGGING_FACE);
        }

        @Schema(description = "model", example = "bge-base-en-v1.5")
        private String model;

        /**
         * <h2 style="color:green">`documentTemplate` usage</h2>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * <p>
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    public static class Custom extends Embedder {

        public Custom() {
            super(EmbedderSource.USER_PROVIDED);
        }

        @Schema(description = "dimensions")
        private Integer dimensions;

    }

}
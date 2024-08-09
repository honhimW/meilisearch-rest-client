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
import io.github.honhimw.ms.api.annotation.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Embedder
 *
 * @author hon_him
 * @since 2024-01-18
 */

@Experimental(features = Experimental.Features.VECTOR_SEARCH)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Embedder implements Serializable {

    /**
     * Create an embedder with embedder source.
     *
     * @param source embedder source
     */
    public Embedder(EmbedderSource source) {
        this.source = source;
    }

    /**
     * Embedders generate vector data from your documents.
     */
    @Schema(description = "(.*?)")
    private EmbedderSource source;

    /**
     * Describes the natural distribution of results
     *
     * @since v1.8
     */
    @Schema(description = "describes the natural distribution of results")
    private Distribution distribution;

    /**
     * Describes the natural distribution of results
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static final class Distribution implements Serializable {

        /**
         * mean value
         */
        @Schema(description = "mean value")
        private Double mean;

        /**
         * variance
         */
        @Schema(description = "variance")
        private Double sigma;
    }

    /**
     * OpenAI embedder
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpenAI extends Embedder {

        /**
         * Create an OpenAI embedder with embedder source.
         */
        public OpenAI() {
            super(EmbedderSource.OPEN_AI);
        }

        /**
         * Create an OpenAI embedder with embedder source. All arguments constructor.
         *
         * @param apiKey           api key
         * @param model            model
         * @param documentTemplate document template
         */
        public OpenAI(String apiKey, String model, String documentTemplate) {
            this();
            this.apiKey = apiKey;
            this.model = model;
            this.documentTemplate = documentTemplate;
        }

        /**
         * It is mandatory to pass an OpenAI API key through the OPENAI_API_KEY environment variable or the apiKey field when using an OpenAI embedder. Generate an API key from your OpenAI account. Use tier 2 keys or above for optimal performance.
         */
        @Schema(description = "It is mandatory to pass an OpenAI API key through the OPENAI_API_KEY environment variable or the apiKey field when using an OpenAI embedder. Generate an API key from your OpenAI account. Use tier 2 keys or above for optimal performance.")
        private String apiKey;

        /**
         * model
         */
        @Schema(description = "model", example = "text-embedding-ada-002")
        private String model;

        /**
         * <p style="color:green;font-weight:bold;font-size:large">`documentTemplate` usage</p>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

    }

    /**
     * Hugging Face embedder
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class HuggingFace extends Embedder {

        /**
         * Create an Hugging Face embedder with embedder source.
         */
        public HuggingFace() {
            super(EmbedderSource.HUGGING_FACE);
        }

        /**
         * Create an Hugging Face embedder with embedder source. All arguments constructor.
         *
         * @param model            model
         * @param documentTemplate document template
         */
        public HuggingFace(String model, String documentTemplate) {
            this();
            this.model = model;
            this.documentTemplate = documentTemplate;
        }

        /**
         * model
         */
        @Schema(description = "model", example = "bge-base-en-v1.5")
        private String model;

        /**
         * <p style="color:green;font-weight:bold;font-size:large">`documentTemplate` usage</p>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

    }

    /**
     * User provided embedder
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Custom extends Embedder {

        /**
         * Create an Custom embedder with embedder source.
         */
        public Custom() {
            super(EmbedderSource.USER_PROVIDED);
        }

        /**
         * Create an Custom embedder with embedder source. All arguments constructor.
         *
         * @param dimensions dimensions
         */
        public Custom(Integer dimensions) {
            this();
            this.dimensions = dimensions;
        }

        /**
         * dimensions
         */
        @Schema(description = "dimensions")
        private Integer dimensions;

    }

    /**
     * Rest embedder
     *
     * @since v1.8
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Rest extends Embedder {

        /**
         * Create an Rest embedder with embedder source.
         */
        public Rest() {
            super(EmbedderSource.REST);
        }

        /**
         * Mandatory, full URL to the embedding endpoint. Must be parseable as an URL
         */
        @Schema(description = "Mandatory, full URL to the embedding endpoint. Must be parseable as an URL")
        private String url;

        /**
         * Optional, will be passed as Bearer in the Authorization header
         */
        @Schema(description = "Optional, will be passed as Bearer in the Authorization header")
        private String apiKey;

        /**
         * Optional, inferred with a dummy request if missing
         */
        @Schema(description = "Optional, inferred with a dummy request if missing")
        private Integer dimensions;

        /**
         * <p style="color:green;font-weight:bold;font-size:large">`documentTemplate` usage</p>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

        /**
         * Optional, defaults to `{}`, A JSON object describing other fields to send in a query
         * <pre>
         * {
         *   "model": "name-of-your-model",
         *   "prompt": "{{text}}"
         * }
         * </pre>
         */
        @Schema(description = "A JSON object describing other fields to send in a query")
        private Map<String, Object> request;

        /**
         * Optional, defaults to `{}`, A JSON object describing other fields to send in a query
         * <pre>
         * {
         *   "embedding": "{{embedding}}"
         * }
         * </pre>
         */
        @Schema(description = "A JSON object describing other fields to send in a query")
        private Map<String, Object> response;

    }

    /**
     * Ollama embedder
     *
     * @since v1.8
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Ollama extends Embedder {

        /**
         * Create an Ollama embedder with embedder source.
         */
        public Ollama() {
            super(EmbedderSource.OLLAMA);
        }

        /**
         * Optional, fetched from environment if missing.
         * If url is not passed, the server URL is fetched from the `MEILI_OLLAMA_URL`  environment variable
         * and defaults to http://localhost:11434/api/embeddings (the default for an ollama server)
         */
        @Schema(description = "Optional, fetched from environment if missing")
        private String url;

        /**
         * Optional.
         * If apiKey is passed, then an Authorization: Bearer header will be added to the requests to the ollama server.
         * While this is not used by the ollama server directly,
         * it is a common practice to have publicly accessible ollama servers behind a reverse proxy that can provide this kind of authentication.
         */
        @Schema(description = "Optional")
        private String apiKey;

        /**
         * model
         */
        @Schema(description = "model", example = "nomic-embed-text")
        private String model;

        /**
         * <p style="color:green;font-weight:bold;font-size:large">`documentTemplate` usage</p>
         * <pre>
         * documentTemplate must be a Liquid template. Use {{ doc.attribute }} to access the attribute field value of your documents. Any field you refer to in this way must exist in all documents or an error will be raised at indexing time.
         * For best results, use short strings indicating the type of document in that index, only include highly relevant document fields, and truncate long fields.
         * </pre>
         */
        @Schema(description = "an optional field you can use to customize the data you send to the embedder. It is highly recommended you configure a custom template for your documents.")
        private String documentTemplate;

    }

}

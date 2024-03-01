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

import io.github.honhimw.ms.Experimental;
import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Documents are objects composed of fields that can store any type of data.
 * Each field contains an attribute and its associated value.
 * Documents are stored inside indexes.
 * Learn more about documents.
 *
 * @author hon_him
 * @since 2023-12-31
 */

public interface ReactiveDocuments {

    /**
     * Get documents by batch.
     *
     * @param offset default 0
     * @param limit  default 20
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default Mono<Page<Map<String, Object>>> list(@Nullable Integer offset, @Nullable Integer limit) {
        return list(request -> {
            request.setOffset(offset);
            request.setLimit(limit);
        });
    }

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default Mono<Page<Map<String, Object>>> list(Consumer<GetDocumentRequest> page) {
        GetDocumentRequest pageRequest = new GetDocumentRequest();
        page.accept(pageRequest);
        return list(pageRequest);
    }

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    Mono<Page<Map<String, Object>>> list(GetDocumentRequest page);

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Mono<Page<T>> list(@Nullable Integer offset, @Nullable Integer limit, TypeRef<T> typeRef);

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default <T> Mono<Page<T>> list(@Nullable Integer offset, @Nullable Integer limit, Class<T> type) {
        return list(offset, limit, TypeRef.of(type));
    }

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Mono<Page<T>> list(GetDocumentRequest page, TypeRef<T> typeRef);

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default <T> Mono<Page<T>> list(Consumer<GetDocumentRequest> page, TypeRef<T> typeRef) {
        GetDocumentRequest pageRequest = new GetDocumentRequest();
        page.accept(pageRequest);
        return list(pageRequest, typeRef);
    }

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default <T> Mono<Page<T>> list(Consumer<GetDocumentRequest> page, Class<T> type) {
        return list(page, TypeRef.of(type));
    }


    /**
     * Add a list of documents or replace them if they already exist.
     * <p/>
     * If you send an already existing document (same id)
     * the whole existing document will be overwritten by the new document.
     * Fields previously in the document not present in the new document are removed.
     * <p/>
     * For a partial update of the document see Add or update documents route.
     * <ul>
     *     <li>info If the provided index does not exist, it will be created.</li>
     *     <li>_geo is an object made of lat and lng field.</li>
     *     <li>Use the reserved _vectors arrays of floats to add embeddings to a document. _vectors is an array of floats or multiple arrays of floats in an outer array.</li>
     * </ul>
     *
     * @param json json formatted array
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> save(String json);

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    default Mono<TaskInfo> save(Object one) {
        return save(Collections.singleton(one));
    }

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> save(Collection<?> collection);

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    default Mono<TaskInfo> saveVectorized(VectorizedDocument one) {
        return save(Collections.singleton(one));
    }

    @Experimental(features = Experimental.Features.VECTOR_SEARCH)
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> saveVectorized(Collection<VectorizedDocument> collection);

    /**
     * Add a list of documents or update them if they already exist.
     * <p/>
     * If you send an already existing document (same id)
     * the old document will be only partially updated according to the fields of the new document.
     * Thus, any fields not present in the new document are kept and remained unchanged.
     * <p/>
     * To completely overwrite a document, see Add or replace documents route.
     * <ul>
     *     <li>info If the provided index does not exist, it will be created.</li>
     *     <li>info Use the reserved _geo object to add geo coordinates to a document. _geo is an object made of lat and lng field.</li>
     *     <li>Use the reserved _vectors arrays of floats to add embeddings to a document. _vectors is an array of floats or multiple arrays of floats in an outer array.</li>
     * </ul>
     *
     * @param json json formatted array
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> update(String json);

    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    default Mono<TaskInfo> update(Object one) {
        return update(Collections.singleton(one));
    }

    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> update(Collection<?> collection);

    /**
     * Delete all documents in the specified index.
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents")
    Mono<TaskInfo> deleteAll();

    /**
     * Get documents by batch.
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    Mono<Page<Map<String, Object>>> batchGet(BatchGetDocumentsRequest fetch);

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    default Mono<Page<Map<String, Object>>> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder) {
        BatchGetDocumentsRequest.Builder _builder = BatchGetDocumentsRequest.builder();
        builder.accept(_builder);
        return batchGet(_builder.build());
    }

    /**
     * Get documents by batch.
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    <T> Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef);

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    default <T> Mono<Page<T>> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder, TypeRef<T> typeRef) {
        BatchGetDocumentsRequest.Builder _builder = BatchGetDocumentsRequest.builder();
        builder.accept(_builder);
        return batchGet(_builder.build(), typeRef);
    }

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    default <T> Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch, Class<T> type) {
        return batchGet(fetch, TypeRef.of(type));
    }

    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    default <T> Mono<Page<T>> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder, Class<T> type) {
        BatchGetDocumentsRequest.Builder _builder = BatchGetDocumentsRequest.builder();
        builder.accept(_builder);
        return batchGet(_builder.build(), TypeRef.of(type));
    }

    /**
     * Delete a set of documents based on an array of document ids.
     *
     * @param ids An array of numbers containing the unique ids of the documents to be deleted.
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/delete-batch")
    Mono<TaskInfo> batchDelete(List<String> ids);

    /**
     * Delete a set of documents based on a filter.
     *
     * @param filter A filter expression written as a string or array of array of strings for the documents to be deleted.
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/delete")
    Mono<TaskInfo> delete(FilterableAttributesRequest filter);

    /**
     * Get one document using its unique id.
     *
     * @param id     Document id of the requested document
     * @param fields Comma-separated list of fields to display for an API resource.
     *               By default it contains all fields of an API resource.
     *               Default *.
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    Mono<Map<String, Object>> get(String id, @Nullable String... fields);

    /**
     * Get one document using its unique id.
     *
     * @param id     Document id of the requested document
     * @param fields Comma-separated list of fields to display for an API resource.
     *               By default it contains all fields of an API resource.
     *               Default *.
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    <T> Mono<T> get(String id, TypeRef<T> typeRef, @Nullable String... fields);

    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    default <T> Mono<T> get(String id, Class<T> type, @Nullable String... fields) {
        return get(id, TypeRef.of(type), fields);
    }

    /**
     * Delete one document based on its unique id.
     *
     * @param id Document id of the requested document
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents/{documentId}")
    Mono<TaskInfo> delete(String id);


}

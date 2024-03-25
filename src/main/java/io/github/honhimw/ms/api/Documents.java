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

import io.github.honhimw.ms.json.TypeRef;
import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

public interface Documents {

    /**
     * Get documents by batch.
     *
     * @param offset default 0
     * @param limit  default 20
     * @return paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    Page<Map<String, Object>> list(@Nullable Integer offset, @Nullable Integer limit);

    /**
     * Get documents by batch.
     *
     * @param page parameter builder
     * @return paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    Page<Map<String, Object>> list(Consumer<GetDocumentRequest> page);

    /**
     * Get documents by batch.
     *
     * @param page parameter builder
     * @return paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    Page<Map<String, Object>> list(GetDocumentRequest page);

    /**
     * Get documents by batch.
     *
     * @param limit   default 20
     * @param offset  default 0
     * @param typeRef document type
     * @param <T>     type
     * @return typed paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Page<T> list(@Nullable Integer offset, @Nullable Integer limit, TypeRef<T> typeRef);

    /**
     * Get documents by batch. And deserialize to a specific type.
     *
     * @param offset default 0
     * @param limit  default 20
     * @param type   document type
     * @param <T>    type
     * @return typed paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Page<T> list(@Nullable Integer offset, @Nullable Integer limit, Class<T> type);

    /**
     * Get documents by batch. And deserialize to a specific type.
     *
     * @param page    parameter builder
     * @param typeRef document type reference
     * @param <T>     type
     * @return typed paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Page<T> list(Consumer<GetDocumentRequest> page, TypeRef<T> typeRef);

    /**
     * Get documents by batch. And deserialize to a specific type.
     *
     * @param page parameter builder
     * @param type document type
     * @param <T>  type
     * @return typed paged result
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    <T> Page<T> list(Consumer<GetDocumentRequest> page, Class<T> type);

    /**
     * Add a list of documents or replace them if they already exist.
     * <p>
     * If you send an already existing document (same id)
     * the whole existing document will be overwritten by the new document.
     * Fields previously in the document not present in the new document are removed.
     * <p>
     * For a partial update of the document see Add or update documents route.
     * <ul>
     *     <li>info If the provided index does not exist, it will be created.</li>
     *     <li>_geo is an object made of lat and lng field.</li>
     *     <li>Use the reserved _vectors arrays of floats to add embeddings to a document. _vectors is an array of floats or multiple arrays of floats in an outer array.</li>
     * </ul>
     *
     * @param json json formatted array
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo save(@Nullable String json);

    /**
     * Save one document.
     *
     * @param one document
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo save(Object one);

    /**
     * Save a collection of documents.
     *
     * @param collection documents
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo save(Collection<?> collection);

    /**
     * Save one vectorized document.
     *
     * @param one vectorized document
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo saveVectorized(VectorizedDocument one);

    /**
     * Save a collection of vectorized documents.
     *
     * @param collection vectorized documents
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo saveVectorized(Collection<VectorizedDocument> collection);


    /**
     * Add a list of documents or update them if they already exist.
     * <p>
     * If you send an already existing document (same id)
     * the old document will be only partially updated according to the fields of the new document.
     * Thus, any fields not present in the new document are kept and remained unchanged.
     * <p>
     * To completely overwrite a document, see Add or replace documents route.
     * <ul>
     *     <li>info If the provided index does not exist, it will be created.</li>
     *     <li>info Use the reserved _geo object to add geo coordinates to a document. _geo is an object made of lat and lng field.</li>
     *     <li>Use the reserved _vectors arrays of floats to add embeddings to a document. _vectors is an array of floats or multiple arrays of floats in an outer array.</li>
     * </ul>
     *
     * @param json json formatted array
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo update(String json);

    /**
     * Update one document.
     *
     * @param one document
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo update(Object one);

    /**
     * Update a collection of documents.
     *
     * @param collection documents
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    TaskInfo update(Collection<?> collection);

    /**
     * Delete all documents in the specified index.
     *
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents")
    TaskInfo deleteAll();

    /**
     * Get documents by batch.
     *
     * @param fetch request
     * @return paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    Page<Map<String, Object>> batchGet(BatchGetDocumentsRequest fetch);

    /**
     * Get documents by batch.
     *
     * @param builder parameter builder
     * @return paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    Page<Map<String, Object>> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder);

    /**
     * Get documents by batch.
     *
     * @param fetch   request
     * @param typeRef document type reference
     * @param <T>     document type
     * @return typed paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    <T> Page<T> batchGet(BatchGetDocumentsRequest fetch, TypeRef<T> typeRef);

    /**
     * Get documents by batch.
     *
     * @param builder parameter builder
     * @param typeRef document type reference
     * @param <T>     document type
     * @return typed paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    <T> Page<T> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder, TypeRef<T> typeRef);

    /**
     * Get documents by batch.
     *
     * @param fetch request
     * @param type  document type
     * @param <T>   document type
     * @return typed paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    <T> Page<T> batchGet(BatchGetDocumentsRequest fetch, Class<T> type);

    /**
     * Get documents by batch.
     *
     * @param builder parameter builder
     * @param type    document type
     * @param <T>     document type
     * @return typed paged result
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    <T> Page<T> batchGet(Consumer<BatchGetDocumentsRequest.Builder> builder, Class<T> type);

    /**
     * Delete a set of documents based on an array of document ids.
     *
     * @param ids An array of numbers containing the unique ids of the documents to be deleted.
     * @return delete task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/delete-batch")
    TaskInfo batchDelete(List<String> ids);

    /**
     * Delete a set of documents based on a filter.
     *
     * @param filter A filter expression written as a string or array of array of strings for the documents to be deleted.
     * @return delete task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/delete")
    TaskInfo delete(FilterableAttributesRequest filter);

    /**
     * Get one document using its unique id.
     *
     * @param id     Document id of the requested document
     * @param fields Comma-separated list of fields to display for an API resource.
     *               By default it contains all fields of an API resource.
     *               Default *.
     * @return document
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    Optional<Map<String, Object>> get(String id, @Nullable String... fields);

    /**
     * Get one document using its unique id.
     *
     * @param id      Document id of the requested document
     * @param typeRef document type reference
     * @param fields  Comma-separated list of fields to display for an API resource.
     *                By default it contains all fields of an API resource.
     *                Default *.
     * @param <T>     document type
     * @return typed document
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    <T> Optional<T> get(String id, TypeRef<T> typeRef, @Nullable String... fields);

    /**
     * Get one document using its unique id.
     *
     * @param id     Document id of the requested document
     * @param type   document type
     * @param fields Comma-separated list of fields to display for an API resource.
     *               By default it contains all fields of an API resource.
     *               Default *.
     * @param <T>    document type
     * @return typed document
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    <T> Optional<T> get(String id, Class<T> type, @Nullable String... fields);

    /**
     * Delete one document based on its unique id.
     *
     * @param id Document id of the requested document
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents/{documentId}")
    TaskInfo delete(String id);


}

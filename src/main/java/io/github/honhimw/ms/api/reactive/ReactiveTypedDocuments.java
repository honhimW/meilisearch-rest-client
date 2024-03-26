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

import io.github.honhimw.ms.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
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

public interface ReactiveTypedDocuments<T> {

    /**
     * Get documents by batch.
     *
     * @param offset default 0
     * @param limit  default 20
     * @return paginated documents
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default Mono<Page<T>> list(@Nullable Integer offset, @Nullable Integer limit) {
        return list(request -> {
            request.setOffset(offset);
            request.setLimit(limit);
        });
    }

    /**
     * Get documents by batch.
     *
     * @param page request
     * @return paginated documents
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    Mono<Page<T>> list(GetDocumentRequest page);

    /**
     * Get documents by batch.
     *
     * @param page request builder
     * @return paginated documents
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents")
    default Mono<Page<T>> list(Consumer<GetDocumentRequest> page) {
        GetDocumentRequest pageRequest = new GetDocumentRequest();
        page.accept(pageRequest);
        return list(pageRequest);
    }

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
    Mono<TaskInfo> save(String json);

    /**
     * Save a document
     *
     * @param t document
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    default Mono<TaskInfo> save(T t) {
        return save(Collections.singleton(t));
    }

    /**
     * Save a collection of documents
     *
     * @param collection documents
     * @return save task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> save(Collection<? extends T> collection);

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
    Mono<TaskInfo> update(String json);

    /**
     * Update a document
     *
     * @param t document
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    default Mono<TaskInfo> update(T t) {
        return update(Collections.singleton(t));
    }

    /**
     * Update a collection of documents
     *
     * @param collection documents
     * @return update task
     */
    @Operation(method = "PUT", tags = "/indexes/{indexUid}/documents", requestBody = @RequestBody(content = @Content(mediaType = "application/json")))
    Mono<TaskInfo> update(Collection<? extends T> collection);

    /**
     * Delete all documents in the specified index.
     *
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents")
    Mono<TaskInfo> deleteAll();

    /**
     * Get documents by batch.
     *
     * @param fetch request
     * @return paginated documents
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/fetch")
    Mono<Page<T>> batchGet(BatchGetDocumentsRequest fetch);

    /**
     * Delete a set of documents based on an array of document ids.
     *
     * @param ids An array of numbers containing the unique ids of the documents to be deleted.
     * @return delete task
     */
    @Operation(method = "POST", tags = "/indexes/{indexUid}/documents/delete-batch")
    Mono<TaskInfo> batchDelete(Collection<String> ids);

    /**
     * Delete a set of documents based on a filter.
     *
     * @param filter A filter expression written as a string or array of array of strings for the documents to be deleted.
     * @return delete task
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
     * @return the requested document
     */
    @Operation(method = "GET", tags = "/indexes/{indexUid}/documents/{documentId}")
    Mono<T> get(String id, @Nullable String... fields);

    /**
     * Delete one document based on its unique id.
     *
     * @param id Document id of the requested document
     * @return delete task
     */
    @Operation(method = "DELETE", tags = "/indexes/{indexUid}/documents/{documentId}")
    Mono<TaskInfo> delete(String id);


}

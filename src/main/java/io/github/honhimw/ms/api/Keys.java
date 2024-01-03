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

import io.github.honhimw.ms.model.CreateKeyRequest;
import io.github.honhimw.ms.model.Key;
import io.github.honhimw.ms.model.Page;
import io.github.honhimw.ms.model.UpdateKeyRequest;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Manage API keys for a Meilisearch instance. Each key has a given set of permissions.
 * You must have the master key or the default admin key to access the keys route.
 * More information about the keys and their rights.
 *
 * @author hon_him
 * @since 2024-01-02
 */

public interface Keys {

    /**
     * Get all Keys
     *
     * @param offset Number of results to skip.
     * @param limit  Maximum number of results to return.
     * @return Returns the 20 most recently created keys in a results array. Expired keys are included in the response, but deleted keys are not.
     */
    @Operation(method = "GET", tags = "/keys")
    Page<Key> get(Integer offset, Integer limit);

    /**
     * Get information on the specified key. Attempting to use this endpoint with a non-existent or deleted key will result in an error.
     *
     * @param keyOrUid key value of the requested API key, uid of the requested API key
     */
    @Operation(method = "GET", tags = "/keys/{key_or_uid}")
    Key get(String keyOrUid);

    /**
     * Create an API key with the provided description, permissions, and expiration date.
     *
     */
    @Operation(method = "POST", tags = "/keys/{key_or_uid}")
    Key create(String keyOrUid, CreateKeyRequest request);

    /**
     * A valid API key or uid is required.
     *
     * @param keyOrUid key value of the requested API key, uid of the requested API key
     * @return
     */
    @Operation(method = "PATCH", tags = "/keys/{key_or_uid}")
    Key update(String keyOrUid, UpdateKeyRequest request);

    @Operation(method = "DELETE", tags = "/keys/{key_or_uid}")
    void delete(String keyOrUid);

}

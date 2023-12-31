package io.github.honhimw.ms.api;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author hon_him
 * @since 2023-12-31
 */

public interface Documents {

    @Operation(method = "POST", tags = "/indexes/{index_uid}/documents/fetch")


}

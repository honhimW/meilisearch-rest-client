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

/**
 * JSON handler interface.
 *
 * @author hon_him
 * @since 2023-07-25
 */

public interface JsonHandler {

    /**
     * Convert the given object to its JSON representation.
     *
     * @param o the object to be converted to JSON
     * @return the JSON representation of the object
     */
    String toJson(Object o);

    /**
     * Parses the given JSON string and converts it into an object of the specified class.
     *
     * @param json   the JSON string to parse
     * @param tClass the class of the object to convert the JSON into
     * @param <T>    the type of the object to return
     * @return an object of type T representing the parsed JSON
     */
    <T> T fromJson(String json, Class<T> tClass);

    /**
     * Converts a JSON string to the specified type.
     *
     * @param json    the JSON string to convert
     * @param typeRef the type reference to convert the JSON to
     * @param <T>     the type of the object to return
     * @return the converted object of the specified type
     */
    <T> T fromJson(String json, TypeRef<T> typeRef);

    /**
     * Transforms the given object into the specified type.
     *
     * @param o      the object to be transformed
     * @param tClass the class of the desired type
     * @param <T>    the type of the desired object
     * @return the transformed object of the specified type
     */
    default <T> T transform(Object o, Class<T> tClass) {
        return transform(o, TypeRef.of(tClass));
    }

    /**
     * Transforms an object to a specified type by converting it to JSON and then
     * parsing the JSON back into the desired type.
     *
     * @param o       the object to be transformed
     * @param typeRef the type reference specifying the desired type of the
     *                transformed object
     * @param <T>     the type of the transformed object
     * @return the transformed object of the specified type
     */
    default <T> T transform(Object o, TypeRef<T> typeRef) {
        String json = toJson(o);
        return fromJson(json, typeRef);
    }

}

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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author hon_him
 * @since 2024-01-03
 */

public abstract class TypeRef<T> {

    protected final Type _type;

    protected TypeRef() {
        Type superClass = getClass().getGenericSuperclass();
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return _type;
    }

    public static <T> TypeRef<T> of(Type type) {
        return new TypeRef<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }

    public static <T> TypeRef<T> of(Class<T> type) {
        return new TypeRef<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }

}

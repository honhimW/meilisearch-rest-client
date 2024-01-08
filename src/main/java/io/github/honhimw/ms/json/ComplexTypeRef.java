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
 * @since 2024-01-08
 */

public abstract class ComplexTypeRef<T> extends TypeRef<T> {

    private final TypeRef<?> another;

    public ComplexTypeRef(TypeRef<?> another) {
        this.another = another;
    }

    @Override
    public Type getType() {
        Type type = super.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                Type anotherType = another.getType();
                return ParameterizedTypeImpl.make((Class<?>) rawType, new Type[]{anotherType}, null);
            }
            return type;
        } else if (type instanceof Class) {
            Type anotherType = another.getType();
            return ParameterizedTypeImpl.make((Class<?>) type, new Type[]{anotherType}, null);
        } else {
            throw new IllegalArgumentException("unknown type");
        }
    }
}

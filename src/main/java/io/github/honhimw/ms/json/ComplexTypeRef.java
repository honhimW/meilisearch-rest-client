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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hon_him
 * @since 2024-01-08
 */

public abstract class ComplexTypeRef<T> extends TypeRef<T> {

    private final List<TypeRef<?>> refs = new ArrayList<>();

    /**
     * Construct a new {@link ComplexTypeRef}.
     *
     * @param ref  first parameterized type reference
     * @param refs more parameterized type references
     */
    public ComplexTypeRef(TypeRef<?> ref, TypeRef<?>... refs) {
        this.refs.add(ref);
        this.refs.addAll(Arrays.asList(refs));
    }

    /**
     * Construct a new {@link ComplexTypeRef}.
     *
     * @param ref single parameterized type reference
     */
    public ComplexTypeRef(TypeRef<?> ref) {
        this.refs.add(ref);
    }

    /**
     * Construct a new {@link ComplexTypeRef}.
     *
     * @param type  first parameterized type
     * @param types more parameterized types
     */
    public ComplexTypeRef(Class<?> type, Class<?>... types) {
        this.refs.add(TypeRef.of(type));
        for (Class<?> aClass : types) {
            this.refs.add(TypeRef.of(aClass));
        }
    }

    /**
     * Construct a new {@link ComplexTypeRef}.
     *
     * @param type single parameterized type
     */
    public ComplexTypeRef(Class<?> type) {
        this.refs.add(TypeRef.of(type));
    }

    @Override
    public Type getType() {
        Type type = super.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                Type[] types = refs.stream().map(TypeRef::getType).toArray(Type[]::new);
                return ParameterizedTypeImpl.make((Class<?>) rawType, types, null);
            }
            return type;
        } else if (type instanceof Class) {
            Type[] types = refs.stream().map(TypeRef::getType).toArray(Type[]::new);
            return ParameterizedTypeImpl.make((Class<?>) type, types, null);
        } else {
            throw new IllegalArgumentException("unknown type");
        }
    }
}

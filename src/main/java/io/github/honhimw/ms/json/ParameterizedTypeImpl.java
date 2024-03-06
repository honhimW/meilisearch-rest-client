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

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author hon_him
 * @see sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
 * @since 2024-01-08
 */

@SuppressWarnings("all")
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;

    private ParameterizedTypeImpl(Class<?> var1, Type[] var2, Type var3) {
        this.actualTypeArguments = var2;
        this.rawType = var1;
        this.ownerType = (Type) (var3 != null ? var3 : var1.getDeclaringClass());
        this.validateConstructorArguments();
    }

    private void validateConstructorArguments() {
        TypeVariable[] var1 = this.rawType.getTypeParameters();
        if (var1.length != this.actualTypeArguments.length) {
            throw new MalformedParameterizedTypeException();
        } else {
            for (int var2 = 0; var2 < this.actualTypeArguments.length; ++var2) {
            }

        }
    }

    public static ParameterizedTypeImpl make(Class<?> var0, Type[] var1, Type var2) {
        return new ParameterizedTypeImpl(var0, var1, var2);
    }

    public Type[] getActualTypeArguments() {
        return (Type[]) this.actualTypeArguments.clone();
    }

    public Class<?> getRawType() {
        return this.rawType;
    }

    public Type getOwnerType() {
        return this.ownerType;
    }

    public boolean equals(Object var1) {
        if (var1 instanceof ParameterizedType) {
            ParameterizedType var2 = (ParameterizedType) var1;
            if (this == var2) {
                return true;
            } else {
                Type var3 = var2.getOwnerType();
                Type var4 = var2.getRawType();
                return Objects.equals(this.ownerType, var3) && Objects.equals(this.rawType, var4) && Arrays.equals(this.actualTypeArguments, var2.getActualTypeArguments());
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.actualTypeArguments) ^ Objects.hashCode(this.ownerType) ^ Objects.hashCode(this.rawType);
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        if (this.ownerType != null) {
            if (this.ownerType instanceof Class) {
                var1.append(((Class) this.ownerType).getName());
            } else {
                var1.append(this.ownerType.toString());
            }

            var1.append("$");
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                var1.append(this.rawType.getName().replace(((ParameterizedTypeImpl) this.ownerType).rawType.getName() + "$", ""));
            } else {
                var1.append(this.rawType.getSimpleName());
            }
        } else {
            var1.append(this.rawType.getName());
        }

        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            var1.append("<");
            boolean var2 = true;
            Type[] var3 = this.actualTypeArguments;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Type var6 = var3[var5];
                if (!var2) {
                    var1.append(", ");
                }

                var1.append(var6.getTypeName());
                var2 = false;
            }

            var1.append(">");
        }

        return var1.toString();
    }
}

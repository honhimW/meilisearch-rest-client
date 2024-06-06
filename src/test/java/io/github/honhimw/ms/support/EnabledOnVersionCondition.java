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

package io.github.honhimw.ms.support;

import io.github.honhimw.ms.client.TestBase;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hon_him
 * @since 2024-06-06
 */

class EnabledOnVersionCondition implements ExecutionCondition {

    private String runtimeVersion() {
        return System.getProperty(TestBase.VERSION_KEY);
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<Method> testMethod = context.getTestMethod();
        String runtimeVersion = runtimeVersion();
        if (Objects.isNull(runtimeVersion)) {
            return ConditionEvaluationResult.disabled("unknown version");
        }
        if (testMethod.isPresent()) {
            Method method = testMethod.get();
            if (method.isAnnotationPresent(EnabledOnVersion.class)) {
                EnabledOnVersion annotation = method.getAnnotation(EnabledOnVersion.class);
                return match(annotation);
            }
        }
        Optional<Class<?>> testClass = context.getTestClass();
        if (testClass.isPresent()) {
            Class<?> aClass = testClass.get();
            if (aClass.isAnnotationPresent(EnabledOnVersion.class)) {
                EnabledOnVersion annotation = aClass.getAnnotation(EnabledOnVersion.class);
                return match(annotation);
            }
        }
        return ConditionEvaluationResult.disabled("unknown condition");
    }

    private ConditionEvaluationResult match(EnabledOnVersion annotation) {
        String[] value = annotation.value();
        boolean higher = annotation.since();
        boolean lower = annotation.before();
        String runtimeVersion = runtimeVersion();

        for (String version : value) {
            int i = runtimeVersion.compareTo(version);
            if (runtimeVersion.startsWith(version)) {
                return ConditionEvaluationResult.enabled("version match");
            }
            if (higher && i >= 0) {
                return ConditionEvaluationResult.enabled("version match");
            }
            if (lower && i <= 0) {
                return ConditionEvaluationResult.enabled("version match");
            }
        }

        return ConditionEvaluationResult.disabled("version not match");
    }

}

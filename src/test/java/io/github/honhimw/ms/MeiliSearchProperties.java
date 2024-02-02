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

package io.github.honhimw.ms;

import io.github.honhimw.ms.support.StringUtils;
import jakarta.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Load properties from gradle.properties or profile-{xxx}.properties
 *
 * @author hon_him
 * @since 2024-01-09
 */

public class MeiliSearchProperties {

    private static final AtomicReference<Properties> ref = new AtomicReference<>();

    public static Properties getProperties() {
        Properties properties = ref.get();
        if (Objects.nonNull(properties)) {
            return properties;
        }
        InputStream config = ClassLoader.getSystemResourceAsStream("meili-search.properties");
        properties = new Properties();
        try {
            properties.load(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ref.set(properties);
        properties.list(System.err);
        assert properties.containsKey("server.host");
        assert properties.containsKey("server.port");
        assert properties.containsKey("server.api-key");
        return properties;
    }

    public static String getHost() {
        return getProperties().getProperty("server.host");
    }

    public static int getPort() {
        return Integer.parseInt(getProperties().getProperty("server.port"));
    }

    @Nullable
    public static String getApiKey() {
        return StringUtils.defaultIfBlank(getProperties().getProperty("server.api-key"), null);
    }

}

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

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author hon_him
 * @since 2024-01-08
 */

public class IOUtils {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static byte[] readFileToByteArray(final File file) throws IOException {
        Objects.requireNonNull(file, "file");
        return Files.readAllBytes(file.toPath());
    }

    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        // We use a ThresholdingOutputStream to avoid reading AND writing more than Integer.MAX_VALUE.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyLarge(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final long count = copyLarge(inputStream, outputStream);
        return count > Integer.MAX_VALUE ? -1 : (int) count;
    }

    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream)
        throws IOException {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize)
        throws IOException {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    public static byte[] byteArray(final int size) {
        return new byte[size];
    }

    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream, final byte[] buffer)
        throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");
        long count = 0;
        int n;
        while (-1 != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}

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

    /**
     * Default buffer size (8K).
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Reads the entire file into a byte array.
     *
     * @param file the file to be read
     * @return the byte array containing the contents of the file
     * @throws IOException if an I/O error occurs
     */
    public static byte[] readFileToByteArray(final File file) throws IOException {
        Objects.requireNonNull(file, "file");
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inputStream the InputStream to convert
     * @return the byte array representation of the InputStream
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        // We use a ThresholdingOutputStream to avoid reading AND writing more than Integer.MAX_VALUE.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyLarge(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Copies bytes from an {@link InputStream} to an {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of {@code -1} after the copy has completed since
     * the correct number of bytes cannot be returned as an int. For large streams use the
     * {@link #copyLarge(InputStream, OutputStream)} method.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @return the number of bytes copied, or -1 if greater than {@link Integer#MAX_VALUE}.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public static int copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final long count = copyLarge(inputStream, outputStream);
        return count > Integer.MAX_VALUE ? -1 : (int) count;
    }

    /**
     * Copies bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream)
        throws IOException {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies bytes from an {@link InputStream} to an {@link OutputStream} using an internal buffer of the
     * given size.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write to
     * @param bufferSize   the bufferSize used to copy from the input to the output
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize)
        throws IOException {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    /**
     * Returns a new byte array of the given size.
     *
     * @param size array size.
     * @return a new byte array of the given size.
     * @throws NegativeArraySizeException if the size is negative.
     */
    public static byte[] byteArray(final int size) {
        return new byte[size];
    }

    /**
     * Copies bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @param buffer the buffer to use for the copy
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException if an I/O error occurs.
     */
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

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

package io.github.honhimw.ms.http;

import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * copied from apache httpclient-core, exclude all dependencies except jdk
 *
 * @author hon_him
 * @since 2023-07-24
 */

@SuppressWarnings({"all", "unused", "UnusedReturnValue"})
public final class URIBuilder {

    private String scheme;
    private String encodedSchemeSpecificPart;
    private String encodedAuthority;
    private String userInfo;
    private String encodedUserInfo;
    private String host;
    private int port;
    private String encodedPath;
    private List<String> pathSegments;
    private String encodedQuery;
    private List<Map.Entry<String, String>> queryParams;
    private String query;
    @Getter
    private Charset charset;
    private String fragment;
    private String encodedFragment;

    /**
     * Constructs an empty instance.
     *
     * @return URIBuilder
     */
    public static URIBuilder create() {
        return new URIBuilder();
    }

    /**
     * Constructs an empty instance from a string.
     *
     * @param uri string
     * @return URIBuilder
     */
    public static URIBuilder from(final String uri) {
        return new URIBuilder(uri);
    }

    /**
     * Construct an instance from the provided URI.
     *
     * @param uri URI
     * @return URIBuilder
     */
    public static URIBuilder from(final URI uri) {
        return new URIBuilder(uri);
    }

    /**
     * Constructs an empty instance.
     */
    public URIBuilder() {
        this.port = -1;
    }

    /**
     * Construct an instance from the string which must be a valid URI.
     *
     * @param string a valid URI in string form
     */
    public URIBuilder(final String string) {
        this(URI.create(string), null);
    }

    /**
     * Construct an instance from the provided URI.
     *
     * @param uri URI
     */
    public URIBuilder(final URI uri) {
        this(uri, null);
    }

    /**
     * Construct an instance from the string which must be a valid URI.
     *
     * @param string  a valid URI in string form
     * @param charset charset
     * @throws URISyntaxException if the string is not a valid URI
     */
    public URIBuilder(final String string, final Charset charset) throws URISyntaxException {
        this(new URI(string), charset);
    }

    /**
     * Construct an instance from the provided URI.
     *
     * @param uri URI
     * @param charset charset
     */
    public URIBuilder(final URI uri, final Charset charset) {
        setCharset(charset);
        digestURI(uri);
    }

    /**
     * Set the charset.
     *
     * @param charset charset
     * @return this
     */
    public URIBuilder setCharset(final Charset charset) {
        this.charset = charset;
        return this;
    }

    private List<Map.Entry<String, String>> parseQuery(final String query, final Charset charset) {
        if (query != null && !query.isEmpty()) {
            final StringBuffer buffer = new StringBuffer(query.length());
            buffer.append(query);
            return parse(buffer, charset, '&', ';');
        }
        return null;
    }

    private List<Map.Entry<String, String>> parse(
        final StringBuffer buf, final Charset charset, final char... separators) {
        Objects.requireNonNull(buf, "Char array buffer");
        final BitSet delimSet = new BitSet();
        for (final char separator : separators) {
            delimSet.set(separator);
        }
        final ParserCursor cursor = new ParserCursor(0, buf.length());
        final List<Map.Entry<String, String>> list = new ArrayList<>();
        while (!cursor.atEnd()) {
            delimSet.set('=');
            final String name = TokenParser.parseToken(buf, cursor, delimSet);
            String value = null;
            if (!cursor.atEnd()) {
                final int delim = buf.charAt(cursor.getPos());
                cursor.updatePos(cursor.getPos() + 1);
                if (delim == '=') {
                    delimSet.clear('=');
                    value = TokenParser.parseToken(buf, cursor, delimSet);
                    if (!cursor.atEnd()) {
                        cursor.updatePos(cursor.getPos() + 1);
                    }
                }
            }
            if (!name.isEmpty()) {
                list.add(new AbstractMap.SimpleEntry<>(
                    urlDecode(name, charset != null ? charset : StandardCharsets.UTF_8, true),
                    Objects.requireNonNull(urlDecode(value, charset != null ? charset : StandardCharsets.UTF_8, true))));
            }
        }
        return list;
    }

    private List<String> parsePath(final String path, final Charset charset) {
        if (path != null && !path.isEmpty()) {
            return parsePathSegments(path, charset);
        }
        return null;
    }

    private List<String> parsePathSegments(final CharSequence s, final Charset charset) {
        Objects.requireNonNull(s, "Char sequence");
        final ParserCursor cursor = new ParserCursor(0, s.length());
        // Skip leading separator
        if (cursor.atEnd()) {
            return Collections.emptyList();
        }
        if (PATH_SAFE.get(s.charAt(cursor.getPos()))) {
            cursor.updatePos(cursor.getPos() + 1);
        }
        final List<String> list = new ArrayList<>();
        final StringBuilder buf = new StringBuilder();
        for (; ; ) {
            if (cursor.atEnd()) {
                list.add(buf.toString());
                break;
            }
            final char current = s.charAt(cursor.getPos());
            if (PATH_SAFE.get(current)) {
                list.add(buf.toString());
                buf.setLength(0);
            } else {
                buf.append(current);
            }
            cursor.updatePos(cursor.getPos() + 1);
        }
        list.replaceAll(content -> urlDecode(content, charset != null ? charset : StandardCharsets.UTF_8, false));
        return list;
    }

    /**
     * Builds a {@link URI} instance.
     * @return {@link URI}
     * @throws URISyntaxException if the string is not a valid URI
     */
    public URI build() throws URISyntaxException {
        return new URI(buildString());
    }

    private String buildString() {
        final StringBuilder sb = new StringBuilder();
        if (this.scheme != null) {
            sb.append(this.scheme).append(':');
        }
        if (this.encodedSchemeSpecificPart != null) {
            sb.append(this.encodedSchemeSpecificPart);
        } else {
            if (this.encodedAuthority != null) {
                sb.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                sb.append("//");
                if (this.encodedUserInfo != null) {
                    sb.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    sb.append(encodeUserInfo(this.userInfo)).append("@");
                }
                if (isIPv6Address(this.host)) {
                    sb.append("[").append(this.host).append("]");
                } else {
                    sb.append(this.host);
                }
                if (this.port >= 0) {
                    sb.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                sb.append(normalizePath(this.encodedPath, sb.length() == 0));
            } else if (this.pathSegments != null) {
                sb.append(encodePath(this.pathSegments));
            }
            if (this.encodedQuery != null) {
                sb.append("?").append(this.encodedQuery);
            } else if (this.queryParams != null && !this.queryParams.isEmpty()) {
                sb.append("?").append(encodeUrlForm(this.queryParams));
            } else if (this.query != null) {
                sb.append("?").append(encodeUric(this.query));
            }
        }
        if (this.encodedFragment != null) {
            sb.append("#").append(this.encodedFragment);
        } else if (this.fragment != null) {
            sb.append("#").append(encodeUric(this.fragment));
        }
        return sb.toString();
    }

    private static final Pattern IPV6_STD_PATTERN =
        Pattern.compile(
            "^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
        Pattern.compile(
            "^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)" + // 0-6 hex fields
            "::" +
            "(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$"); // 0-6 hex fields

    private static final char COLON_CHAR = ':';

    private static final int MAX_COLON_COUNT = 7;

    private static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    private static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    private static boolean isIPv6HexCompressedAddress(final String input) {
        int colonCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == COLON_CHAR) {
                colonCount++;
            }
        }
        return colonCount <= MAX_COLON_COUNT && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    private static String normalizePath(final String path, final boolean relative) {
        String s = path;

        if (isBlank(s)) {
            return "";
        }
        if (!relative && !s.startsWith("/")) {
            s = "/" + s;
        }
        return s;
    }

    private void digestURI(final URI uri) {
        this.scheme = uri.getScheme();
        this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        this.encodedAuthority = uri.getRawAuthority();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.encodedUserInfo = uri.getRawUserInfo();
        this.userInfo = uri.getUserInfo();
        this.encodedPath = uri.getRawPath();
        this.pathSegments = parsePath(uri.getRawPath(), this.charset != null ? this.charset : StandardCharsets.UTF_8);
        this.encodedQuery = uri.getRawQuery();
        this.queryParams = parseQuery(uri.getRawQuery(), this.charset != null ? this.charset : StandardCharsets.UTF_8);
        this.encodedFragment = uri.getRawFragment();
        this.fragment = uri.getFragment();
    }

    private String encodeUserInfo(final String userInfo) {
        return encUserInfo(userInfo, this.charset != null ? this.charset : StandardCharsets.UTF_8);
    }

    private String encUserInfo(final String content, final Charset charset) {
        return urlEncode(content, charset, USERINFO, false);
    }

    private String encodePath(final List<String> pathSegments) {
        return formatSegments(pathSegments, this.charset != null ? this.charset : StandardCharsets.UTF_8);
    }

    private String formatSegments(final Iterable<String> segments, final Charset charset) {
        Objects.requireNonNull(segments, "Segments");
        final StringBuilder result = new StringBuilder();
        for (final String segment : segments) {
            result.append('/').append(urlEncode(segment, charset, PATHSAFE, false));
        }
        return result.toString();
    }

    private String encodeUrlForm(final List<Map.Entry<String, String>> params) {
        return format(params, this.charset != null ? this.charset : StandardCharsets.UTF_8);
    }

    private String format(
        final Iterable<? extends Map.Entry<String, String>> parameters,
        final Charset charset) {
        Objects.requireNonNull(parameters, "Parameters");
        final StringBuilder result = new StringBuilder();
        for (final Map.Entry<String, String> parameter : parameters) {
            final String encodedName = encodeFormFields(parameter.getKey(), charset);
            final String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (!(result.length() == 0)) {
                result.append('&');
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append('=');
                result.append(encodedValue);
            }
        }
        return result.toString();
    }

    private String encodeFormFields(final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return urlEncode(content, charset != null ? charset : StandardCharsets.UTF_8, URLENCODER, true);
    }

    private String encodeUric(final String fragment) {
        return encUric(fragment, this.charset != null ? this.charset : StandardCharsets.UTF_8);
    }

    private String encUric(final String content, final Charset charset) {
        return urlEncode(content, charset, URIC, false);
    }

    /**
     * Set the scheme.
     * @param scheme The scheme.
     * @return this
     */
    public URIBuilder setScheme(final String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Set the user info.
     * @param userInfo The user info.
     * @return this
     */
    public URIBuilder setUserInfo(final String userInfo) {
        this.userInfo = userInfo;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        this.encodedUserInfo = null;
        return this;
    }

    /**
     * Set the user info.
     * @param username The username.
     * @param password The password.
     * @return this
     */
    public URIBuilder setUserInfo(final String username, final String password) {
        return setUserInfo(username + ':' + password);
    }

    /**
     * Set the host.
     * @param host The host.
     * @return this
     */
    public URIBuilder setHost(final String host) {
        this.host = host;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    /**
     * Set the port.
     * @param port The port.
     * @return this
     */
    public URIBuilder setPort(final int port) {
        this.port = port < 0 ? -1 : port;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    /**
     * Set the path.
     * @param path The path.
     * @return this
     */
    public URIBuilder setPath(final String path) {
        return setPathSegments(path != null ? splitSegments(path) : null);
    }

    private List<String> splitSegments(final CharSequence s) {
        final ParserCursor cursor = new ParserCursor(0, s.length());
        // Skip leading separator
        if (cursor.atEnd()) {
            return Collections.emptyList();
        }
        if (URIBuilder.PATH_SAFE.get(s.charAt(cursor.getPos()))) {
            cursor.updatePos(cursor.getPos() + 1);
        }
        final List<String> list = new ArrayList<String>();
        final StringBuilder buf = new StringBuilder();
        for (; ; ) {
            if (cursor.atEnd()) {
                list.add(buf.toString());
                break;
            }
            final char current = s.charAt(cursor.getPos());
            if (URIBuilder.PATH_SAFE.get(current)) {
                list.add(buf.toString());
                buf.setLength(0);
            } else {
                buf.append(current);
            }
            cursor.updatePos(cursor.getPos() + 1);
        }
        return list;
    }

    /**
     * Set the path segments.
     * @param pathSegments The path segments.
     * @return this
     */
    public URIBuilder setPathSegments(final String... pathSegments) {
        this.pathSegments = pathSegments.length > 0 ? Arrays.asList(pathSegments) : null;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    /**
     * Set the path segments.
     * @param pathSegments The path segments.
     * @return this
     */
    public URIBuilder setPathSegments(final List<String> pathSegments) {
        this.pathSegments = pathSegments != null && !pathSegments.isEmpty() ? new ArrayList<>(pathSegments) : null;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    /**
     * Remove all query parameters.
     * @return this
     */
    public URIBuilder removeQuery() {
        this.queryParams = null;
        this.query = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Set the query parameters.
     * @param nvps The query parameters.
     * @return this
     */
    public URIBuilder setParameters(final List<Map.Entry<String, String>> nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        } else {
            this.queryParams.clear();
        }
        this.queryParams.addAll(nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Add query parameters.
     * @param nvps The query parameters.
     * @return this
     */
    public URIBuilder addParameters(final List<Map.Entry<String, String>> nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        }
        this.queryParams.addAll(nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Set the query parameters.
     * @param nvps The query parameters.
     * @return this
     */
    @SafeVarargs
    public final URIBuilder setParameters(final Map.Entry<String, String>... nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        } else {
            this.queryParams.clear();
        }
        Collections.addAll(this.queryParams, nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Add a query parameter.
     * @param param parameter name
     * @param value parameter value
     * @return this
     */
    public URIBuilder addParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        }
        this.queryParams.add(new AbstractMap.SimpleEntry<>(param, value));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Replace a query parameter.
     * @param param parameter name
     * @param value parameter value
     * @return this
     */
    public URIBuilder setParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        }
        if (!this.queryParams.isEmpty()) {
            this.queryParams.removeIf(nvp -> nvp.getKey().equals(param));
        }
        this.queryParams.add(new AbstractMap.SimpleEntry<>(param, value));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Clear all query parameters.
     * @return this
     */
    public URIBuilder clearParameters() {
        this.queryParams = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Set custom query.
     * @param query custom query
     * @return this
     */
    public URIBuilder setCustomQuery(final String query) {
        this.query = query;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.queryParams = null;
        return this;
    }

    /**
     * Set fragment.
     * @param fragment fragment
     * @return this
     */
    public URIBuilder setFragment(final String fragment) {
        this.fragment = fragment;
        this.encodedFragment = null;
        return this;
    }

    /**
     * Is absolute.
     * @return is absolute
     */
    public boolean isAbsolute() {
        return this.scheme != null;
    }

    /**
     * Is opaque.
     * @return is opaque
     */
    public boolean isOpaque() {
        return this.pathSegments == null && this.encodedPath == null;
    }

    /**
     * Get scheme.
     * @return  scheme
     */
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Get user info.
     * @return user info
     */
    public String getUserInfo() {
        return this.userInfo;
    }

    /**
     * Get host.
     * @return host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Get port.
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Is path empty.
     * @return is path empty
     */
    public boolean isPathEmpty() {
        return (this.pathSegments == null || this.pathSegments.isEmpty()) &&
               (this.encodedPath == null || this.encodedPath.isEmpty());
    }

    /**
     * Get path segments.
     * @return path segments
     */
    public List<String> getPathSegments() {
        return this.pathSegments != null ? new ArrayList<String>(this.pathSegments) : new ArrayList<String>();
    }

    /**
     * Get path.
     * @return path
     */
    public String getPath() {
        if (this.pathSegments == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        for (final String segment : this.pathSegments) {
            result.append('/').append(segment);
        }
        return result.toString();
    }

    /**
     * Is query empty.
     * @return is query empty
     */
    public boolean isQueryEmpty() {
        return (this.queryParams == null || this.queryParams.isEmpty()) && this.encodedQuery == null;
    }

    /**
     * Get query parameters.
     * @return query parameters
     */
    public List<Map.Entry<String, String>> getQueryParams() {
        return this.queryParams != null ? new ArrayList<>(this.queryParams) : new ArrayList<>();
    }

    /**
     * Get fragment.
     * @return fragment
     */
    public String getFragment() {
        return this.fragment;
    }

    @Override
    public String toString() {
        return buildString();
    }

    private static final BitSet UNRESERVED = new BitSet(256);
    /**
     * Punctuation characters: , ; : $ & + =
     * <p>
     * These are the additional characters allowed by userinfo.
     */
    private static final BitSet PUNCT = new BitSet(256);
    /**
     * Characters which are safe to use in userinfo,
     * i.e. {@link #UNRESERVED} plus {@link #PUNCT}uation
     */
    private static final BitSet USERINFO = new BitSet(256);
    /**
     * Characters which are safe to use in a path,
     * i.e. {@link #UNRESERVED} plus {@link #PUNCT}uation plus / @
     */
    private static final BitSet PATHSAFE = new BitSet(256);
    /**
     * Characters which are safe to use in a query or a fragment,
     * i.e. {@link #RESERVED} plus {@link #UNRESERVED}
     */
    private static final BitSet URIC = new BitSet(256);

    /**
     * Reserved characters, i.e. {@code ;/?:@&=+$,[]}
     * <p>
     * This list is the same as the {@code reserved} list in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
     * as augmented by
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC 2732</a>
     */
    private static final BitSet RESERVED = new BitSet(256);


    /**
     * Safe characters for x-www-form-urlencoded data, as per java.net.URLEncoder and browser behaviour,
     * i.e. alphanumeric plus {@code "-", "_", ".", "*"}
     */
    private static final BitSet URLENCODER = new BitSet(256);

    private static final BitSet PATH_SPECIAL = new BitSet(256);

    private static final BitSet PATH_SAFE = new BitSet(256);

    static {
        // unreserved chars
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            UNRESERVED.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            UNRESERVED.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set('_'); // these are the charactes of the "mark" list
        UNRESERVED.set('-');
        UNRESERVED.set('.');
        UNRESERVED.set('*');
        URLENCODER.or(UNRESERVED); // skip remaining unreserved characters
        UNRESERVED.set('!');
        UNRESERVED.set('~');
        UNRESERVED.set('\'');
        UNRESERVED.set('(');
        UNRESERVED.set(')');
        // punct chars
        PUNCT.set(',');
        PUNCT.set(';');
        PUNCT.set(':');
        PUNCT.set('$');
        PUNCT.set('&');
        PUNCT.set('+');
        PUNCT.set('=');
        // Safe for userinfo
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);

        // URL path safe
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set(';'); // param separator
        PATHSAFE.set(':'); // RFC 2396
        PATHSAFE.set('@');
        PATHSAFE.set('&');
        PATHSAFE.set('=');
        PATHSAFE.set('+');
        PATHSAFE.set('$');
        PATHSAFE.set(',');

        PATH_SPECIAL.or(PATHSAFE);
        PATH_SPECIAL.set('/');

        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
        RESERVED.set('['); // added by RFC 2732
        RESERVED.set(']'); // added by RFC 2732

        URIC.or(RESERVED);
        URIC.or(UNRESERVED);

        PATH_SAFE.set('/');
    }

    private static final int RADIX = 16;

    private static String urlEncode(
        final String content,
        final Charset charset,
        final BitSet bitSet,
        final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (bitSet.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

    private static String urlDecode(
        final String content,
        final Charset charset,
        final boolean plusAsBlank) {
        if (content == null) {
            return null;
        }
        final ByteBuffer bb = ByteBuffer.allocate(content.length());
        final CharBuffer cb = CharBuffer.wrap(content);
        while (cb.hasRemaining()) {
            final char c = cb.get();
            if (c == '%' && cb.remaining() >= 2) {
                final char uc = cb.get();
                final char lc = cb.get();
                final int u = Character.digit(uc, 16);
                final int l = Character.digit(lc, 16);
                if (u != -1 && l != -1) {
                    bb.put((byte) ((u << 4) + l));
                } else {
                    bb.put((byte) '%');
                    bb.put((byte) uc);
                    bb.put((byte) lc);
                }
            } else if (plusAsBlank && c == '+') {
                bb.put((byte) ' ');
            } else {
                bb.put((byte) c);
            }
        }
        bb.flip();
        return charset.decode(bb).toString();
    }

    private static class ParserCursor {
        private final int lowerBound;
        private final int upperBound;
        private int pos;

        public ParserCursor(int lowerBound, int upperBound) {
            if (lowerBound < 0) {
                throw new IndexOutOfBoundsException("Lower bound cannot be negative");
            } else if (lowerBound > upperBound) {
                throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
            } else {
                this.lowerBound = lowerBound;
                this.upperBound = upperBound;
                this.pos = lowerBound;
            }
        }

        public int getLowerBound() {
            return this.lowerBound;
        }

        public int getUpperBound() {
            return this.upperBound;
        }

        public int getPos() {
            return this.pos;
        }

        public void updatePos(int pos) {
            if (pos < this.lowerBound) {
                throw new IndexOutOfBoundsException("pos: " + pos + " < lowerBound: " + this.lowerBound);
            } else if (pos > this.upperBound) {
                throw new IndexOutOfBoundsException("pos: " + pos + " > upperBound: " + this.upperBound);
            } else {
                this.pos = pos;
            }
        }

        public boolean atEnd() {
            return this.pos >= this.upperBound;
        }

        public String toString() {
            StringBuilder buffer = new StringBuilder();
            buffer.append('[');
            buffer.append(this.lowerBound);
            buffer.append('>');
            buffer.append(this.pos);
            buffer.append('>');
            buffer.append(this.upperBound);
            buffer.append(']');
            return buffer.toString();
        }
    }

    private static class TokenParser {
        public static final char CR = '\r';
        public static final char LF = '\n';
        public static final char SP = ' ';
        public static final char HT = '\t';
        public static final char DQUOTE = '"';
        public static final char ESCAPE = '\\';

        public static BitSet INIT_BITSET(int... b) {
            BitSet bitset = new BitSet();
            int len$ = b.length;

            for (int aB : b) {
                bitset.set(aB);
            }

            return bitset;
        }

        public static boolean isWhitespace(char ch) {
            return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
        }

        public static String parseToken(StringBuffer buf, ParserCursor cursor, BitSet delimiters) {
            StringBuilder dst = new StringBuilder();
            boolean whitespace = false;

            while (!cursor.atEnd()) {
                char current = buf.charAt(cursor.getPos());
                if (delimiters != null && delimiters.get(current)) {
                    break;
                }

                if (isWhitespace(current)) {
                    skipWhiteSpace(buf, cursor);
                    whitespace = true;
                } else {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }

                    copyContent(buf, cursor, delimiters, dst);
                    whitespace = false;
                }
            }

            return dst.toString();
        }

        public static String parseValue(StringBuffer buf, ParserCursor cursor, BitSet delimiters) {
            StringBuilder dst = new StringBuilder();
            boolean whitespace = false;

            while (!cursor.atEnd()) {
                char current = buf.charAt(cursor.getPos());
                if (delimiters != null && delimiters.get(current)) {
                    break;
                }

                if (isWhitespace(current)) {
                    skipWhiteSpace(buf, cursor);
                    whitespace = true;
                } else if (current == '"') {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }

                    copyQuotedContent(buf, cursor, dst);
                    whitespace = false;
                } else {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }

                    copyUnquotedContent(buf, cursor, delimiters, dst);
                    whitespace = false;
                }
            }

            return dst.toString();
        }

        public static void skipWhiteSpace(StringBuffer buf, ParserCursor cursor) {
            int pos = cursor.getPos();
            int indexFrom = cursor.getPos();
            int indexTo = cursor.getUpperBound();

            for (int i = indexFrom; i < indexTo; ++i) {
                char current = buf.charAt(i);
                if (!isWhitespace(current)) {
                    break;
                }

                ++pos;
            }

            cursor.updatePos(pos);
        }

        public static void copyContent(StringBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
            int pos = cursor.getPos();
            int indexFrom = cursor.getPos();
            int indexTo = cursor.getUpperBound();

            for (int i = indexFrom; i < indexTo; ++i) {
                char current = buf.charAt(i);
                if (delimiters != null && delimiters.get(current) || isWhitespace(current)) {
                    break;
                }

                ++pos;
                dst.append(current);
            }

            cursor.updatePos(pos);
        }

        public static void copyUnquotedContent(StringBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
            int pos = cursor.getPos();
            int indexFrom = cursor.getPos();
            int indexTo = cursor.getUpperBound();

            for (int i = indexFrom; i < indexTo; ++i) {
                char current = buf.charAt(i);
                if (delimiters != null && delimiters.get(current) || isWhitespace(current) || current == '"') {
                    break;
                }

                ++pos;
                dst.append(current);
            }

            cursor.updatePos(pos);
        }

        public static void copyQuotedContent(StringBuffer buf, ParserCursor cursor, StringBuilder dst) {
            if (!cursor.atEnd()) {
                int pos = cursor.getPos();
                int indexFrom = cursor.getPos();
                int indexTo = cursor.getUpperBound();
                char current = buf.charAt(pos);
                if (current == '"') {
                    ++pos;
                    ++indexFrom;
                    boolean escaped = false;

                    for (int i = indexFrom; i < indexTo; ++pos) {
                        current = buf.charAt(i);
                        if (escaped) {
                            if (current != '"' && current != '\\') {
                                dst.append('\\');
                            }

                            dst.append(current);
                            escaped = false;
                        } else {
                            if (current == '"') {
                                ++pos;
                                break;
                            }

                            if (current == '\\') {
                                escaped = true;
                            } else if (current != '\r' && current != '\n') {
                                dst.append(current);
                            }
                        }

                        ++i;
                    }

                    cursor.updatePos(pos);
                }
            }
        }
    }

    private static boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}

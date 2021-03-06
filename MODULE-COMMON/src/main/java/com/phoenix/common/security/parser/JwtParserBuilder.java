/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.common.security.parser;

import com.phoenix.common.exception.runtime.IncorrectClaimException;
import com.phoenix.common.exception.runtime.MissingClaimException;
import com.phoenix.common.security.common.Clock;
import com.phoenix.common.security.common.Deserializer;
import com.phoenix.common.security.compression.CompressionCodec;
import com.phoenix.common.security.compression.CompressionCodecResolver;
import com.phoenix.common.security.compression.CompressionCodecs;
import com.phoenix.common.security.jws.JwtBuilder;
import com.phoenix.common.security.jws.JwtParser;
import com.phoenix.common.security.signature.SigningKeyResolver;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * A builder to construct a {@link JwtParser}. Example usage:
 * <pre>{@code
 *     Jwts.parserBuilder()
 *         .setSigningKey(...)
 *         .requireIssuer("https://issuer.example.com")
 *         .build()
 *         .parse(jwtString)
 * }</pre>
 * @since 0.11.0
 */
public interface JwtParserBuilder {

    /**
     * Ensures that the specified {@code jti} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param id
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireId(String id);

    /**
     * Ensures that the specified {@code sub} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param subject
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireSubject(String subject);

    /**
     * Ensures that the specified {@code aud} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param audience
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireAudience(String audience);

    /**
     * Ensures that the specified {@code iss} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param issuer
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireIssuer(String issuer);

    /**
     * Ensures that the specified {@code iat} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param issuedAt
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireIssuedAt(Date issuedAt);

    /**
     * Ensures that the specified {@code exp} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param expiration
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireExpiration(Date expiration);

    /**
     * Ensures that the specified {@code nbf} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param notBefore
     * @return the parser builder for method chaining
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder requireNotBefore(Date notBefore);

    /**
     * Ensures that the specified {@code claimName} exists in the parsed JWT.  If missing or if the parsed
     * value does not equal the specified value, an exception will be thrown indicating that the
     * JWT is invalid and may not be used.
     *
     * @param claimName
     * @param value
     * @return the parser builder for method chaining.
     * @see MissingClaimException
     * @see IncorrectClaimException
     */
    JwtParserBuilder require(String claimName, Object value);

    /**
     * Sets the {@link Clock} that determines the timestamp to use when validating the parsed JWT.
     * The parser uses a default Clock implementation that simply returns {@code new Date()} when called.
     *
     * @param clock a {@code Clock} object to return the timestamp to use when validating the parsed JWT.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setClock(Clock clock);

    /**
     * Sets the amount of clock skew in seconds to tolerate when verifying the local time against the {@code exp}
     * and {@code nbf} claims.
     *
     * @param seconds the number of seconds to tolerate for clock skew when verifying {@code exp} or {@code nbf} claims.
     * @return the parser builder for method chaining.
     * @throws IllegalArgumentException if {@code seconds} is a value greater than {@code Long.MAX_VALUE / 1000} as
     * any such value would cause numeric overflow when multiplying by 1000 to obtain a millisecond value.
     */
    JwtParserBuilder setAllowedClockSkewSeconds(long seconds) throws IllegalArgumentException;

    /**
     * Sets the signing key used to verify any discovered JWS digital signature.  If the specified JWT string is not
     * a JWS (no signature), this key is not used.
     * <p>
     * <p>Note that this key <em>MUST</em> be a valid key for the signature algorithm found in the JWT header
     * (as the {@code alg} header parameter).</p>
     * <p>
     * <p>This method overwrites any previously set key.</p>
     *
     * @param key the algorithm-specific signature verification key used to validate any discovered JWS digital
     *            signature.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setSigningKey(byte[] key);

    /**
     * Sets the signing key used to verify any discovered JWS digital signature.  If the specified JWT string is not
     * a JWS (no signature), this key is not used.
     *
     * <p>Note that this key <em>MUST</em> be a valid key for the signature algorithm found in the JWT header
     * (as the {@code alg} header parameter).</p>
     *
     * <p>This method overwrites any previously set key.</p>
     *
     * <p>This is a convenience method: the string argument is first BASE64-decoded to a byte array and this resulting
     * byte array is used to invoke {@link #setSigningKey(byte[])}.</p>
     *
     * <h4>Deprecation Notice: Deprecated as of 0.10.0, will be removed in 1.0.0</h4>
     *
     * <p>This method has been deprecated because the {@code key} argument for this method can be confusing: keys for
     * cryptographic operations are always binary (byte arrays), and many people were confused as to how bytes were
     * obtained from the String argument.</p>
     *
     * <p>This method always expected a String argument that was effectively the same as the result of the following
     * (pseudocode):</p>
     *
     * <p>{@code String base64EncodedSecretKey = base64Encode(secretKeyBytes);}</p>
     *
     * <p>However, a non-trivial number of JJWT users were confused by the method signature and attempted to
     * use raw password strings as the key argument - for example {@code setSigningKey(myPassword)} - which is
     * almost always incorrect for cryptographic hashes and can produce erroneous or insecure results.</p>
     *
     * <p>See this
     * <a href="https://stackoverflow.com/questions/40252903/static-secret-as-byte-key-or-string/40274325#40274325">
     * StackOverflow answer</a> explaining why raw (non-base64-encoded) strings are almost always incorrect for
     * signature operations.</p>
     *
     * <p>Finally, please use the {@link #setSigningKey(Key) setSigningKey(Key)} instead, as this method and the
     * {@code byte[]} variant will be removed before the 1.0.0 release.</p>
     *
     * @param base64EncodedSecretKey the BASE64-encoded algorithm-specific signature verification key to use to validate
     *                               any discovered JWS digital signature.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setSigningKey(String base64EncodedSecretKey);

    /**
     * Sets the signing key used to verify any discovered JWS digital signature.  If the specified JWT string is not
     * a JWS (no signature), this key is not used.
     * <p>
     * <p>Note that this key <em>MUST</em> be a valid key for the signature algorithm found in the JWT header
     * (as the {@code alg} header parameter).</p>
     * <p>
     * <p>This method overwrites any previously set key.</p>
     *
     * @param key the algorithm-specific signature verification key to use to validate any discovered JWS digital
     *            signature.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setSigningKey(Key key);

    /**
     * Sets the {@link SigningKeyResolver} used to acquire the <code>signing key</code> that should be used to verify
     * a JWS's signature.  If the parsed String is not a JWS (no signature), this resolver is not used.
     * <p>
     * <p>Specifying a {@code SigningKeyResolver} is necessary when the signing key is not already known before parsing
     * the JWT and the JWT header or payload (plaintext body or Claims) must be inspected first to determine how to
     * look up the signing key.  Once returned by the resolver, the JwtParser will then verify the JWS signature with the
     * returned key.  For example:</p>
     * <p>
     * <pre>
     * Jws&lt;Claims&gt; jws = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
     *         &#64;Override
     *         public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
     *             //inspect the header or claims, lookup and return the signing key
     *             return getSigningKey(header, claims); //implement me
     *         }})
     *     .parseClaimsJws(compact);
     * </pre>
     * <p>
     * <p>A {@code SigningKeyResolver} is invoked once during parsing before the signature is verified.</p>
     * <p>
     * <p>This method should only be used if a signing key is not provided by the other {@code setSigningKey*} builder
     * methods.</p>
     *
     * @param signingKeyResolver the signing key resolver used to retrieve the signing key.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setSigningKeyResolver(SigningKeyResolver signingKeyResolver);

    /**
     * Sets the {@link CompressionCodecResolver} used to acquire the {@link CompressionCodec} that should be used to
     * decompress the JWT body. If the parsed JWT is not compressed, this resolver is not used.
     * <p><b>NOTE:</b> Compression is not defined by the JWT Specification, and it is not expected that other libraries
     * (including JJWT versions &lt; 0.6.0) are able to consume a compressed JWT body correctly.  This method is only
     * useful if the compact JWT was compressed with JJWT &gt;= 0.6.0 or another library that you know implements
     * the same behavior.</p>
     * <h3>Default Support</h3>
     * <p>JJWT's default {@link JwtParser} implementation supports both the
     * {@link CompressionCodecs#DEFLATE DEFLATE}
     * and {@link CompressionCodecs#GZIP GZIP} algorithms by default - you do not need to
     * specify a {@code CompressionCodecResolver} in these cases.</p>
     * <p>However, if you want to use a compression algorithm other than {@code DEF} or {@code GZIP}, you must implement
     * your own {@link CompressionCodecResolver} and specify that via this method and also when
     * {@link JwtBuilder#compressWith(CompressionCodec) building} JWTs.</p>
     *
     * @param compressionCodecResolver the compression codec resolver used to decompress the JWT body.
     * @return the parser builder for method chaining.
     */
    JwtParserBuilder setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver);

    /**
     * Uses the specified deserializer to convert JSON Strings (UTF-8 byte arrays) into Java Map objects.  This is
     * used by the parser after Base64Url-decoding to convert JWT/JWS/JWT JSON headers and claims into Java Map
     * objects.
     *
     * <p>If this method is not called, JJWT will use whatever deserializer it can find at runtime, checking for the
     * presence of well-known implementations such Jackson, Gson, and org.json.  If one of these is not found
     * in the runtime classpath, an exception will be thrown when one of the various {@code parse}* methods is
     * invoked.</p>
     *
     * @param deserializer the deserializer to use when converting JSON Strings (UTF-8 byte arrays) into Map objects.
     * @return the builder for method chaining.
     */
    JwtParserBuilder deserializeJsonWith(Deserializer<Map<String,?>> deserializer);

    /**
     * Returns an immutable/thread-safe {@link JwtParser} created from the configuration from this JwtParserBuilder.
     * @return an immutable/thread-safe JwtParser created from the configuration from this JwtParserBuilder.
     */
    JwtParser build();
}

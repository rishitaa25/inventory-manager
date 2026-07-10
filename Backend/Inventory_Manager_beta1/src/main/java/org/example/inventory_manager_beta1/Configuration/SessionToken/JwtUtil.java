package org.example.inventory_manager_beta1.Configuration.SessionToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Utility component for generating and validating JWT tokens.
 *
 * <p>With server-side sessions, the token carries <em>only</em> the {@code sessionId}
 * (a UUID) as its subject claim. All real auth data — who the user is and what they
 * can do — lives in the {@code Sessions} table. This means a logged-out user's token
 * is immediately invalid once the session row is deleted, regardless of expiry.
 *
 * <p>Token flow:
 * <ol>
 *   <li>On login, {@link #generateToken(String)} wraps the session UUID in a
 *       signed JWT and returns it to the client</li>
 *   <li>On each request, {@link #isTokenValid(String)} checks the signature
 *       and expiry, then {@link #getSessionId(String)} extracts the UUID</li>
 *   <li>{@code JwtAuthFilter} uses that UUID to look up the session in the DB</li>
 * </ol>
 *
 * <p>Configuration is loaded from {@code application.properties}:
 * <pre>
 *   jwt.secret=your-secret-key-at-least-32-characters-long
 *   jwt.expiration-ms=86400000
 * </pre>
 */
@Component
public class JwtUtil {

    /**
     * Secret key used to sign and verify JWT tokens.
     * Must be at least 32 characters (256 bits) for HS256.
     * Loaded from {@code jwt.secret} in application.properties.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token validity window in milliseconds.
     * Loaded from {@code jwt.expiration-ms} in application.properties.
     * Default: 86400000 (24 hours).
     */
    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    /**
     * INIT: Builds the HMAC-SHA256 signing key from the configured secret string.
     * @return
     *  The cryptographic signing key
     */
    private Key getSigningKey() {return Keys.hmacShaKeyFor(secret.getBytes());}

    /**
     * GENERATE: Builds a signed JWT whose only payload claim is the session ID.
     * The token is signed with HS256 and expires after {@code jwt.expiration-ms}.
     * @param sessionId
     *  The UUID of the newly created {@code Session} record
     * @return
     *  A compact signed JWT string to return to the client
     */
    public String generateToken(String sessionId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * EXTRACT: Pulls the session ID out of a token's subject claim.
     * @param token
     *  The raw JWT string (without the {@code "Bearer "} prefix)
     * @return
     *  The session ID UUID string
     */
    public String getSessionId(String token) {return extractAllClaims(token).getSubject();}

    /**
     * VALIDATE: Returns {@code true} if the token is well-formed, correctly
     * signed, and not yet expired. Does not check whether the session still
     * exists in the database — that is {@code JwtAuthFilter}'s responsibility.
     * @param token
     *  The raw JWT string
     * @return
     *  {@code true} if valid, {@code false} otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        }
        catch (JwtException |  IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * READ: Extracts all claims from a JWT string.
     * Throws a {@link JwtException} if the token is invalid or expired.
     * @param token
     *  The raw JWT string
     * @return
     *  The parsed {@link Claims} object
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

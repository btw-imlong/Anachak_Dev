package AccomManage.system.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import AccomManage.system.Entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // ✅ Secure HS256 key
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "MySuperSecretKeyForJWTGeneration1234567890!".getBytes(StandardCharsets.UTF_8)
    ); // in prod, use env variable

    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    // Generate token
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // ✅ use SECRET_KEY here
                .compact();
    }

    // Extract email
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // ✅ use SECRET_KEY
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String extractRole(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
}
}
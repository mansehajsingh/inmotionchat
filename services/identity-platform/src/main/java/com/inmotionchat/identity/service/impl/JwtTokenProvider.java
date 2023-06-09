package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.security.WebContextRole;
import com.inmotionchat.identity.service.contract.TokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String ISSUER = "inmotion-identityplatform-service";

    private static final Duration ACCESS_TOKEN_EXPIRATION_DURATION = Duration.of(1, ChronoUnit.MINUTES);

    private final String jwtSecret;

    private final Key signingKey;

    @Autowired
    public JwtTokenProvider(@Value("${JWT_SECRET}") final String secret) {
        this.jwtSecret = secret;
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private JwtBuilder createBaseToken(User user) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setSubject(user.getId().toString())
                .signWith(this.signingKey);
    }

    @Override
    public String issueAccessToken(User user) {
        return createBaseToken(user)
                .claim("role", new WebContextRole(user.getRole()))
                .setExpiration(Date.from(Instant.now().plus(ACCESS_TOKEN_EXPIRATION_DURATION)))
                .compact();
    }

    @Override
    public String issueRefreshToken(User user, Session session) {
        return createBaseToken(user)
                .claim("sessionId", session.getId())
                .setExpiration(Date.from(session.getExpiresAt().toInstant()))
                .compact();
    }

    @Override
    public Jws<Claims> validateAndDecodeToken(String token) throws UnauthorizedException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.signingKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Access token is expired");
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid access token: There may have been an issue parsing the claims.");
        }
    }

}

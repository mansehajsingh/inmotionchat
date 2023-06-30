package com.inmotionchat.identity.service.impl;

import com.inmotionchat.identity.IdentityPlatformService;
import com.inmotionchat.identity.service.contract.TokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class JwtTokenProvider implements TokenProvider {

    private final Key signingKey;

    private final Duration accessTokenExpirationDuration;

    private final JwtParser parser;

    @Autowired
    public JwtTokenProvider(IdentityPlatformService idpService) {
        this.signingKey = Keys.hmacShaKeyFor(idpService.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationDuration = Duration.of(idpService.getAccessTokenExpirationInMinutes(), ChronoUnit.MINUTES);
        this.parser = Jwts.parserBuilder().setSigningKey(this.signingKey).build();
    }

    @Override
    public String issueAccessToken(Long userId, Long roleId, Long tenantId, Set<String> permissions) {
        return Jwts.builder()
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setSubject(userId.toString())
                .claim("roleId", roleId)
                .claim("tenantId", tenantId)
                .claim("permissions", permissions)
                .signWith(this.signingKey)
                .setExpiration(Date.from(Instant.now().plus(this.accessTokenExpirationDuration)))
                .compact();
    }

    @Override
    public Jws<Claims> validateAndDecodeToken(String token) throws JwtException {
        return this.parser.parseClaimsJws(token);
    }

}

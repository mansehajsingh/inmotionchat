package com.inmotionchat.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmotionchat.core.web.AuthenticationError;
import com.inmotionchat.core.web.MessageResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {

    private JwtParser parser;

    @Autowired
    public AccessTokenFilter(@Value("${" + InMotionSecurityProperties.JWT_SECRET_KEY_PROP_NAME + "}") final String jwtSecretKey) {
        this.parser = Jwts.parserBuilder().setSigningKey(jwtSecretKey.getBytes(StandardCharsets.UTF_8)).build();
    }

    protected void prepareResponse(HttpServletResponse response, String message) {
        try {
            String json = new ObjectMapper().writeValueAsString(new MessageResponse(message));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            response.getWriter().write(json);
        } catch (Exception e) {}
    }

    protected Jws<Claims> validateAndDecodeToken(String token) throws JwtException {
        return this.parser.parseClaimsJws(token);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            prepareResponse(response, AuthenticationError.NO_BEARER_PROVIDED);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());

        Claims claims;

        try {
            claims = validateAndDecodeToken(token).getBody();
        } catch (ExpiredJwtException e) {
            prepareResponse(response, AuthenticationError.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            prepareResponse(response, AuthenticationError.INVALID_TOKEN);
            return;
        }

        Long userId = Long.parseLong(claims.getSubject());
        Long roleId = claims.get("roleId", Long.class);
        Long tenantId = claims.get("tenantId", Long.class);
        Set<String> permissions = new HashSet<>(claims.get("permissions", ArrayList.class));

        AuthenticationDetails details = new AuthenticationDetails(userId, roleId, permissions, tenantId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                details,
                token,
                Collections.singletonList(new SimpleGrantedAuthority(SpringSecurityRoles.ROLE_USER))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (Endpoint e : InMotionSecurityProperties.doNotAuthenticate()) {
            if (e.getMethod().toString().equalsIgnoreCase(request.getMethod())) {
                String cleaned = e.getPath().replace("{}", "[^\\/]*");
                cleaned = cleaned.replace("/", "\\/");

                if (request.getRequestURI().matches(cleaned)) {
                    return true;
                }
            }
        }

        return false;
    }

}

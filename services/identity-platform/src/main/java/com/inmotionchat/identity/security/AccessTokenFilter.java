package com.inmotionchat.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmotionchat.core.security.AuthenticationDetails;
import com.inmotionchat.core.web.AuthenticationError;
import com.inmotionchat.core.web.MessageResponse;
import com.inmotionchat.identity.service.contract.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AccessTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public AccessTokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    protected void prepareResponse(HttpServletResponse response, String message) {
        try {
            String json = new ObjectMapper().writeValueAsString(new MessageResponse(message));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(json);
        } catch (Exception e) {}
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
            claims = this.tokenProvider.validateAndDecodeToken(token).getBody();
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

package com.inmotionchat.identity.security;

import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.service.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Autowired
    public JwtAccessTokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null) {
            response.setStatus(403);
            return;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(403);
            return;
        }

        String token = authorizationHeader.substring(7); // "Remove the "Bearer "

        Claims claims;

        try {
            claims = this.tokenProvider.validateAndDecodeToken(token).getBody();
        } catch (UnauthorizedException e) {
            response.setStatus(403);
            return;
        }

        AuthenticationDetails userDetails = new AuthenticationDetails(
                Long.parseLong(claims.getSubject()),
                Collections.singletonList(new SimpleGrantedAuthority(SpringSecurityRoles.ROLE_USER))
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (Endpoint e : InMotionSecurityProperties.doNotAuthenticate()) {
            if (
                    e.getMethod().toString().equalsIgnoreCase(request.getMethod()) &&
                    e.getPath().equals(request.getRequestURI())
            ) {
                return true;
            }
        }

        return false;
    }

}

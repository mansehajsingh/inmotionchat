package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.service.contract.SessionService;
import com.inmotionchat.identity.service.contract.TokenProvider;
import com.inmotionchat.identity.web.dto.LoginRequest;
import com.inmotionchat.identity.web.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
public class AuthenticationResource {

    private static final String REFRESH_COOKIE_NAME = "inmotion-identityplatform-service-session-refresh";

    private SessionService sessionService;

    private TokenProvider tokenProvider;

    @Autowired
    public AuthenticationResource(SessionService sessionService, TokenProvider tokenProvider) {
        this.sessionService = sessionService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(PATH + "/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse)
            throws ConflictException, UnauthorizedException, NotFoundException
    {
        Session openedSession = this.sessionService.create(loginRequest);
        String refreshToken = this.tokenProvider.issueRefreshToken(openedSession.getUser(), openedSession);

        Cookie refreshCookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        int cookieMaxAge = (int) ChronoUnit.SECONDS.between(Instant.now(), openedSession.getExpiresAt());
        refreshCookie.setMaxAge(cookieMaxAge);
        refreshCookie.setPath(PATH + "/auth/refresh");
        refreshCookie.setHttpOnly(true);

        httpServletResponse.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping(PATH + "/auth/refresh")
    public TokenResponse refresh(@CookieValue(REFRESH_COOKIE_NAME) String refreshToken) throws UnauthorizedException {
        Claims claims = this.tokenProvider.validateAndDecodeToken(refreshToken).getBody();

        Long sessionId = claims.get("sessionId", Long.class);

        if (!this.sessionService.stillExists(sessionId)) {
            throw new UnauthorizedException("The session was invalidated. Open a new one.");
        }

        User user = SQLUser.fromId(
                Long.parseLong(claims.getSubject())
        );

        String accessToken = this.tokenProvider.issueAccessToken(user);
        return new TokenResponse(accessToken);
    }

}

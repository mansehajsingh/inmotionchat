package com.inmotionchat.identity.web;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.firebase.FirebaseErrorCodeTranslator;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

import static com.inmotionchat.core.web.AbstractResource.API_V1;

@RestController
@RequestMapping(API_V1 + "/auth")
public class AuthenticationResource {

    record AccessTokenResponse(String accessToken) {}

    private final TokenProvider tokenProvider;

    private final RoleService roleService;

    @Autowired
    public AuthenticationResource(TokenProvider tokenProvider, RoleService roleService) {
        this.tokenProvider = tokenProvider;
        this.roleService = roleService;
    }

    @GetMapping("/refresh")
    public AccessTokenResponse refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ConflictException, NotFoundException, UnauthorizedException {
        String idToken = authorizationHeader.substring("Bearer ".length());

        FirebaseToken firebaseToken = null;

        try {
            firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
        }

        Map<String, Object> claims = firebaseToken.getClaims();

        boolean hasVerifiedEmail = (Boolean) claims.get("email_verified");

        if (!hasVerifiedEmail) {
            throw new UnauthorizedException("Must verify email before attempting to retrieve access token.");
        }

        Long userId = ((BigDecimal) claims.get("inMotionUserId")).longValue();
        Long tenantId = ((BigDecimal) claims.get("tenantId")).longValue();

        Role role = this.roleService.retrieveByUserId(userId);
        String token = this.tokenProvider.issueAccessToken(
                userId, role.getId(), tenantId, role.getPermissionsAsStrings());

        return new AccessTokenResponse(token);
    }

}

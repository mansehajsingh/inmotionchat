package com.inmotionchat.identity.service.contract;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

import java.util.Set;

public interface TokenProvider {

    String issueAccessToken(Long userId, Long roleId, Long tenantId, Set<String> permissions);

    Jws<Claims> validateAndDecodeToken(String token) throws JwtException;

}

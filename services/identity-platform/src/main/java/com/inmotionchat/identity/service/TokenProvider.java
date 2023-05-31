package com.inmotionchat.identity.service;

import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenProvider {

    String issueAccessToken(User user);

    String issueRefreshToken(User user, Session session);

    Jws<Claims> validateAndDecodeToken(String token) throws UnauthorizedException;

}

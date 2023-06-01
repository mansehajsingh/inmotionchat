package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.web.dto.LoginRequest;

public interface SessionService {

    Session create(LoginRequest request) throws NotFoundException, ConflictException, UnauthorizedException;

    boolean stillExists(Long sessionId);

}

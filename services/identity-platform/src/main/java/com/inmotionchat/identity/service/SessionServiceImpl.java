package com.inmotionchat.identity.service;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.postgres.SQLSession;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.postgres.SQLSessionRepository;
import com.inmotionchat.identity.web.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private static final int MAX_ACTIVE_SESSIONS_PER_USER = 5;

    private final SQLSessionRepository sqlSessionRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SessionServiceImpl(
            SQLSessionRepository sqlSessionRepository,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.sqlSessionRepository = sqlSessionRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Session create(LoginRequest request) throws NotFoundException, ConflictException, UnauthorizedException {
        // fetch the user
        MultiValueMap<String, Object> searchParams = new LinkedMultiValueMap<>();
        searchParams.add("email", request.email());
        Page<? extends User> userPage = this.userService.search(searchParams, Pageable.unpaged());

        if (userPage.isEmpty()) {
            throw new NotFoundException("No user with email " + request.email() + " found.");
        }

        User user = userPage.getContent().get(0);

        // check that the password is correct for the login
        if (!this.passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        // fetch the sessions for the user
        List<SQLSession> sessions = this.sqlSessionRepository.findAllByUser(user.getId());

        // there can be some leftover inactive sessions that are expired, count number of active sessions
        long numberOfActiveSessions = sessions.stream().filter(SQLSession::isExpired).count();

        if (numberOfActiveSessions >= MAX_ACTIVE_SESSIONS_PER_USER) {
            throw new ConflictException(LogicalConstraints.Session.MAX_SESSIONS_REACHED,
                    "Already reached or exceeded the maximum number of sessions.");
        }

        return this.sqlSessionRepository.save(new SQLSession(SQLUser.fromId(user.getId())));
    }

    @Override
    public boolean stillExists(Long sessionId) {
        Optional<SQLSession> sessionOptional = this.sqlSessionRepository.findById(sessionId);
        return sessionOptional.isPresent();
    }
}

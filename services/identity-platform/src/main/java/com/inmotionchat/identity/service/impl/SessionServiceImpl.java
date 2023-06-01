package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.postgres.SQLSession;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.data.redis.RedisSession;
import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.identity.postgres.SQLSessionRepository;
import com.inmotionchat.identity.redis.RedisSessionRepository;
import com.inmotionchat.identity.service.contract.UserService;
import com.inmotionchat.identity.service.contract.SessionService;
import com.inmotionchat.identity.web.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.inmotionchat.core.util.query.Operation.EQUALS;

@Service
public class SessionServiceImpl implements SessionService {

    private static final int MAX_ACTIVE_SESSIONS_PER_USER = 5;

    private final SQLSessionRepository sqlSessionRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final RedisSessionRepository redisSessionRepository;

    @Autowired
    public SessionServiceImpl(
            SQLSessionRepository sqlSessionRepository,
            UserService userService,
            PasswordEncoder passwordEncoder,
            RedisSessionRepository redisSessionRepository
    ) {
        this.sqlSessionRepository = sqlSessionRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.redisSessionRepository = redisSessionRepository;
    }

    @Override
    public Session create(LoginRequest request) throws NotFoundException, ConflictException, UnauthorizedException {
        // fetch the user
        Page<? extends User> userPage = this.userService
                .search(Pageable.unpaged(), new SearchCriteria<>("email", EQUALS, request.email()));

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

        SQLUser mappedToSQL = SQLUser.fromId(user.getId());

        SQLSession sqlSession = this.sqlSessionRepository.save(new SQLSession(mappedToSQL));

        this.redisSessionRepository.save(new RedisSession(sqlSession));

        return sqlSession;
    }

    @Override
    public boolean stillExists(Long sessionId) {
        Optional<RedisSession> sessionOptional = this.redisSessionRepository.findById(sessionId);

        if (sessionOptional.isPresent()) return true; // redis session should be first point to search

        // search postgres if redis does not have it
        Optional<SQLSession> sqlSessionOptional = this.sqlSessionRepository.findById(sessionId);

        if (sqlSessionOptional.isPresent()) {
            // if postgres has it store it in redis then return true
            RedisSession redisSession = new RedisSession(sqlSessionOptional.get());
            this.redisSessionRepository.save(redisSession);
            return true;
        }

        return false;
    }

}

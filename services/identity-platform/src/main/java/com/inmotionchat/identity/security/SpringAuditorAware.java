package com.inmotionchat.identity.security;

import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.security.AuthenticationDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringAuditorAware implements AuditorAware<SQLUser> {

    @Override
    public Optional<SQLUser> getCurrentAuditor() {
        Long userId = ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();

        return Optional.of(SQLUser.fromId(userId));
    }

}

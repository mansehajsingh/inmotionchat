package com.inmotionchat.core.security;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Abstraction over SecurityContextHolder
 */
@Component
@RequestScope
public class IdentityContext {

    private User user;

    private Long userId;

    public IdentityContext() {}

    public User requestingUser() {

        if (user != null) return user;

        Long userId = ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();

        return AbstractDomain.forId(SQLUser.class, userId);
    }

}

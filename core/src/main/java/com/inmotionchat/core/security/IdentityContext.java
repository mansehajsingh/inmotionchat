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

    public IdentityContext() {}

    public User requestingUser() {
        Long userId = ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();

        return AbstractDomain.forId(SQLUser.class, userId);
    }

    public WebContextRole getRole() {
        return ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getRole();
    }

    public Long getTenantId() {
        return ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTenantId();
    }

}

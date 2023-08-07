package com.inmotionchat.core.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashSet;

@Component
@RequestScope
public class IdentityContext {

    private Requester requester;

    public IdentityContext() {}

    public Requester getRequester() {

        if (requester != null)
            return requester;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AuthenticationDetails details) {
            this.requester = new Requester(details.getUserId(), details.getRoleId(), details.getTenantId(), details.getPermissions());
        } else {
            this.requester = new Requester(null, null, null, new HashSet<>());
        }
        return this.requester;
    }

}

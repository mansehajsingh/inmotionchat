package com.inmotionchat.core.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class IdentityContext {

    private Requester requester;

    public IdentityContext() {}

    public Requester getRequester() {

        if (requester != null)
            return requester;

        AuthenticationDetails details = (AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.requester = new Requester(details.getUserId(), details.getRoleId(), details.getTenantId(), details.getPermissions());
        return this.requester;
    }

}

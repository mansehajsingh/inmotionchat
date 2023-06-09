package com.inmotionchat.core.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationDetails extends SecurityProperties.User {

    private final Long id;

    private final Collection<? extends GrantedAuthority> authorities;

    private final WebContextRole role;

    private final Long tenantId;

    public AuthenticationDetails(
            Long id, WebContextRole role, Long tenantId, Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.role = role;
        this.authorities = authorities;
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return this.id;
    }

    public WebContextRole getRole() {
        return this.role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

}

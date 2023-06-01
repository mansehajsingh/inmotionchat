package com.inmotionchat.core.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationDetails extends SecurityProperties.User {

    private final Long id;

    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticationDetails(Long id, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return this.id;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

}

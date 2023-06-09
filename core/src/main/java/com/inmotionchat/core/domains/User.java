package com.inmotionchat.core.domains;

import java.util.UUID;

public interface User extends ArchivableDomain<User> {

    String getEmail();

    void setEmail(String email);

    Boolean hasVerifiedEmail();

    String getUsername();

    void setUsername(String username);

    String getPasswordHash();

    void setPasswordHash(String passwordHash);

    String getPassword();

    void setPassword(String password);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Tenant getTenant();

    void setTenant(Tenant tenant);

    boolean isTenantRoot();

    void setIsTenantRoot(boolean isTenantRoot);

    Role getRole();

    void setRole(Role role);

    UUID getVerificationCode();

    void setVerificationCode(UUID verificationCode);

    boolean isVerified();

}

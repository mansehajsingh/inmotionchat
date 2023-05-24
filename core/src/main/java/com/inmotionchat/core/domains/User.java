package com.inmotionchat.core.domains;

public interface User extends Domain {

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

    Boolean hasVerificationCode();

    void setVerificationCode(Integer verificationCode);

}

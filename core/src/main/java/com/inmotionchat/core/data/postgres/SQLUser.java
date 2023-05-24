package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.Metadata;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_USERNAME", columnNames = "username")
        }
)
public class SQLUser extends AbstractArchivableDomain implements User {

    @Column(nullable = false, unique = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Transient
    private String password; // Not persisted, only filled at creation time of a new user.

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private Integer verificationCode;

    public static SQLUser fromId(Long id) {
        SQLUser onlyContainsId = new SQLUser();
        onlyContainsId.setId(id);
        return onlyContainsId;
    }

    public SQLUser() {}

    public SQLUser(
            String email,
            String username,
            String password,
            String firstName,
            String lastName
    ) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Metadata metadata() {
        return new Metadata(this.createdAt, this.createdBy == null ? this : this.createdBy, this.lastUpdatedAt, this.lastUpdatedBy);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Boolean hasVerifiedEmail() {
        return this.verificationCode == null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    @Override
    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

}

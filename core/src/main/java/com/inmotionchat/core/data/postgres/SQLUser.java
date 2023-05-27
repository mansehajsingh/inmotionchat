package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.Metadata;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(
        name = "users",
        schema = Schema.IdentityPlatform,
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_USERNAME", columnNames = "username")
        }
)
public class SQLUser extends AbstractArchivableDomain<User> implements User {

    private static final Pattern emailPattern
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // 1 upper case character, 1 lowercase character, 1 number, 1 symbol
    private static final Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])((?=.*[-+_!@#$%^&*.,?])|(?=.*_))");

    private static final Pattern alphanumericUnderscorePattern = Pattern.compile("^[A-Za-z0-9_]*$");

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

    public SQLUser(String email, String username, String password, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SQLUser(
            String email,
            String username,
            String password,
            PasswordEncoder passwordEncoder,
            String firstName,
            String lastName
    ) {
        this(email, username, password, firstName, lastName);
        this.passwordHash = passwordEncoder.encode(password);
    }

    public SQLUser(UserDTO proto, PasswordEncoder passwordEncoder) {
        this(
                proto.email(),
                proto.username(),
                proto.password(),
                passwordEncoder,
                proto.firstName(),
                proto.lastName()
        );
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

    @Override
    public String toString() {
        return String.format(
                "SQLUser[id=%d,email=%s,username=%s,passwordHash=%s,password=%s,firstName=%s,lastName=%s,verificationCode=%d]@"
                        + this.metadata().toString(),
                this.id, this.email, this.username, this.passwordHash, this.password, this.firstName, this.lastName, this.verificationCode
        );
    }

    @Override
    public SQLUser copy() {
        SQLUser copy = new SQLUser();
        copy.setId(this.id);
        copy.setEmail(this.email);
        copy.setUsername(this.username);
        copy.setFirstName(this.firstName);
        copy.setPassword(this.password);
        copy.setLastName(this.lastName);
        copy.setPasswordHash(this.passwordHash);
        copy.setVerificationCode(this.verificationCode);
        copy.setMetadata(this.metadata());

        return copy;
    }

    public void update(UserDTO prototype) {
        this.username = prototype.username();
        this.firstName = prototype.firstName();
        this.lastName = prototype.lastName();
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> emailRule = StringRule.forField("email")
                .isNotNull().isNotEmpty().matches(emailPattern, "Not a valid email.");

        AbstractRule<String> usernameRule = StringRule.forField("username")
                .isNotNull()
                .minimumLength(4)
                .maximumLength(20)
                .doesNotContain(" ", "Username cannot contain spaces.")
                .matches(alphanumericUnderscorePattern, "Username can only contain alphanumeric characters and underscores.");

        AbstractRule<String> firstNameRule = StringRule.forField("firstName")
                .isNotNull()
                .isNotEmpty()
                .maximumLength(30);

        AbstractRule<String> lastNameRule = StringRule.forField("lastName")
                .isNotNull()
                .maximumLength(30);

        List<Violation> violations = new ArrayList<>();
        violations.addAll(emailRule.collectViolations(this.email));
        violations.addAll(usernameRule.collectViolations(this.username));
        violations.addAll(firstNameRule.collectViolations(this.firstName));
        violations.addAll(lastNameRule.collectViolations(this.lastName));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    @Override
    public void validateForCreate() throws DomainInvalidException {
        List<Violation> violations = new ArrayList<>();

        try {
            validate();
        } catch (DomainInvalidException e) {
            violations.addAll(e.getViolations());
        }

        AbstractRule<String> passwordRule = StringRule.forField("password")
                .isNotNull()
                .minimumLength(8)
                .matches(
                        passwordPattern,
                        "Password must contain at least 1 uppercase character," +
                                " 1 lowercase character, 1 number, 1 special character from the following: -+_!@#$%^&*.,?");

        AbstractRule<String> passwordHashRule = StringRule.forField("passwordHash")
                        .isNotNull();

        violations.addAll(passwordRule.collectViolations(this.password));
        violations.addAll(passwordHashRule.collectViolations(this.passwordHash));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

package com.inmotionchat.core.data.postgres.identity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.misc.RegExpPatterns;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(
        name = "users",
        schema = Schema.IdentityPlatform,
        uniqueConstraints = {
                @UniqueConstraint(name = LogicalConstraints.User.EMAIL_EXISTS, columnNames = "email"),
                @UniqueConstraint(name = LogicalConstraints.User.UNIQUE_UID, columnNames = "uid")
        }
)
public class User {

    // 1 upper case character, 1 lowercase character, 1 number, 1 symbol
    private static final Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])((?=.*[-+_!@#$%^&*.,?])|(?=.*_))");

    @Id
    @GeneratedValue
    private Long id;

    private String uid;

    @ManyToOne
    private Tenant tenant;

    private String email;

    private String displayName;

    public User() {}

    public User(String uid, Tenant tenant, String email, String displayName) {
        this.uid = uid;
        this.tenant = tenant;
        this.email = email;
        this.displayName = displayName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static void validate(UserDTO userDTO) throws DomainInvalidException {
        AbstractRule<String> emailRule = StringRule.forField("email")
                .isNotNull()
                .isNotEmpty()
                .matches(RegExpPatterns.EMAIL, "Email was not provided in a valid format.");

        AbstractRule<String> displayNameRule = StringRule.forField("displayName")
                .isNotNull()
                .isNotEmpty()
                .maximumLength(50);

        AbstractRule<String> passwordRule = StringRule.forField("password")
                .isNotNull()
                .isNotEmpty()
                .minimumLength(8)
                .search(passwordPattern,
                        "Password must contain 1 uppercase letter, 1 lowercase letter, " +
                                "and 1 special character from the following: -+_!@#$%^&*.,?");

        List<Violation> violations = new ArrayList<>();
        violations.addAll(emailRule.collectViolations(userDTO.email()));
        violations.addAll(displayNameRule.collectViolations(userDTO.displayName()));
        violations.addAll(passwordRule.collectViolations(userDTO.password(), true));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    @Override
    public String toString() {
        return  "User[" +
                "id="           + id                + ", " +
                "uid="          + uid               + ", " +
                "tenant(id)="   + tenant.getId()    + ", " +
                "email="        + email             + ", " +
                "displayName="  + displayName       + "]";
    }

}

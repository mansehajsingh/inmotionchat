package com.inmotionchat.core.data.aggregates;

import com.google.firebase.auth.UserRecord;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserAggregate implements User {

    private static final Pattern emailPattern
        = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // 1 upper case character, 1 lowercase character, 1 number, 1 symbol
    private static final Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])((?=.*[-+_!@#$%^&*.,?])|(?=.*_))");

    private final UserRecord firebaseRecord;

    private final SQLUser sqlUser;

    public UserAggregate(SQLUser user, UserRecord firebaseRecord) {
        this.sqlUser = user;
        this.firebaseRecord = firebaseRecord;
    }

    @Override
    public Long getId() {
        return this.sqlUser.getId();
    }

    @Override
    public String getUid() {
        return this.firebaseRecord.getUid();
    }

    @Override
    public String getEmail() {
        return this.sqlUser.getEmail();
    }

    @Override
    public String getDisplayName() {
        return this.sqlUser.getDisplayName();
    }

    @Override
    public Tenant getTenant() {
        return this.sqlUser.getTenant();
    }

    public static void validate(UserDTO userDTO) throws DomainInvalidException {
        AbstractRule<String> emailRule = StringRule.forField("email")
                .isNotNull()
                .isNotEmpty()
                .matches(emailPattern, "Email was not provided in a valid format.");

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

}

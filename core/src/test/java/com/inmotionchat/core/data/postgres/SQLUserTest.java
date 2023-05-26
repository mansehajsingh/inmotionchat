package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.UnitTest;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

public class SQLUserTest extends UnitTest {

    protected SQLUser validUser = new SQLUser(
            "user@email.com",
            "username123",
            "@Mypass123",
            new BCryptPasswordEncoder(),
            "FirstName",
            ""
    );

    @Test
    public void test_validateValidSQLUser() throws DomainInvalidException {
        validUser.validate();
    }

    @Test
    public void test_invalidEmail() {
        SQLUser user = (SQLUser) validUser.copy();

        user.setEmail(null);
        assertThrows(DomainInvalidException.class, user::validate);

        user.setEmail(null);
        assertThrows(DomainInvalidException.class, user::validate);
    }

    @Test
    public void test_invalidUsername() {
        SQLUser user = (SQLUser) validUser.copy();

        user.setUsername(null);
        assertThrows(DomainInvalidException.class, user::validate);

        user.setUsername("contains space");
        assertThrows(DomainInvalidException.class, user::validate);

        user.setUsername("invalidch@r1");
        assertThrows(DomainInvalidException.class, user::validate);
    }

    @Test
    public void test_invalidFirstName() {
        SQLUser user = (SQLUser) validUser.copy();

        user.setFirstName(null);
        assertThrows(DomainInvalidException.class, user::validate);

        user.setFirstName("");
        assertThrows(DomainInvalidException.class, user::validate);

        user.setFirstName("overMaximumCharacterLimit123456");
        assertThrows(DomainInvalidException.class, user::validate);
    }

    @Test
    public void test_invalidLastName() {
        SQLUser user = (SQLUser) validUser.copy();

        user.setLastName(null);
        assertThrows(DomainInvalidException.class, user::validate);

        user.setLastName("overMaximumCharacterLimit123456");
        assertThrows(DomainInvalidException.class, user::validate);
    }

    @Test
    public void test_invalidPassword() {
        SQLUser user = (SQLUser) validUser.copy();

        user.setPassword(null);
        assertThrows(DomainInvalidException.class, user::validateForCreate);

        // password too short
        user.setPassword("T@2t");
        assertThrows(DomainInvalidException.class, user::validateForCreate);

        // password not containing upper case letter
        user.setPassword("@myawesomepassword123");
        assertThrows(DomainInvalidException.class, user::validateForCreate);

        // password not containing lower case letter
        user.setPassword("@MYAWESOMEPASSWORD123");
        assertThrows(DomainInvalidException.class, user::validateForCreate);

        // password not containing number
        user.setPassword("@myawesomepassword");
        assertThrows(DomainInvalidException.class, user::validateForCreate);

        // password not containing special character
        user.setPassword("Myawesomepassword123");
        assertThrows(DomainInvalidException.class, user::validateForCreate);
    }

}

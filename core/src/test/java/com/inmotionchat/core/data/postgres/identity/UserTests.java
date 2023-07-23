package com.inmotionchat.core.data.postgres.identity;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.junit.Test;

public class UserTests {

    private final static String validEmail = "email@email.com";
    private final static String validPassword = "@Mypassword123";
    private final static String validDisplayName = "John";
    private final static Long tenantId = 5L;

    @Test
    public void validateUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO(validEmail, validPassword, validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_InvalidFormatEmail() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO("invalid", validPassword, validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_EmptyEmail() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO("", validPassword, validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_NullEmail() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(null, validPassword, validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_ShortPassword() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, "@Short1", validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_NoSpecialCharacterPassword() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, "NoSpecialChars12345", validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_NoNumberPassword() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, "@MyAwesomePasswordButNoNumbers", validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_NoUpperCaseLetters() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, "@myawesomepassword12345", validDisplayName, tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_EmptyDisplayName() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, validPassword, "", tenantId);
        User.validate(userDTO);
    }

    @Test(expected = DomainInvalidException.class)
    public void validateUser_NullDisplayName() throws DomainInvalidException {
        UserDTO userDTO = new UserDTO(validEmail, validPassword, null, tenantId);
        User.validate(userDTO);
    }

}

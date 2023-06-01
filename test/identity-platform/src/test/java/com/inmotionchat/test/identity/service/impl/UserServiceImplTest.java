package com.inmotionchat.test.identity.service.impl;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.model.EmailVerificationStatus;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import com.inmotionchat.identity.service.contract.UserService;
import com.inmotionchat.test.commons.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.inmotionchat.test.identity.postgres.SQLUserRepositoryTest.generateRandomUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { com.inmotionchat.startup.InMotion.class })
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplTest extends IntegrationTest {

    @Autowired
    private SQLUserRepository sqlUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private UserDTO copyToDTO(SQLUser user) {
        return new UserDTO(user.getEmail(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    private SQLUser persistOne(EmailVerificationStatus verificationStatus, ArchivalStatus archivalStatus) throws ConflictException {
        SQLUser generated = generateRandomUser(this.passwordEncoder);

        switch (verificationStatus) {
            case VERIFIED -> generated.setVerificationCode(null);
            case NOT_VERIFIED -> generated.setVerificationCode(UUID.randomUUID()); // should already be set but setting it again just to make sure
        }

        switch (archivalStatus) {
            case ANY, NOT_ARCHIVED -> generated.setArchivedAt(null);
            case ARCHIVED -> generated.setArchivedAt(ZonedDateTime.now());
        }

        return sqlUserRepository.store(generated);
    }

    @Test
    @Transactional
    public void retrieveById() throws ConflictException, NotFoundException {
        SQLUser verifiedNotArchived = persistOne(EmailVerificationStatus.VERIFIED, ArchivalStatus.NOT_ARCHIVED);
        User vNA = this.userService.retrieveById(verifiedNotArchived.getId());
        assertEquals(verifiedNotArchived.getId(), vNA.getId());

        SQLUser verifiedArchived = persistOne(EmailVerificationStatus.VERIFIED, ArchivalStatus.ARCHIVED);
        assertThrows(NotFoundException.class, () ->
                this.userService.retrieveById(verifiedArchived.getId())
        );

        SQLUser notVerifiedNotArchived = persistOne(EmailVerificationStatus.NOT_VERIFIED, ArchivalStatus.NOT_ARCHIVED);
        assertThrows(NotFoundException.class, () ->
                this.userService.retrieveById(notVerifiedNotArchived.getId())
        );
    }

    @Test
    @Transactional
    public void create() throws ConflictException, DomainInvalidException, NotFoundException {
        SQLUser user = generateRandomUser(this.passwordEncoder);
        UserDTO userDTO = copyToDTO(user);

        User created = this.userService.create(userDTO);
        SQLUser fetched = this.sqlUserRepository.findById(created.getId()).get();

        assertEquals(userDTO.email(), fetched.getEmail());
        assertEquals(userDTO.username(), fetched.getUsername());
        assertEquals(userDTO.firstName(), fetched.getFirstName());
        assertEquals(userDTO.lastName(), fetched.getLastName());

        assertTrue(this.passwordEncoder.matches(userDTO.password(), fetched.getPasswordHash()));
        assertNotNull(fetched.getVerificationCode());
    }

    @Test
    @Transactional
    public void attemptToCreateADuplicateUsername() throws ConflictException, DomainInvalidException, NotFoundException {
        SQLUser original = generateRandomUser(this.passwordEncoder);
        UserDTO originalDTO = copyToDTO(original);

        this.userService.create(originalDTO);

        SQLUser conflict = generateRandomUser(this.passwordEncoder);
        conflict.setUsername(original.getUsername());
        UserDTO conflictDTO = copyToDTO(conflict);

        ConflictException e = assertThrows(ConflictException.class, () -> this.userService.create(conflictDTO));
        assertEquals(LogicalConstraints.User.UNIQUE_USERNAME.toUpperCase(), e.getViolatedConstraint().toUpperCase());
    }

    @Test
    @Transactional
    public void successfullyCreateDuplicateNotVerifiedEmail() throws ConflictException, DomainInvalidException, NotFoundException {
        SQLUser original = generateRandomUser(this.passwordEncoder);
        UserDTO originalDTO = copyToDTO(original);

        SQLUser noConflict = generateRandomUser(this.passwordEncoder);
        noConflict.setEmail(originalDTO.email());
        UserDTO noConflictDTO = copyToDTO(noConflict);

        this.userService.create(originalDTO);
        this.userService.create(noConflictDTO);
    }

    @Test
    @Transactional
    public void attemptToCreateDuplicateVerifiedEmail() throws ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        SQLUser original = generateRandomUser(this.passwordEncoder);
        UserDTO originalDTO = copyToDTO(original);
        User created = this.userService.create(originalDTO);

        this.userService.verify(created.getId(), created.getVerificationCode());

        SQLUser conflict = generateRandomUser(this.passwordEncoder);
        conflict.setEmail(originalDTO.email());
        UserDTO conflictDTO = copyToDTO(conflict);

        ConflictException e = assertThrows(ConflictException.class, () -> this.userService.create(conflictDTO));
        assertEquals(LogicalConstraints.User.UNIQUE_EMAIL_FOR_VERIFIED_USERS, e.getViolatedConstraint());
    }

    @Test
    @Transactional
    public void verifyUser() throws ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        SQLUser user = generateRandomUser(this.passwordEncoder);
        UserDTO userDTO = copyToDTO(user);
        User created = this.userService.create(userDTO);

        this.userService.verify(created.getId(), created.getVerificationCode());

        User retrieved = this.userService.retrieveById(created.getId());
        assertTrue(retrieved.isVerified());
    }

    @Test
    @Transactional
    public void attemptVerifyWithWrongVerificationCode() throws ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        SQLUser user = generateRandomUser(this.passwordEncoder);
        UserDTO userDTO = copyToDTO(user);
        User created = this.userService.create(userDTO);

        assertThrows(UnauthorizedException.class, () -> this.userService.verify(created.getId(), UUID.randomUUID()));

        SQLUser fetched = this.sqlUserRepository.findById(created.getId(), ArchivalStatus.ANY, EmailVerificationStatus.NOT_VERIFIED).get();
        assertFalse(fetched.isVerified());
    }

    @Test
    @Transactional
    public void verifyUser_CheckDuplicateEmailsRemoved() throws ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        SQLUser original = generateRandomUser(this.passwordEncoder);
        UserDTO originalDTO = copyToDTO(original);
        User created = this.userService.create(originalDTO);

        SQLUser sameEmail = generateRandomUser(this.passwordEncoder);
        sameEmail.setEmail(original.getEmail());
        UserDTO sameEmailDTO = copyToDTO(sameEmail);
        User createdSameEmail = this.userService.create(sameEmailDTO);

        this.userService.verify(created.getId(), created.getVerificationCode());

        Optional<SQLUser> sameEmailOpt = this.sqlUserRepository.findById(createdSameEmail.getId());

        assertTrue(sameEmailOpt.isEmpty());
    }

}

package com.inmotionchat.test.identity.postgres;

import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.identity.model.EmailVerificationStatus;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import com.inmotionchat.test.commons.IntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { com.inmotionchat.startup.InMotion.class })
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SQLUserRepositoryTest extends IntegrationTest {

    @Autowired
    private SQLUserRepository sqlUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static SQLUser generateRandomUser(PasswordEncoder passwordEncoder) {
        String email = RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(5) + ".com";
        String username = RandomStringUtils.randomAlphanumeric(4) + "_" + RandomStringUtils.randomAlphanumeric(4);
        String password = "@" + RandomStringUtils.randomAlphabetic(8) + RandomStringUtils.randomAlphabetic(1).toUpperCase()
                + RandomStringUtils.randomNumeric(3);
        String firstName = RandomStringUtils.random(8);
        String lastName = RandomStringUtils.random(8);

        return new SQLUser(email, username, password, passwordEncoder, firstName, lastName);
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
    public void findById_Verified_NotArchived() throws ConflictException {
        SQLUser user = persistOne(EmailVerificationStatus.VERIFIED, ArchivalStatus.NOT_ARCHIVED);

        Optional<SQLUser> foundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.VERIFIED);

        assert foundOpt.isPresent();

        SQLUser found = foundOpt.get();

        assertEquals(user.getId(), found.getId());

        Optional<SQLUser> archivedNotFoundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.ARCHIVED, EmailVerificationStatus.VERIFIED);

        assert archivedNotFoundOpt.isEmpty();

        Optional<SQLUser> unverifiedNotFoundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.NOT_VERIFIED);

        assert unverifiedNotFoundOpt.isEmpty();
    }

    @Test
    @Transactional
    public void findById_Verified_Archived() throws ConflictException {
        SQLUser user = persistOne(EmailVerificationStatus.VERIFIED, ArchivalStatus.ARCHIVED);

        Optional<SQLUser> foundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.ARCHIVED, EmailVerificationStatus.VERIFIED);

        assert foundOpt.isPresent();

        SQLUser found = foundOpt.get();

        assertEquals(user.getId(), found.getId());

        Optional<SQLUser> unarchivedNotFoundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.VERIFIED);

        assert unarchivedNotFoundOpt.isEmpty();

        Optional<SQLUser> unverifiedNotFoundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.NOT_VERIFIED);

        assert unverifiedNotFoundOpt.isEmpty();
    }

    @Test
    @Transactional
    public void findById_NotVerified_NotArchived() throws ConflictException {
        SQLUser user = persistOne(EmailVerificationStatus.NOT_VERIFIED, ArchivalStatus.NOT_ARCHIVED);

        Optional<SQLUser> foundOpt =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.NOT_VERIFIED);

        assert foundOpt.isPresent();

        SQLUser found = foundOpt.get();

        assertEquals(user.getId(), found.getId());

        Optional<SQLUser> verifiedNotFound =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.NOT_ARCHIVED, EmailVerificationStatus.VERIFIED);

        assert verifiedNotFound.isEmpty();

        Optional<SQLUser> unverifiedArchivedNotFound =
                this.sqlUserRepository.findById(user.getId(), ArchivalStatus.ARCHIVED, EmailVerificationStatus.NOT_VERIFIED);

        assert unverifiedArchivedNotFound.isEmpty();
    }

}

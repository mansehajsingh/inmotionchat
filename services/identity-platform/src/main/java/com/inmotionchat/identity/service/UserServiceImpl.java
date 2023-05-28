package com.inmotionchat.identity.service;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.identity.model.EmailVerificationStatus;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.inmotionchat.core.util.query.NullConstant.NULL;
import static com.inmotionchat.core.util.query.Operation.EQUALS;
import static com.inmotionchat.core.util.query.Operation.NOT_EQUALS;

@Service
public class UserServiceImpl implements UserService {

    private final SQLUserRepository sqlUserRepository;

    private final PasswordEncoder passwordEncoder;

    private static final SearchCriteriaMapper searchCriteriaMapper = new SearchCriteriaMapper()
            .key("email", String.class)
            .key("username", String.class)
            .key("firstName", String.class)
            .key("lastName", String.class);

    private final ThrowingTransactionTemplate transactionTemplate;

    @Autowired
    public UserServiceImpl(PlatformTransactionManager transactionManager,
                           SQLUserRepository sqlUserRepository,
                           PasswordEncoder passwordEncoder
    ) {
        this.sqlUserRepository = sqlUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
    }

    private SQLUser retrieveUser(Long id, ArchivalStatus archivalStatus)  throws NotFoundException {
        return retrieveUser(id, EmailVerificationStatus.VERIFIED, archivalStatus);
    }

    private SQLUser retrieveUser(Long id, EmailVerificationStatus verificationStatus, ArchivalStatus archivalStatus)
            throws NotFoundException
    {
        return this.sqlUserRepository.findById(id, archivalStatus, verificationStatus)
                .orElseThrow(() -> new NotFoundException("Could not find a user that fit these parameters: [verified=" +
                        verificationStatus.name() + ", archived=" + archivalStatus.name() + "]."));
    }

    private Boolean verifiedUserWithEmailExists(String email) {
        return this.sqlUserRepository.exists(
                EmailVerificationStatus.VERIFIED,
                new SearchCriteria<>("email", EQUALS, email)
        );
    }

    @Override
    public User retrieveById(Long id) throws NotFoundException {
        return retrieveUser(id, ArchivalStatus.NOT_ARCHIVED);
    }

    @Override
    public Page<? extends User> search(MultiValueMap<String, Object> parameters, Pageable pageable) {
        SearchCriteria<?>[] criteria = getSearchCriteriaFromParameters(searchCriteriaMapper, parameters);
        return this.sqlUserRepository.filter(pageable, criteria);
    }

    @Override
    public User create(UserDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        SQLUser user = new SQLUser(prototype, passwordEncoder);
        user.validateForCreate();

        if (verifiedUserWithEmailExists(user.getEmail())) {
            throw new ConflictException(
                    LogicalConstraints.User.UNIQUE_EMAIL_FOR_VERIFIED_USERS,
                    "An email-verified user with email " + user.getEmail() + " exists already."
            );
        }

        return this.sqlUserRepository.store(user);
    }

    @Override
    public User update(Long id, UserDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        SQLUser existingUser = retrieveUser(id, ArchivalStatus.NOT_ARCHIVED);
        existingUser.update(prototype);
        existingUser.validateForUpdate();

        return this.sqlUserRepository.update(existingUser);
    }

    @Override
    public User delete(Long id) throws NotFoundException, ConflictException {
        SQLUser userToArchive = retrieveUser(id, ArchivalStatus.NOT_ARCHIVED);
        userToArchive.setArchivedAt(ZonedDateTime.now());

        return this.sqlUserRepository.update(userToArchive);
    }

    @Override
    public User restore(Long id) throws NotFoundException, ConflictException {
        SQLUser userToRestore = retrieveUser(id, ArchivalStatus.ARCHIVED);
        userToRestore.setArchivedAt(null);

        return this.sqlUserRepository.update(userToRestore);
    }

    @Override
    public User hardDelete(Long id) throws NotFoundException {
        SQLUser userToHardDelete = retrieveUser(id, ArchivalStatus.ANY);
        this.sqlUserRepository.deleteById(userToHardDelete.getId());

        return userToHardDelete;
    }

    @Override
    public void verify(Long id, UUID verificationCode) throws NotFoundException, UnauthorizedException, ConflictException, DomainInvalidException {
        SQLUser userToVerify = retrieveUser(id, EmailVerificationStatus.NOT_VERIFIED, ArchivalStatus.NOT_ARCHIVED);

        if (!verificationCode.equals(userToVerify.getVerificationCode())) {
            throw new UnauthorizedException("Incorrect verification code.");
        }

        this.transactionTemplate.execute(status -> {
            userToVerify.setVerificationCode(null);

            this.sqlUserRepository.update(userToVerify);

            List<SQLUser> usersWithSameEmail = this.sqlUserRepository.filter(
                    new SearchCriteria<>("email", EQUALS, userToVerify.getEmail()),
                    new SearchCriteria<>("verificationCode", NOT_EQUALS, NULL)
            );

            this.sqlUserRepository.deleteAll(usersWithSameEmail);
            return null;
        });
    }
}

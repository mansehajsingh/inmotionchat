package com.inmotionchat.identity.service;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.identity.model.EmailVerificationStatus;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final SQLUserRepository sqlUserRepository;

    private final PasswordEncoder passwordEncoder;

    private static SearchCriteriaMapper searchCriteriaMapper = new SearchCriteriaMapper()
            .key("email", String.class)
            .key("username", String.class)
            .key("firstName", String.class)
            .key("lastName", String.class);

    @Autowired
    public UserServiceImpl(SQLUserRepository sqlUserRepository, PasswordEncoder passwordEncoder) {
        this.sqlUserRepository = sqlUserRepository;
        this.passwordEncoder = passwordEncoder;
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
    public User create(UserDTO prototype) throws DomainInvalidException, ConflictException {
        SQLUser user = new SQLUser(prototype, passwordEncoder);
        user.validateForCreate();

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

}

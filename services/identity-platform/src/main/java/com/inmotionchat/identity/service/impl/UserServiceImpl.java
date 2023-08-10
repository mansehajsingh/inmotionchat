package com.inmotionchat.identity.service.impl;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.dto.VerifyDTO;
import com.inmotionchat.core.data.events.PersistUserEvent;
import com.inmotionchat.core.data.events.StreamEventPublisher;
import com.inmotionchat.core.data.events.UnverifiedUserEvent;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.identity.firebase.FirebaseErrorCodeTranslator;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import com.inmotionchat.identity.service.contract.FirebaseWrapper;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static com.inmotionchat.core.data.AbstractDomainService.getSearchCriteriaFromParameters;

@Service
public class UserServiceImpl implements UserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static SearchCriteriaMapper searchCriteriaMapper = new SearchCriteriaMapper()
            .key("email", String.class)
            .key("displayName", String.class)
            .key("tenant", Long.class);

    private final ThrowingTransactionTemplate transactionTemplate;

    private final SQLUserRepository sqlUserRepository;

    private final RoleService roleService;

    private final StreamEventPublisher eventPublisher;

    private final FirebaseWrapper firebaseWrapper;

    @Autowired
    public UserServiceImpl(
            PlatformTransactionManager transactionManager,
            SQLUserRepository sqlUserRepository,
            RoleService roleService,
            StreamEventPublisher eventPublisher,
            FirebaseWrapper firebaseWrapper
    ) {
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.sqlUserRepository = sqlUserRepository;
        this.roleService = roleService;
        this.eventPublisher = eventPublisher;
        this.firebaseWrapper = firebaseWrapper;
    }

    @Override
    public String createEmailPasswordUser(UserDTO prototype) throws NotFoundException, DomainInvalidException, ConflictException {
        User.validate(prototype);

        UserRecord.CreateRequest crq = new UserRecord.CreateRequest();
        crq.setEmail(prototype.email());
        crq.setPassword(prototype.password());
        crq.setDisplayName(prototype.displayName());

        UserRecord record = null;

        try {
            record = firebaseWrapper.create(crq);
        } catch (FirebaseAuthException e) {
            FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
        }

        final UserRecord lambdaPassableRecord = record;
        this.transactionTemplate.execute((status) -> { // have to send events within a transaction for @TransactionalEventListener
            eventPublisher.publish(new UnverifiedUserEvent(this, lambdaPassableRecord));
            return null;
        });

        log.info("Successfully created unverified Firebase user with uid {}.", record.getUid());

        return record.getUid();
    }

    protected Map<String, Object> createCustomClaims(User user, Role role) {
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("tenantId", role.getTenant().getId());
        customClaims.put("inMotionUserId", user.getId());
        return customClaims;
    }

    @Override
    public void verifyEmailPasswordUser(String uid, VerifyDTO verifyDTO) throws ConflictException, NotFoundException, DomainInvalidException {
        UserRecord.UpdateRequest urq = new UserRecord.UpdateRequest(uid);
        urq.setEmailVerified(true);

        this.transactionTemplate.execute((status) -> {
            UserRecord record = null;

            try {
                record = firebaseWrapper.getUser(uid);
            } catch (FirebaseAuthException e) {
                FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
                throw new ServerException();
            }

            if (record.isEmailVerified())
                throw new ConflictException(LogicalConstraints.User.ALREADY_VERIFIED, "User with provided uid is already verified.");

            Tenant tenant = new Tenant();
            tenant.setId(verifyDTO.tenantId());
            User user = new User(uid, tenant, record.getEmail(), record.getDisplayName());

            User createdUser = this.sqlUserRepository.save(user);

            Role role = this.roleService.assignInitialRole(createdUser);

            Map<String, Object> customClaims = createCustomClaims(createdUser, role);
            urq.setCustomClaims(customClaims);

            try {
                firebaseWrapper.update(urq);
            } catch (FirebaseAuthException e) {
                FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
            }

            this.eventPublisher.publish(new PersistUserEvent(this, createdUser));

            log.info("Successfully verified firebase user with uid {} and created SQL double: {}.", uid, createdUser);

            return null;
        });
    }

    @Override
    public User retrieveById(Long tenantId, Long id) throws NotFoundException {
        User user = this.sqlUserRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Could not find a user with this id."));

        if (!user.getTenant().getId().equals(tenantId))
            throw new NotFoundException("Could not find a user with this id.");

        return user;
    }

    @Override
    public Page<User> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters) {
        parameters.add("tenant", tenantId);
        SearchCriteria<?>[] searchCriteria = getSearchCriteriaFromParameters(searchCriteriaMapper, parameters);
        return this.sqlUserRepository.filter(pageable, searchCriteria);
    }

}

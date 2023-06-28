package com.inmotionchat.identity.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.aggregates.UserAggregate;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.dto.VerifyDTO;
import com.inmotionchat.core.data.postgres.SQLTenant;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.identity.firebase.FirebaseErrorCodeTranslator;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import com.inmotionchat.identity.service.contract.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class UserServiceImpl implements UserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ThrowingTransactionTemplate transactionTemplate;

    private final SQLUserRepository sqlUserRepository;

    @Autowired
    public UserServiceImpl(
            PlatformTransactionManager transactionManager,
            SQLUserRepository sqlUserRepository
    ) {
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.sqlUserRepository = sqlUserRepository;
    }

    @Override
    public String createEmailPasswordUser(UserDTO prototype) throws NotFoundException, DomainInvalidException, ConflictException {
        UserAggregate.validate(prototype);

        UserRecord.CreateRequest crq = new UserRecord.CreateRequest();
        crq.setEmail(prototype.email());
        crq.setPassword(prototype.password());
        crq.setDisplayName(prototype.displayName());

        String uid = null;

        try {
            uid = FirebaseAuth.getInstance().createUser(crq).getUid();
        } catch (FirebaseAuthException e) {
            FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
        }

        log.info("Successfully created unverified Firebase user with uid {}.", uid);

        return uid;
    }

    @Override
    public void verifyEmailPasswordUser(String uid, VerifyDTO verifyDTO) throws ConflictException, NotFoundException, DomainInvalidException {
        UserRecord.UpdateRequest urq = new UserRecord.UpdateRequest(uid);
        urq.setEmailVerified(true);

        UserRecord record = null;

        try {
            record = FirebaseAuth.getInstance().getUser(uid);
        } catch (FirebaseAuthException e) {
            FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
        }

        SQLTenant tenant = new SQLTenant();
        tenant.setId(verifyDTO.tenantId());
        SQLUser sqlUser = new SQLUser(uid, tenant, record.getEmail(), record.getDisplayName());

        this.transactionTemplate.execute((status) -> {
            SQLUser createdUser = this.sqlUserRepository.save(sqlUser);

            try {
                FirebaseAuth.getInstance().updateUser(urq);
            } catch (FirebaseAuthException e) {
                FirebaseErrorCodeTranslator.getInstance().translateAuthErrorCode(e.getAuthErrorCode());
            }

            log.info("Successfully verified firebase user with uid {} and created SQL double: {}.", uid, createdUser);

            return null;
        });
    }

}

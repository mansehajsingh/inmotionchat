package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.util.query.NullConstant;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.identity.model.EmailVerificationStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.inmotionchat.core.util.query.NullConstant.NULL;
import static com.inmotionchat.core.util.query.Operation.EQUALS;
import static com.inmotionchat.core.util.query.Operation.NOT_EQUALS;
import static com.inmotionchat.identity.model.EmailVerificationStatus.VERIFIED;

@Repository
public interface SQLUserRepository extends SQLArchivingRepository<SQLUser> {

    default Optional<SQLUser> findById(
            Long id,
            ArchivalStatus archivalStatus,
            EmailVerificationStatus verified
    ) {
        SearchCriteria<Number> idCriteria = new SearchCriteria<>("id", EQUALS, id);
        SearchCriteria<NullConstant> verificationCriteria =
                new SearchCriteria<>("verificationCode", verified == VERIFIED ? EQUALS : NOT_EQUALS, NULL);
        return SQLArchivingRepository.super.filterOne(archivalStatus, idCriteria, verificationCriteria);
    }

}

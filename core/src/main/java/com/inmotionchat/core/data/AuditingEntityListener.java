package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.security.AuthenticationDetails;
import jakarta.persistence.PrePersist;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditingEntityListener {

    @PrePersist
    public void beforePersist(Object entity) {
        AbstractDomain<?> domain = (AbstractDomain<?>) entity;

        Long userId = ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();

        SQLUser user = AbstractDomain.forId(SQLUser.class, userId);

        if (domain.metadata().createdBy == null) {
            domain.setCreatedBy(user);
        } else {
            domain.setLastUpdatedBy(user);
        }

    }

}

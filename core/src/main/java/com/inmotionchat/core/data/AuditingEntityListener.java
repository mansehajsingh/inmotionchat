package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.User;
import com.inmotionchat.core.security.AuthenticationDetails;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditingEntityListener {

    @PrePersist
    protected void beforePersist(Object o) {
        AbstractDomain<?> domain = (AbstractDomain<?>) o;

        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return;

        Object principal = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        AuthenticationDetails details;

        if (principal instanceof AuthenticationDetails d) {
            details = d;
        } else {
            return;
        }

        if (details.getUserId() != null) {

            if (domain.getCreatedBy() == null) {
                User creator = new User();
                creator.setId(details.getUserId());
                domain.setCreatedBy(creator);
            }

        }
    }

    @PreUpdate
    protected void beforeUpdate(Object o) {
        AbstractDomain<?> domain = (AbstractDomain<?>) o;

        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return;

        AuthenticationDetails details = (AuthenticationDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (details != null && details.getUserId() != null) {

            if (domain.getLastModifiedBy() == null) {
                User modifier = new User();
                modifier.setId(details.getUserId());
                domain.setLastModifiedBy(modifier);
            }

        }
    }

}

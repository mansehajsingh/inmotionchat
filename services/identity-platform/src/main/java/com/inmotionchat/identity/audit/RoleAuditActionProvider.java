package com.inmotionchat.identity.audit;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditActionProvider;
import org.springframework.stereotype.Service;

@Service
public class RoleAuditActionProvider implements AuditActionProvider {

    @Override
    public AuditAction getCreateAction() {
        return AuditAction.CREATE_ROLE;
    }

    @Override
    public AuditAction getUpdateAction() {
        return AuditAction.UPDATE_ROLE;
    }

    @Override
    public AuditAction getDeleteAction() {
        return AuditAction.DELETE_ROLE;
    }

}

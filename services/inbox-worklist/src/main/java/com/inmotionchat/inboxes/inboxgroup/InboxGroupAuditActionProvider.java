package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditActionProvider;
import org.springframework.stereotype.Service;

@Service
public class InboxGroupAuditActionProvider implements AuditActionProvider {

    @Override
    public AuditAction getCreateAction() {
        return AuditAction.CREATE_INBOX_GROUP;
    }

    @Override
    public AuditAction getUpdateAction() {
        return AuditAction.UPDATE_INBOX_GROUP;
    }

    @Override
    public AuditAction getDeleteAction() {
        return AuditAction.DELETE_INBOX_GROUP;
    }

}

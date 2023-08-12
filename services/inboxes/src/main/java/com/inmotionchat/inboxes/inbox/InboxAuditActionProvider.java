package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditActionProvider;
import org.springframework.stereotype.Service;

@Service
public class InboxAuditActionProvider implements AuditActionProvider {

    @Override
    public AuditAction getCreateAction() {
        return AuditAction.CREATE_INBOX;
    }

    @Override
    public AuditAction getUpdateAction() {
        return null;
    }

    @Override
    public AuditAction getDeleteAction() {
        return null;
    }

}

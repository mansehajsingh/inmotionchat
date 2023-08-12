package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditActionProvider;
import org.springframework.stereotype.Service;

@Service
public class ChatboxAuditActionProvider implements AuditActionProvider {

    @Override
    public AuditAction getCreateAction() {
        return AuditAction.CREATE_CHATBOX;
    }

    @Override
    public AuditAction getUpdateAction() {
        return AuditAction.UPDATE_CHATBOX;
    }

    @Override
    public AuditAction getDeleteAction() {
        return AuditAction.DELETE_CHATBOX;
    }

}

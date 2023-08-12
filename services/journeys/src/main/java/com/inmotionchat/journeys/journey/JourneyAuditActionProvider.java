package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditActionProvider;
import org.springframework.stereotype.Service;

@Service
public class JourneyAuditActionProvider implements AuditActionProvider {

    @Override
    public AuditAction getCreateAction() {
        return AuditAction.CREATE_JOURNEY;
    }

    @Override
    public AuditAction getUpdateAction() {
        return AuditAction.UPDATE_JOURNEY;
    }

    @Override
    public AuditAction getDeleteAction() {
        return AuditAction.DELETE_JOURNEY;
    }

}

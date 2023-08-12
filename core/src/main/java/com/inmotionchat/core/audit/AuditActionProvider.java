package com.inmotionchat.core.audit;

public interface AuditActionProvider {

    AuditAction getCreateAction();

    AuditAction getUpdateAction();

    AuditAction getDeleteAction();

}

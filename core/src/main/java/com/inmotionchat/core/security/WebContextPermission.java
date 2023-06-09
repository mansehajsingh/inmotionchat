package com.inmotionchat.core.security;

import com.inmotionchat.core.domains.Permission;
import com.inmotionchat.core.domains.models.ActionType;

public class WebContextPermission {

    private final String actionType;

    private final String domainName;

    public WebContextPermission(Permission permission) {
        this.actionType = permission.getActionType().value();
        this.domainName = permission.getDomainName();
    }

    public WebContextPermission(ActionType actionType, Class<?> domain) {
        this.actionType = actionType.value();
        this.domainName = domain.getSimpleName();
    }

    public WebContextPermission(ActionType actionType, String domainName) {
        this.actionType = actionType.value();
        this.domainName = domainName;
    }

    public ActionType getActionType() {
        return ActionType.forValue(actionType);
    }

    public String getDomainName() {
        return this.domainName;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof WebContextPermission other) {
            return other.getActionType().value().equals(this.actionType)
                    && other.getDomainName().equals(this.domainName);
        }

        return false;
    }

}

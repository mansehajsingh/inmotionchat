package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.Permission;
import com.inmotionchat.core.domains.models.ActionType;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class SQLPermission implements Permission {

    private String actionType;

    private String domainName;

    public SQLPermission() {}

    public SQLPermission(ActionType actionType, Class<?> domainClass) {
        this.actionType = actionType.value();
        this.domainName = domainClass.getSimpleName();
    }

    @Override
    public ActionType getActionType() {
        return ActionType.forValue(this.actionType);
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SQLPermission other) {
            return other.getActionType().value().equals(actionType) && other.getDomainName().equals(domainName);
        }
        return false;
    }
}
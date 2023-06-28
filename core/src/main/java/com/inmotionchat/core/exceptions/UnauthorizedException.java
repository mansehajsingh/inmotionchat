package com.inmotionchat.core.exceptions;

import com.inmotionchat.core.models.ActionType;

public class UnauthorizedException extends InMotionException {

    private String action;

    private String domain;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(ActionType actionType, Class<?> domainClass) {
        super("Do not possess " + actionType.value() + " permission for " + domainClass.getSimpleName() + ",");
        this.action = actionType.value();
        this.domain = domainClass.getSimpleName();
    }

}

package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.ActionType;

public interface Permission {

    ActionType getActionType();

    String getDomainName();

}

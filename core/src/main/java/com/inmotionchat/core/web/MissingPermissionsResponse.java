package com.inmotionchat.core.web;

import com.inmotionchat.core.data.LogicalConstraints;

import java.util.List;

public class MissingPermissionsResponse {

    private final String constraint = LogicalConstraints.Permission.MISSING_PERMISSIONS;

    private final List<String> missingPermissions;

    public MissingPermissionsResponse(List<String> missingPermissions) {
        this.missingPermissions = missingPermissions;
    }

}

package com.inmotionchat.core.exceptions;

import java.util.List;

public class PermissionException extends InMotionException {

    private List<String> missingPermissions;

    public PermissionException(List<String> missingPermissions) {
        this.missingPermissions = missingPermissions;
    }

    public List<String> getMissingPermissions() {
        return this.missingPermissions;
    }

}

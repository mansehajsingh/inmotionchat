package com.inmotionchat.core.web;

import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;

import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    public static void throwIfMissingPermissions(IdentityContext identityContext, Permission[] permissions) throws PermissionException {
        List<String> missingPermissions = new ArrayList<>();

        for (Permission p : permissions) {
            if (!identityContext.getRequester().hasPermission(p)) {
                missingPermissions.add(p.value());
            }
        }

        if (!missingPermissions.isEmpty()) {
            throw new PermissionException(missingPermissions);
        }
    }

    public static boolean isCorrectTenant(IdentityContext identityContext, Long tenantId) {
        return identityContext.getRequester().getTenantId().equals(tenantId);
    }

}

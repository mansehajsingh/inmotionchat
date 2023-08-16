package com.inmotionchat.audit.logs;

import com.inmotionchat.core.audit.AuditLog;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static com.inmotionchat.core.models.Permission.READ_AUDIT_LOGS;
import static com.inmotionchat.core.web.AbstractResource.PATH;
import static com.inmotionchat.core.web.WebUtils.isCorrectTenant;
import static com.inmotionchat.core.web.WebUtils.throwIfMissingPermissions;

@RestController
@RequestMapping(PATH + "/audit-logs")
public class AuditLogResource {

    protected final AuditManager auditManager;

    protected final IdentityContext identityContext;

    private static final Permission[] READ_PERMISSIONS = new Permission[] { READ_AUDIT_LOGS };

    @Autowired
    public AuditLogResource(AuditManager auditManager, IdentityContext identityContext) {
        this.auditManager = auditManager;
        this.identityContext = identityContext;
    }

    @GetMapping
    public PageResponse<AuditLog> search(@PathVariable Long tenantId, @RequestParam MultiValueMap<String, Object> parameters) throws PermissionException, UnauthorizedException {
        if (!isCorrectTenant(identityContext, tenantId)) {
            throw new UnauthorizedException("Not authorized to search this resource for this tenant.");
        }

        throwIfMissingPermissions(identityContext, READ_PERMISSIONS);

        int pageSize = 50, page = 0;

        if (parameters.containsKey("size")) {
            pageSize = Integer.parseInt(parameters.getFirst("size").toString());
        }

        if (parameters.containsKey("page")) {
            page =  Integer.parseInt(parameters.getFirst("page").toString());
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<AuditLog> auditLogPage = this.auditManager.search(tenantId, pageable, parameters);

        return new PageResponse<>(
                auditLogPage.getTotalElements(),
                auditLogPage.getTotalPages(),
                auditLogPage.getContent()
        );
    }

}

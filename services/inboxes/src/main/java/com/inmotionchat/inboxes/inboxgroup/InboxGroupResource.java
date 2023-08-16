package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.dto.InboxGroupAssignmentsDTO;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.core.web.PageResponse;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inmotionchat.core.models.Permission.EDIT_INBOX_GROUPS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/inbox-groups")
public class InboxGroupResource extends AbstractResource<InboxGroup, InboxGroupDTO> {

    private static final Permission[] CREATE_PERMISSIONS = { EDIT_INBOX_GROUPS };
    private static final Permission[] UPDATE_PERMISSIONS = { EDIT_INBOX_GROUPS };

    private final InboxGroupService inboxGroupService;

    protected InboxGroupResource(IdentityContext identityContext, InboxGroupService inboxGroupService) {
        super(identityContext, inboxGroupService);
        this.inboxGroupService = inboxGroupService;
    }

    @Override
    protected int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected int getMaximumPageSize() {
        return 300;
    }

    @Override
    protected boolean isGetEnabled() {
        return true;
    }

    @Override
    public Permission[] getGetPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isCreateEnabled() {
        return true;
    }

    @Override
    public Permission[] getCreatePermissions() {
        return CREATE_PERMISSIONS;
    }

    @Override
    protected boolean isUpdateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getUpdatePermissions() {
        return UPDATE_PERMISSIONS;
    }

    @PostMapping("/{id}/inboxes")
    public void assignInboxes(@PathVariable Long tenantId, @PathVariable Long id, @RequestBody InboxGroupAssignmentsDTO dto) throws NotFoundException, UnauthorizedException, PermissionException, DomainInvalidException, ConflictException {
        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to create this resource for this tenant.");

        throwIfMissingPermissions(CREATE_PERMISSIONS);

        this.inboxGroupService.assignInboxes(tenantId, id, dto);
    }

    @GetMapping("/{id}/inboxes")
    public PageResponse<Inbox> getInboxes(@PathVariable Long tenantId, @PathVariable Long id) throws UnauthorizedException, NotFoundException {
        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to retrieve this resource for this tenant.");

        List<Inbox> inboxes = this.inboxGroupService.retrieveInboxes(tenantId, id);

        return new PageResponse<>(inboxes.size(), 1, inboxes);
    }

    @DeleteMapping("/{id}/inboxes/{inboxId}")
    public ResponseEntity<?> removeInbox(@PathVariable Long tenantId, @PathVariable Long id, @PathVariable Long inboxId) throws NotFoundException, UnauthorizedException, PermissionException, ConflictException, DomainInvalidException {
        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to delete this resource for this tenant.");

        throwIfMissingPermissions(CREATE_PERMISSIONS);

        this.inboxGroupService.removeInbox(tenantId, id, inboxId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

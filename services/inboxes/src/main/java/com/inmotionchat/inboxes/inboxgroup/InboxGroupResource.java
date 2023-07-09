package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.models.Permission.EDIT_INBOX_GROUPS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/inbox-groups")
public class InboxGroupResource extends AbstractResource<InboxGroup, InboxGroupDTO> {

    private static final Permission[] CREATE_PERMISSIONS = { EDIT_INBOX_GROUPS };
    private static final Permission[] UPDATE_PERMISSIONS = { EDIT_INBOX_GROUPS };

    protected InboxGroupResource(IdentityContext identityContext, InboxGroupService inboxGroupService) {
        super(identityContext, inboxGroupService);
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

}

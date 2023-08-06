package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.data.dto.ChatboxDTO;
import com.inmotionchat.core.data.postgres.journey.Chatbox;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.models.Permission.EDIT_CHATBOXES;
import static com.inmotionchat.core.models.Permission.READ_CHATBOXES;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/chatboxes")
public class ChatboxResource extends AbstractResource<Chatbox, ChatboxDTO> {

    protected static final Permission[] CREATE_PERMISSIONS = { EDIT_CHATBOXES };

    protected static final Permission[] READ_PERMISSIONS = { READ_CHATBOXES };

    @Autowired
    public ChatboxResource(IdentityContext identityContext, ChatboxService chatboxService) {
        super(identityContext, chatboxService);
    }

    @Override
    protected int getDefaultPageSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumPageSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected boolean isCreateEnabled() {
        return true;
    }

    @Override
    public Permission[] getCreatePermissions() {
        return CREATE_PERMISSIONS;
    }

    @Override
    protected boolean isGetEnabled() {
        return true;
    }

    @Override
    public Permission[] getGetPermissions() {
        return READ_PERMISSIONS;
    }

    @Override
    protected boolean isSearchEnabled() {
        return true;
    }

    @Override
    public Permission[] getSearchPermissions() {
        return READ_PERMISSIONS;
    }

    @Override
    protected boolean isUpdateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getUpdatePermissions() {
        return CREATE_PERMISSIONS;
    }

}

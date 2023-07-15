package com.inmotionchat.workflows.workflow;

import com.inmotionchat.core.data.dto.WorkflowDTO;
import com.inmotionchat.core.data.postgres.workflow.Workflow;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.models.Permission.EDIT_WORKFLOWS;
import static com.inmotionchat.core.models.Permission.READ_WORKFLOWS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/workflows")
public class WorkflowResource extends AbstractResource<Workflow, WorkflowDTO> {

    protected static final Permission[] READ_PERMISSIONS = { READ_WORKFLOWS };
    protected static final Permission[] CREATE_PERMISSIONS = { EDIT_WORKFLOWS };

    @Autowired
    public WorkflowResource(IdentityContext identityContext, WorkflowService workflowService) {
        super(identityContext, workflowService);
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
    public boolean isSearchEnabled() {
        return true;
    }

    @Override
    protected Permission[] getSearchPermissions() {
        return READ_PERMISSIONS;
    }

}

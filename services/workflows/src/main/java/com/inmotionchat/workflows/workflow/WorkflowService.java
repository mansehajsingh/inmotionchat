package com.inmotionchat.workflows.workflow;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.WorkflowDTO;
import com.inmotionchat.core.data.postgres.workflow.Workflow;

public interface WorkflowService extends ArchivingDomainService<Workflow, WorkflowDTO> {
}

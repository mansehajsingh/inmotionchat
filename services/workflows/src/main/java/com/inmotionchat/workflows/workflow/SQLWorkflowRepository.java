package com.inmotionchat.workflows.workflow;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.workflow.Workflow;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLWorkflowRepository extends SQLArchivingRepository<Workflow> {
}

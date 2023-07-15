package com.inmotionchat.workflows.workflow;

import com.inmotionchat.core.data.AbstractArchivingDomainService;
import com.inmotionchat.core.data.dto.WorkflowDTO;
import com.inmotionchat.core.data.postgres.workflow.Workflow;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl extends AbstractArchivingDomainService<Workflow, WorkflowDTO> implements WorkflowService {

    protected final static Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper()
            .key("name", String.class);

    @Autowired
    protected WorkflowServiceImpl(SQLWorkflowRepository repository) {
        super(Workflow.class, WorkflowDTO.class, log, repository, mapper);
    }

}

package com.inmotionchat.workflows;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkflowsService extends InMotionService {

    protected final static Logger log = LoggerFactory.getLogger(WorkflowsService.class);

    public WorkflowsService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "WorkflowsService";
    }

    @Override
    public String getServiceConfigFileName() {
        return "workflows.json";
    }

}

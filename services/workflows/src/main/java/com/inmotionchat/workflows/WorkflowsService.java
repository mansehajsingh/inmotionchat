package com.inmotionchat.workflows;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkflowsService extends InMotionService {

    private String geoIpDatabaseLocation;

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

    @ServiceProperty(name = "geoIpDatabaseLocation", required = true)
    public void setGeoIpDatabaseLocation(String geoIpDatabaseLocation) {
        this.geoIpDatabaseLocation = geoIpDatabaseLocation;
    }

    public String getGeoIpDatabaseLocation() {
        return this.geoIpDatabaseLocation;
    }

}

package com.inmotionchat.journeys;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JourneysService extends InMotionService {

    private String geoIpDatabaseLocation;

    protected final static Logger log = LoggerFactory.getLogger(JourneysService.class);

    public JourneysService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "JourneysService";
    }

    @Override
    public String getServiceConfigFileName() {
        return "journeys.json";
    }

    @ServiceProperty(name = "geoIpDatabaseLocation", required = true)
    public void setGeoIpDatabaseLocation(String geoIpDatabaseLocation) {
        this.geoIpDatabaseLocation = geoIpDatabaseLocation;
    }

    public String getGeoIpDatabaseLocation() {
        return this.geoIpDatabaseLocation;
    }

}

package com.inmotionchat.journeys;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JourneysService extends InMotionService {

    private File geoIpDatabase;

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
        return "journeys.toml";
    }

    @ServiceProperty(name = "geoIpDatabaseLocation", required = true)
    public void setGeoIpDatabase(String geoIpDatabaseLocation) {
        this.geoIpDatabase = new File(geoIpDatabaseLocation);
    }

    public File getGeoIpDatabase() {
        return this.geoIpDatabase;
    }

}

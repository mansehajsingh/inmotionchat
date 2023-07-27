package com.inmotionchat.journeys.geo;

import com.inmotionchat.journeys.JourneysService;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeolocationServiceImpl implements GeolocationService {

    private final static Logger log = LoggerFactory.getLogger(GeolocationServiceImpl.class);

    private final CHMCache geoCache;

    private final DatabaseReader reader;

    @Autowired
    public GeolocationServiceImpl(JourneysService journeysService) throws IOException {
        this.geoCache = new CHMCache();
        this.reader = new DatabaseReader.Builder(journeysService.getGeoIpDatabase()).withCache(geoCache).build();
        log.info("Loaded MaxMind GeoIp2 database from location {}.", journeysService.getGeoIpDatabase().getAbsolutePath());
    }

}

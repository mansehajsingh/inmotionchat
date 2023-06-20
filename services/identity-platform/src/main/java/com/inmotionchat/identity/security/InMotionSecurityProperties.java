package com.inmotionchat.identity.security;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import static com.inmotionchat.core.web.AbstractResource.PATH;

public class InMotionSecurityProperties {

    public static List<Endpoint> doNotAuthenticate() {
        return new ArrayList<>() {{
           add(new Endpoint(HttpMethod.POST, PATH + "/auth/login"));
           add(new Endpoint(HttpMethod.POST, PATH + "/users"));
           add(new Endpoint(HttpMethod.GET, PATH + "/auth/refresh"));
           add(new Endpoint(HttpMethod.PATCH, PATH + "/users/**"));
           add(new Endpoint(HttpMethod.POST, PATH + "/tenants"));
        }};
    }

}

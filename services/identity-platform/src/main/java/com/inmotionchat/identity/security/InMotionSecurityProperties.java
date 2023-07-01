package com.inmotionchat.identity.security;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import static com.inmotionchat.core.web.AbstractResource.API_V1;

public class InMotionSecurityProperties {

    public static List<Endpoint> doNotAuthenticate() {
        return new ArrayList<>() {{
           add(new Endpoint(HttpMethod.POST,  API_V1 + "/tenants"));
           add(new Endpoint(HttpMethod.POST, API_V1 + "/users"));
           add(new Endpoint(HttpMethod.POST, API_V1 + "/users/{}/verify"));
           add(new Endpoint(HttpMethod.GET, API_V1 + "/auth/refresh"));
        }};
    }

}

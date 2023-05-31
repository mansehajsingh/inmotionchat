package com.inmotionchat.identity.security;

import org.springframework.http.HttpMethod;

public class Endpoint {

    private final String path;

    private final HttpMethod method;

    public Endpoint(HttpMethod method, String path) {
        this.path = path;
        this.method = method;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

}

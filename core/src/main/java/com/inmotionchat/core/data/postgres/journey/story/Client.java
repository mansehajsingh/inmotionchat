package com.inmotionchat.core.data.postgres.journey.story;

import jakarta.persistence.Embeddable;
import jakarta.servlet.http.HttpServletRequest;

@Embeddable
public class Client {

    private String ipAddress;

    protected Client() {}

    public Client(HttpServletRequest request) {
        this.ipAddress = request.getRemoteAddr();
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

}

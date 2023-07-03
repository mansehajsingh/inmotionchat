package com.inmotionchat.core.messaging;

public class StreamKey {

    public static final StreamKey VERIFY_USER = new StreamKey("VERIFY_USER");

    private final String name;

    private StreamKey(String name) {
        this.name = name;
    }

    public String getKey() {
        return "streams:" + name;
    }

    public String getConsumerGroup() {
        return "cgroups:" + name;
    }

}

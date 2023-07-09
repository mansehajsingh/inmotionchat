package com.inmotionchat.core.messaging;

public class Stream {

    public static final Stream VERIFY_USER = new Stream("VERIFY_USER");

    public static final Stream PERSIST_USER = new Stream("PERSIST_USER");

    private final String name;

    private Stream(String name) {
        this.name = name;
    }

    public String getKey() {
        return "streams:" + name;
    }

    @Override
    public String toString() {
        return "RedisStream[key=" + getKey() + "]";
    }

}

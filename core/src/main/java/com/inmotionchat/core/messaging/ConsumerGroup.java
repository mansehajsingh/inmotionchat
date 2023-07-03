package com.inmotionchat.core.messaging;

import com.inmotionchat.core.soa.InMotionService;

public class ConsumerGroup {

    private final Class<? extends InMotionService> serviceClass;

    private final Stream stream;

    public ConsumerGroup(Class<? extends InMotionService> serviceClass, Stream stream) {
        this.serviceClass = serviceClass;
        this.stream = stream;
    }

    public String getGroupName() {
        return this.serviceClass.getSimpleName();
    }

    public Stream getStream() {
        return this.stream;
    }

    @Override
    public String toString() {
        return "RedisConsumerGroup[service=" + serviceClass.getSimpleName() + ", streamKey=" + stream.getKey() + "]";
    }

}

package com.inmotionchat.core.messaging;

import org.slf4j.Logger;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

public abstract class Consumer<T> implements StreamListener<String, ObjectRecord<String, T>> {

    protected final Logger log;

    protected Consumer(Logger log) {
        this.log = log;
    }

    @Override
    public void onMessage(ObjectRecord<String, T> message) {
        log.debug("Consumed event details {}. Proceeding to process.", message.getValue());
        process(message.getValue());
    }

    protected abstract void process(T details);

}

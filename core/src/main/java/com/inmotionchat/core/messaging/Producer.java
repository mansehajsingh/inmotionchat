package com.inmotionchat.core.messaging;

import com.inmotionchat.core.data.events.StreamEvent;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.event.TransactionalEventListener;

public abstract class Producer<E extends StreamEvent<?>> {

    protected final Logger log;

    protected final RedisTemplate<String, Object> redisTemplate;

    protected final String streamKey;

    protected Producer(Logger log, RedisTemplate<String, Object> redisTemplate, String streamKey) {
        this.log = log;
        this.redisTemplate = redisTemplate;
        this.streamKey = streamKey;
    }

    @TransactionalEventListener
    public void handleEvent(E event) {
        ObjectRecord<String, ?> record = StreamRecords.newRecord()
                .ofObject(event.getDetails())
                .withStreamKey(streamKey);

        this.redisTemplate
                .opsForStream()
                .add(record);

        log.debug("Produced event {}.", event);
    }

}

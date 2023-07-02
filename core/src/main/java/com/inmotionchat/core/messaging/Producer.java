package com.inmotionchat.core.messaging;

import com.inmotionchat.core.data.events.StreamEvent;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.event.TransactionalEventListener;

public abstract class Producer<E extends StreamEvent<?>> {

    protected final RedisTemplate<String, Object> redisTemplate;

    protected final String streamKey;

    protected Producer(RedisTemplate<String, Object> redisTemplate, String streamKey) {
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
    }

}

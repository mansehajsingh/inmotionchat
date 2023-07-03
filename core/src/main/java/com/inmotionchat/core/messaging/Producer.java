package com.inmotionchat.core.messaging;

import com.inmotionchat.core.data.events.StreamEvent;
import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.inmotionchat.core.util.misc.ServiceResolution.isFromService;

public abstract class Producer<E extends StreamEvent<?>> {

    protected final Logger log;

    protected InMotionService service;

    protected final RedisTemplate<String, String> redisTemplate;

    protected final StreamKey streamKey;

    protected Producer(Logger log, InMotionService service, RedisTemplate<String, String> redisTemplate, StreamKey streamKey) {
        this.log = log;
        this.service = service;
        this.redisTemplate = redisTemplate;
        this.streamKey = streamKey;
    }

    @TransactionalEventListener
    public void handleEvent(E event) {
        if (!service.isRunning() || !isFromService(event.getSource().getClass(), service.getClass())) {
            // if the service isn't running or the event was not sent from the producer's service,
            // don't publish the event from here
            return;
        }

        ObjectRecord<String, ?> record = StreamRecords.newRecord()
                .ofObject(event.getDetails())
                .withStreamKey(streamKey.getKey());

        RecordId recordId = this.redisTemplate
                .opsForStream()
                .add(record);

        log.debug("Produced event with id: {}, details: {}.", recordId, event.getDetails());
    }

}

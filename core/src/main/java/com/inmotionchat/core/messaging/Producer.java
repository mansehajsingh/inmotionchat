package com.inmotionchat.core.messaging;

import com.inmotionchat.core.data.events.StreamEvent;
import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import static com.inmotionchat.core.util.misc.ServiceResolution.isFromService;

public abstract class Producer<E extends StreamEvent> {

    protected final Logger log;

    protected InMotionService service;

    protected final RedisTemplate<String, String> redisTemplate;

    protected final Stream stream;

    protected final Class<E> eventType;

    protected Producer(Class<E> eventType,
                       Logger log,
                       InMotionService service,
                       RedisTemplate<String, String> redisTemplate,
                       Stream stream) {
        this.eventType = eventType;
        this.log = log;
        this.service = service;
        this.redisTemplate = redisTemplate;
        this.stream = stream;
    }

    public void handleEvent(StreamEvent event) {
        if (!eventType.isInstance(event) || !service.isRunning() || !isFromService(event.getSource(), service.getClass())) {
            // if the service isn't running or the event was not sent from the producer's service,
            // don't publish the event from here

            // we need to check for eventType.isInstance(event) due to the event listener picking up ALL stream events
            // because of type erasure at runtime
            return;
        }

        ObjectRecord<String, ?> record = StreamRecords.newRecord()
                .ofObject(event)
                .withStreamKey(stream.getKey());

        RecordId recordId = this.redisTemplate
                .opsForStream()
                .add(record);

        log.debug("Produced event with id: {}, details: {}.", recordId, event);
    }

}

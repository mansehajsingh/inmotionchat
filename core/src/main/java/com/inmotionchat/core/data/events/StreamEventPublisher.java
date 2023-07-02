package com.inmotionchat.core.data.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StreamEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(StreamEventPublisher.class);

    private final ApplicationEventPublisher publisher;

    @Autowired
    public StreamEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(StreamEvent<?> event) {
        this.publisher.publishEvent(event);
        log.debug("Published event {} from source {}.", event, event.getSource());
    }

}

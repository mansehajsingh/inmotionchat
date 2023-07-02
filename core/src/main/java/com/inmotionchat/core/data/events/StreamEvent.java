package com.inmotionchat.core.data.events;

import org.springframework.context.ApplicationEvent;

public abstract class StreamEvent<T> extends ApplicationEvent {

    protected StreamEvent(Object source) {
        super(source);
    }

    /**
     * Returns the actual data pushed to the stream.
     */
    public abstract T getDetails();

}

package com.inmotionchat.core.data.events;

import com.inmotionchat.core.messaging.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class StreamEventPublisher {

    protected final static Logger log = LoggerFactory.getLogger(StreamEventPublisher.class);

    private final Collection<Producer> producers;

    private final AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    public StreamEventPublisher(ListableBeanFactory listableBeanFactory, AsyncTaskExecutor asyncTaskExecutor) {
        this.producers = listableBeanFactory.getBeansOfType(Producer.class).values();
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public void publish(StreamEvent event) {
        this.asyncTaskExecutor.submit(new PublishEventTask(producers, event));
    }

    private static class PublishEventTask implements Runnable {

        private final Collection<Producer> producers;

        private final StreamEvent event;

        public PublishEventTask(Collection<Producer> producers, StreamEvent event) {
            this.producers = producers;
            this.event = event;

        }

        @Override
        public void run() {
            for (Producer<?> producer : producers) {
                try {
                    producer.handleEvent(event);
                } catch (Exception e) {
                    log.error("Encountered error executing producer [{}] with exception {}", producer.getClass().getName(), e.toString());
                }
            }
        }

    }

}

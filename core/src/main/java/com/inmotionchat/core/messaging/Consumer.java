package com.inmotionchat.core.messaging;

import org.slf4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

public abstract class Consumer<T> implements StreamListener<String, ObjectRecord<String, T>> {

    protected final Logger log;

    protected Consumer(Logger log, ConsumerGroup consumerGroup, RedisTemplate<String, String> redisTemplate) {
        this.log = log;
        try {

            redisTemplate.opsForStream().createGroup(
                    consumerGroup.getStream().getKey(),
                    consumerGroup.getGroupName()
            );
            log.debug("Created redis consumer group {} for stream {}.", consumerGroup, consumerGroup.getStream());

        } catch (InvalidDataAccessApiUsageException e) {

            log.debug("Tried to create redis consumer group {} for stream {}, but found that it already exists.",
                    consumerGroup, consumerGroup.getStream());

        }
    }

    @Override
    public void onMessage(ObjectRecord<String, T> message) {
        log.debug("Consumed event with id: {}, details: {}", message.getId(), message.getValue());
        process(message.getId(), message.getValue());
    }

    protected abstract void process(RecordId recordId, T details);

    protected abstract ConsumerGroup getConsumerGroup();

    public class SubscriptionBuilder {

        private Duration pollTimeout = Duration.ofSeconds(1);
        private Class<T> eventDetailsType;
        private RedisConnectionFactory redisConnectionFactory;
        private ConsumerGroup consumerGroup = getConsumerGroup();
        private Consumer<T> subclass;

        public SubscriptionBuilder(Consumer<T> subclass, Class<T> eventDetailsType, RedisConnectionFactory connectionFactory) {
            this.subclass = subclass;
            this.eventDetailsType = eventDetailsType;
            this.redisConnectionFactory = connectionFactory;
        }

        public SubscriptionBuilder setPollTimeout(Duration pollTimeout) {
            this.pollTimeout = pollTimeout;
            return this;
        }

        public SubscriptionBuilder setConsumerGroup(ConsumerGroup consumerGroup) {
            this.consumerGroup = consumerGroup;
            return this;
        }

        public Subscription build() {
            StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, T>> options =
                    StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                            .builder()
                            .pollTimeout(this.pollTimeout)
                            .targetType(this.eventDetailsType)
                            .build();

            StreamMessageListenerContainer<String, ObjectRecord<String, T>> listenerContainer = StreamMessageListenerContainer
                    .create(this.redisConnectionFactory, options);

            Subscription subscription = listenerContainer.receive(
                        org.springframework.data.redis.connection.stream.Consumer.from(
                                consumerGroup.getGroupName(), subclass.getClass().getName()
                        ),
                        StreamOffset.create(consumerGroup.getStream().getKey(), ReadOffset.lastConsumed()),
                        this.subclass
                    );

            listenerContainer.start();

            return subscription;
        }

    }

}

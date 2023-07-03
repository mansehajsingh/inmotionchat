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

    protected Consumer(Logger log, StreamKey streamKey, RedisTemplate<String, String> redisTemplate) {
        this.log = log;
        try {
            redisTemplate.opsForStream().createGroup(streamKey.getKey(), streamKey.getConsumerGroup());
            log.debug("Created redis consumer group \"{}\" for stream \"{}\".", streamKey.getConsumerGroup(), streamKey.getKey());
        } catch (InvalidDataAccessApiUsageException e) {
            log.debug("Tried to create redis consumer group \"{}\" for stream \"{}\", but found that it already exists.",
                    streamKey.getConsumerGroup(), streamKey.getKey());
        }
    }

    @Override
    public void onMessage(ObjectRecord<String, T> message) {
        log.debug("Consumed event with id: {}, details: {}", message.getId(), message.getValue());
        process(message.getId(), message.getValue());
    }

    protected abstract void process(RecordId recordId, T details);

    protected abstract String getConsumerName();

    public class SubscriptionBuilder {

        private Duration pollTimeout = Duration.ofSeconds(1);
        private Class<T> targetType;
        private RedisConnectionFactory redisConnectionFactory;
        private StreamKey streamKey;
        private String consumerName = getConsumerName();
        private Consumer<T> subclass;

        public SubscriptionBuilder setPollTimeout(Duration pollTimeout) {
            this.pollTimeout = pollTimeout;
            return this;
        }

        public SubscriptionBuilder setTargetType(Class<T> clazz) {
            this.targetType = clazz;
            return this;
        }

        public SubscriptionBuilder setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
            this.redisConnectionFactory = redisConnectionFactory;
            return this;
        }

        public SubscriptionBuilder setStreamKey(StreamKey streamKey) {
            this.streamKey = streamKey;
            return this;
        }

        public SubscriptionBuilder setConsumerName(String consumerName) {
            this.consumerName = consumerName;
            return this;
        }

        public SubscriptionBuilder setHandler(Consumer<T> subclass) {
            this.subclass = subclass;
            return this;
        }

        public Subscription build() {
            StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, T>> options =
                    StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                            .builder()
                            .pollTimeout(this.pollTimeout)
                            .targetType(this.targetType)
                            .build();

            StreamMessageListenerContainer<String, ObjectRecord<String, T>> listenerContainer = StreamMessageListenerContainer
                    .create(this.redisConnectionFactory, options);

            Subscription subscription = listenerContainer.receive(
                        org.springframework.data.redis.connection.stream.Consumer.from(this.streamKey.getConsumerGroup(), this.consumerName),
                        StreamOffset.create(this.streamKey.getKey(), ReadOffset.lastConsumed()),
                        this.subclass
                    );

            listenerContainer.start();

            return subscription;
        }

    }

}

package com.inmotionchat.notifications.messaging;

import com.inmotionchat.core.data.events.VerifyEvent;
import com.inmotionchat.core.messaging.Consumer;
import com.inmotionchat.core.messaging.StreamKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

@Component
public class VerifyEventConsumer extends Consumer<VerifyEvent.Details> {

    protected final static Logger log = LoggerFactory.getLogger(VerifyEventConsumer.class);

    protected final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public VerifyEventConsumer(RedisConnectionFactory redisConnectionFactory, RedisTemplate<String, String> redisTemplate) {
        super(log, StreamKey.VERIFY_USER, redisTemplate);
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public String getConsumerName() {
        return "Notifications:VerifyEventConsumer";
    }

    @Override
    protected void process(RecordId recordId, VerifyEvent.Details details) {
        // TODO: Submit to email executor service
    }

    @Bean
    public Subscription verifyEventSubscription() {
        return new SubscriptionBuilder()
                .setTargetType(VerifyEvent.Details.class)
                .setRedisConnectionFactory(redisConnectionFactory)
                .setStreamKey(StreamKey.VERIFY_USER)
                .setHandler(this)
                .build();
    }

}

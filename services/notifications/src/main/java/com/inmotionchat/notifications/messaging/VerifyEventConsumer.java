package com.inmotionchat.notifications.messaging;

import com.inmotionchat.core.data.events.VerifyEvent;
import com.inmotionchat.core.messaging.Consumer;
import com.inmotionchat.core.messaging.ConsumerGroup;
import com.inmotionchat.core.messaging.Stream;
import com.inmotionchat.notifications.NotificationsService;
import com.inmotionchat.notifications.service.NotifierService;
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

    public static ConsumerGroup consumerGroup = new ConsumerGroup(NotificationsService.class, Stream.VERIFY_USER);

    private final NotifierService notifierService;

    private final NotificationsService service;

    @Autowired
    public VerifyEventConsumer(NotificationsService service,
                               RedisConnectionFactory redisConnectionFactory,
                               RedisTemplate<String, String> redisTemplate,
                               NotifierService notifierService) {
        super(log, consumerGroup, redisTemplate);
        this.service = service;
        this.redisConnectionFactory = redisConnectionFactory;
        this.notifierService = notifierService;
    }

    @Override
    public ConsumerGroup getConsumerGroup() {
        return consumerGroup;
    }

    @Override
    protected void process(RecordId recordId, VerifyEvent.Details details) {
        this.notifierService.sendNotifications(details);
    }

    @Bean
    public Subscription verifyEventSubscription() {
        if (service.isRunning()) {
            return new SubscriptionBuilder(this, VerifyEvent.Details.class, redisConnectionFactory).build();
        } else {
            return null;
        }
    }

}

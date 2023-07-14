package com.inmotionchat.inboxes.messaging;

import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.events.PersistUserEvent;
import com.inmotionchat.core.messaging.Consumer;
import com.inmotionchat.core.messaging.ConsumerGroup;
import com.inmotionchat.core.messaging.Stream;
import com.inmotionchat.inboxes.InboxesService;
import com.inmotionchat.inboxes.inbox.InboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

@Component(value = PersistUserEventConsumer.NAME)
public class PersistUserEventConsumer extends Consumer<PersistUserEvent> {

    public static final String NAME = "InboxesServicePersistUserEventConsumer";

    protected final static Logger log = LoggerFactory.getLogger(PersistUserEventConsumer.class);

    protected final RedisConnectionFactory redisConnectionFactory;

    public static ConsumerGroup consumerGroup = new ConsumerGroup(InboxesService.class, Stream.PERSIST_USER);

    private final InboxesService inboxesService;

    private final InboxService inboxService;

    @Autowired
    public PersistUserEventConsumer(InboxesService inboxesService,
                                    RedisConnectionFactory redisConnectionFactory,
                                    RedisTemplate<String, String> redisTemplate,
                                    InboxService inboxService) {
        super(log, consumerGroup, redisTemplate);
        this.inboxesService = inboxesService;
        this.redisConnectionFactory = redisConnectionFactory;
        this.inboxService = inboxService;
    }

    @Override
    protected void process(RecordId recordId, PersistUserEvent details) throws Exception {
        InboxDTO inboxDTO = new InboxDTO(details.userId());
        this.inboxService.create(details.tenantId(), inboxDTO);
    }

    @Override
    protected ConsumerGroup getConsumerGroup() {
        return consumerGroup;
    }

    @Bean(name = NAME + "Subscription")
    public Subscription subscription() {
        if (inboxesService.isRunning()) {
            return new SubscriptionBuilder(this, PersistUserEvent.class, redisConnectionFactory).build();
        } else {
            return null;
        }
    }

}

package com.inmotionchat.identity.messaging;

import com.inmotionchat.core.data.events.PersistUserEvent;
import com.inmotionchat.core.messaging.Producer;
import com.inmotionchat.core.messaging.Stream;
import com.inmotionchat.identity.IdentityPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component(value = PersistUserEventProducer.NAME)
public class PersistUserEventProducer extends Producer<PersistUserEvent> {

    public static final String NAME = "IdentityPlatformServicePersistUserEventProducer";

    protected final static Logger log = LoggerFactory.getLogger(PersistUserEventProducer.class);

    @Autowired
    protected PersistUserEventProducer(IdentityPlatformService identityPlatformService,
                                       RedisTemplate<String, String> redisTemplate) {
        super(PersistUserEvent.class, log, identityPlatformService, redisTemplate, Stream.PERSIST_USER);
    }

}

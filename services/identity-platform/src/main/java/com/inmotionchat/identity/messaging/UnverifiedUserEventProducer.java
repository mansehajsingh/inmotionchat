package com.inmotionchat.identity.messaging;

import com.inmotionchat.core.data.events.UnverifiedUserEvent;
import com.inmotionchat.core.messaging.Producer;
import com.inmotionchat.core.messaging.Stream;
import com.inmotionchat.identity.IdentityPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component(value = UnverifiedUserEventProducer.NAME)
public class UnverifiedUserEventProducer extends Producer<UnverifiedUserEvent> {

    public static final String NAME = "IdentityPlatformServiceUnverifiedUserEventProducer";

    private final static Logger log = LoggerFactory.getLogger(UnverifiedUserEventProducer.class);

    @Autowired
    public UnverifiedUserEventProducer(IdentityPlatformService service, RedisTemplate<String, String> redisTemplate) {
        super(UnverifiedUserEvent.class, log, service, redisTemplate, Stream.UNVERIFIED_USER);
    }

}

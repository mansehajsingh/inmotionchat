package com.inmotionchat.identity.messaging;

import com.inmotionchat.core.data.events.VerifyEvent;
import com.inmotionchat.core.messaging.Producer;
import com.inmotionchat.core.messaging.Stream;
import com.inmotionchat.identity.IdentityPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class IdpVerifyEventProducer extends Producer<VerifyEvent> {

    private final static Logger log = LoggerFactory.getLogger(IdpVerifyEventProducer.class);

    @Autowired
    public IdpVerifyEventProducer(IdentityPlatformService service, RedisTemplate<String, String> redisTemplate) {
        super(VerifyEvent.class, log, service, redisTemplate, Stream.VERIFY_USER);
    }

}

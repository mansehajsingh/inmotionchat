package com.inmotionchat.core.data.redis;

import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@RedisHash("sessions")
public class RedisSession implements Session {

    private static final Long EXPIRY_PERIOD = 2628000L * 6L; // # of seconds in a month * 6 months

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long timeToLive = EXPIRY_PERIOD;

    @Id
    private Long id;

    private Long userId;

    public RedisSession() {}

    public RedisSession(Session session) {
        this.id = session.getId();
        this.userId = session.getUser().getId();
        this.timeToLive = ChronoUnit.SECONDS.between(ZonedDateTime.now(), session.getExpiresAt());
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public User getUser() {
        return SQLUser.fromId(id);
    }

    @Override
    public ZonedDateTime getExpiresAt() {
        return ZonedDateTime.now().plus(this.timeToLive, ChronoUnit.SECONDS);
    }

    @Override
    public boolean isExpired() {
        return false; // redis key will expire with TTL so a fetched one can never be expired
    }

}

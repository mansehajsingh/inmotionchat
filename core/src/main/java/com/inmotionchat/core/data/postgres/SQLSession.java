package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Session;
import com.inmotionchat.core.domains.User;
import jakarta.persistence.*;

import java.time.Period;
import java.time.ZonedDateTime;

@Entity
@Table(name = "sessions", schema = Schema.IdentityPlatform)
public class SQLSession implements Session {

    private static final Period EXPIRY_PERIOD = Period.ofMonths(6);

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private SQLUser user;

    private ZonedDateTime expiresAt;

    public SQLSession() {}

    public SQLSession(Long id, SQLUser user) {
        this.id = id;
        this.user = user;
        this.expiresAt = ZonedDateTime.now().plus(EXPIRY_PERIOD);
    }

    public SQLSession(SQLUser user) {
        this.user = user;
        this.expiresAt = ZonedDateTime.now().plus(EXPIRY_PERIOD);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public User getUser() {
        return this.user ;
    }

    @Override
    public ZonedDateTime getExpiresAt() {
        return this.expiresAt;
    }

    @Override
    public boolean isExpired() {
        return this.expiresAt.isAfter(ZonedDateTime.now());
    }
}

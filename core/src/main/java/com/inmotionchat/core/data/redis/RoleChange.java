package com.inmotionchat.core.data.redis;

import com.inmotionchat.core.models.RoleChangeType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash
public class RoleChange {

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long timeToLive = 1L;

    @Id
    private Long roleId;

    private RoleChangeType changeType;

    @Indexed
    private Long userId;

    public RoleChange(Long roleId, RoleChangeType changeType) {
        this.roleId = roleId;
        this.changeType = changeType;
    }

    public RoleChange(Long roleId, RoleChangeType changeType, Long userId) {
        this(roleId, changeType);
        this.userId = userId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public RoleChangeType getChangeType() {
        return this.changeType;
    }

    public Long getUserId() {
        return userId;
    }

}

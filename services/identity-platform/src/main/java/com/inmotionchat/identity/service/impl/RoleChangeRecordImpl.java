package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.redis.RoleChange;
import com.inmotionchat.identity.redis.RedisRoleChangeRepository;
import com.inmotionchat.identity.service.contract.RoleChangeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleChangeRecordImpl implements RoleChangeRecord {

    private final RedisRoleChangeRepository redisRoleChangeRepository;

    @Autowired
    public RoleChangeRecordImpl(RedisRoleChangeRepository redisRoleChangeRepository) {
        this.redisRoleChangeRepository = redisRoleChangeRepository;
    }


    @Override
    public boolean roleHasChanged(Long roleId, Long userId) {
        List<RoleChange> roleChanges = this.redisRoleChangeRepository.findAllByRoleId(roleId);

        for (RoleChange roleChange : roleChanges) {
            if (roleChange.getRoleId().equals(roleId)) {
                return switch (roleChange.getChangeType()) {
                    case PERMISSIONS -> true;
                    case REASSIGNMENT -> roleChange.getUserId().equals(userId);
                };
            }
        }

        return false;
    }

}

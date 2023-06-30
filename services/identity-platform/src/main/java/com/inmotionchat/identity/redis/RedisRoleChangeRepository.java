package com.inmotionchat.identity.redis;

import com.inmotionchat.core.data.redis.RoleChange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisRoleChangeRepository extends CrudRepository<RoleChange, Long> {

    List<RoleChange> findAllByRoleId(Long roleId);

}

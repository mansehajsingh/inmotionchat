package com.inmotionchat.identity.redis;

import com.inmotionchat.core.data.redis.RedisSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisSessionRepository extends CrudRepository<RedisSession, Long> {
}

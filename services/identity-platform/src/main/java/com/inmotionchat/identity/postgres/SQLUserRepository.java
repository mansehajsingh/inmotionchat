package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLUserRepository extends SmartJPARepository<User, Long> {
}

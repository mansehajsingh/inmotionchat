package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.SQLUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLUserRepository extends SQLArchivingRepository<SQLUser> {
}

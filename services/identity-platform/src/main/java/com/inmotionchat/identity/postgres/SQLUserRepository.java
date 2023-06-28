package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.SQLUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLUserRepository extends JpaRepository<SQLUser, Long> {
}

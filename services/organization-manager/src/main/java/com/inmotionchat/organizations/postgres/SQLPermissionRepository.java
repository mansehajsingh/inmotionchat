package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.postgres.SQLPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLPermissionRepository extends JpaRepository<SQLPermission, Long> {
}

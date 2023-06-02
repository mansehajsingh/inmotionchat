package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLRoleAssignment;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLRoleAssignmentRepository extends SQLRepository<SQLRoleAssignment> {
}

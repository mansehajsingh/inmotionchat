package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.data.postgres.SQLRoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLRoleAssignmentRepository extends JpaRepository<SQLRoleAssignment, Long> {

    Integer countSQLRoleAssignmentByRole(SQLRole role);

}

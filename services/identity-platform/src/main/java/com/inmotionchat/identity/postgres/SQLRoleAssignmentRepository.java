package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.data.postgres.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SQLRoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    Integer countSQLRoleAssignmentByRole(Role role);

    Optional<RoleAssignment> findRoleAssignmentByUserId(Long userId);

}

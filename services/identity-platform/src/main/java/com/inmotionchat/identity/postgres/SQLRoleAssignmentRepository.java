package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.RoleAssignment;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SQLRoleAssignmentRepository extends SmartJPARepository<RoleAssignment, Long> {

    Integer countSQLRoleAssignmentByRole(Role role);

    Optional<RoleAssignment> findRoleAssignmentByUserId(Long userId);

    List<RoleAssignment> findAllByRoleId(Long roleId);

}

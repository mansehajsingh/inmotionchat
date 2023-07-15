package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.RoleAssignment;
import com.inmotionchat.core.exceptions.ConflictException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SQLRoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    Integer countSQLRoleAssignmentByRole(Role role);

    Optional<RoleAssignment> findRoleAssignmentByUserId(Long userId);

    List<RoleAssignment> findAllByRoleId(Long roleId);

    default RoleAssignment store(RoleAssignment roleAssignment) throws ConflictException {
        try {
            return saveAndFlush(roleAssignment);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintEx = ((ConstraintViolationException) e.getCause());
            String constraint = constraintEx.getConstraintName();
            throw new ConflictException(constraint, "This user is already assigned a role.");
        }
    }

}

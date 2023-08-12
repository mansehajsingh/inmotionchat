package com.inmotionchat.inboxes.inboxgroupassignment;

import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.data.postgres.inbox.InboxGroupAssignment;
import com.inmotionchat.core.exceptions.ConflictException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SQLInboxGroupAssignmentRepository extends JpaRepository<InboxGroupAssignment, Long> {

    List<InboxGroupAssignment> findAllByGroup(InboxGroup inboxGroup);

    default List<InboxGroupAssignment> storeAll(List<InboxGroupAssignment> inboxGroupAssignments) throws ConflictException {
        try {
            return saveAllAndFlush(inboxGroupAssignments);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintEx = ((ConstraintViolationException) e.getCause());
            String constraint = constraintEx.getConstraintName();
            throw new ConflictException(constraint, "One or more provided inbox already belongs to this group.");
        }
    }

    void deleteByGroupIdAndInboxId(Long groupId, Long inboxId);

    Optional<InboxGroupAssignment> findByGroupIdAndInboxId(Long groupId, Long inboxId);

}

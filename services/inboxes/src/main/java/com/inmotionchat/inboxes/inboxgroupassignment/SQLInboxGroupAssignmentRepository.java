package com.inmotionchat.inboxes.inboxgroupassignment;

import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.data.postgres.inbox.InboxGroupAssignment;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SQLInboxGroupAssignmentRepository extends SmartJPARepository<InboxGroupAssignment, Long> {

    List<InboxGroupAssignment> findAllByGroup(InboxGroup inboxGroup);

    void deleteByGroupIdAndInboxId(Long groupId, Long inboxId);

    Optional<InboxGroupAssignment> findByGroupIdAndInboxId(Long groupId, Long inboxId);

}

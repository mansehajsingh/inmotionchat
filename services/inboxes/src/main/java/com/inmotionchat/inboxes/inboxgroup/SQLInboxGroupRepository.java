package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLInboxGroupRepository extends SmartJPARepository<InboxGroup, Long> {}

package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLInboxGroupRepository extends SQLRepository<InboxGroup> {}

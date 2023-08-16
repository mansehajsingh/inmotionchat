package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.smartpersist.SmartJPARepository;

public interface SQLInboxRepository extends SmartJPARepository<Inbox, Long> {}

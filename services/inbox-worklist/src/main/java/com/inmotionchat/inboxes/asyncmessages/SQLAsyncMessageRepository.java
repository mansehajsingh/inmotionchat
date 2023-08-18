package com.inmotionchat.inboxes.asyncmessages;

import com.inmotionchat.core.data.postgres.inbox.AsynchronousMessage;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLAsyncMessageRepository extends SmartJPARepository<AsynchronousMessage, Long> {
}

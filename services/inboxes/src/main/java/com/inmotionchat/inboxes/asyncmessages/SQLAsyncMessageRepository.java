package com.inmotionchat.inboxes.asyncmessages;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.inbox.AsynchronousMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLAsyncMessageRepository extends SQLRepository<AsynchronousMessage> {
}

package com.inmotionchat.inboxes.asyncmessages;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.AsyncMessageDTO;
import com.inmotionchat.core.data.postgres.inbox.AsynchronousMessage;

public interface AsyncMessageService extends DomainService<AsynchronousMessage, AsyncMessageDTO> {
}

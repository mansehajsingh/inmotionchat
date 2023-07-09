package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;

public interface InboxService extends DomainService<Inbox, InboxDTO> {}

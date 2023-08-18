package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.security.IdentityContext;

public interface InboxService extends DomainService<Inbox, InboxDTO> {

    void setIdentityContext(IdentityContext identityContext);

}

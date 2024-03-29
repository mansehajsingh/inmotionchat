package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.security.IdentityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class InboxServiceImpl extends AbstractDomainService<Inbox, InboxDTO> implements InboxService {

    protected final static Logger log = LoggerFactory.getLogger(InboxServiceImpl.class);

    @Autowired
    protected InboxServiceImpl(PlatformTransactionManager transactionManager,
                               AuditManager auditManager,
                               InboxAuditActionProvider auditActionProvider,
                               SQLInboxRepository sqlInboxRepository) {
        super(Inbox.class, InboxDTO.class, log, transactionManager, null, sqlInboxRepository, auditManager, auditActionProvider);
    }

    @Override
    public void setIdentityContext(IdentityContext identityContext) {
        this.identityContext = identityContext;
    }

}

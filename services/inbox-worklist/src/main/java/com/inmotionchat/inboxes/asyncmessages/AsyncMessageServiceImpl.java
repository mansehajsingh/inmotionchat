package com.inmotionchat.inboxes.asyncmessages;

import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.dto.AsyncMessageDTO;
import com.inmotionchat.core.data.postgres.inbox.AsynchronousMessage;
import com.inmotionchat.core.security.IdentityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class AsyncMessageServiceImpl extends AbstractDomainService<AsynchronousMessage, AsyncMessageDTO> implements AsyncMessageService {

    private static final Logger log = LoggerFactory.getLogger(AsyncMessageServiceImpl.class);

    @Autowired
    protected AsyncMessageServiceImpl(PlatformTransactionManager transactionManager,
                                      IdentityContext identityContext,
                                      SQLAsyncMessageRepository repository,
                                      AuditManager auditManager) {
        super(AsynchronousMessage.class, AsyncMessageDTO.class, log, transactionManager, identityContext, repository, auditManager, null);
    }

}

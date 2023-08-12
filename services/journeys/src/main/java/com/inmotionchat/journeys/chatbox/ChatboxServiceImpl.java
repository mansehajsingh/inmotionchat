package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.AbstractArchivingDomainService;
import com.inmotionchat.core.data.dto.ChatboxDTO;
import com.inmotionchat.core.data.postgres.journey.Chatbox;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class ChatboxServiceImpl extends AbstractArchivingDomainService<Chatbox, ChatboxDTO> implements ChatboxService{

    private static final Logger log = LoggerFactory.getLogger(ChatboxServiceImpl.class);

    private static final SearchCriteriaMapper mapper = new SearchCriteriaMapper().key("name", String.class);

    @Autowired
    public ChatboxServiceImpl(PlatformTransactionManager transactionManager,
                              IdentityContext identityContext,
                              SQLChatboxRepository repository,
                              AuditManager auditManager,
                              ChatboxAuditActionProvider chatboxAuditActionProvider) {
        super(Chatbox.class, ChatboxDTO.class, log, transactionManager, identityContext, repository, auditManager, chatboxAuditActionProvider, mapper);
    }

}

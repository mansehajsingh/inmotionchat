package com.inmotionchat.inboxes.inbox;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.postgres.Inbox;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InboxServiceImpl extends AbstractDomainService<Inbox, InboxDTO> implements InboxService {

    protected final static Logger log = LoggerFactory.getLogger(InboxServiceImpl.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper();

    @Autowired
    protected InboxServiceImpl(SQLInboxRepository sqlInboxRepository) {
        super(Inbox.class, InboxDTO.class, log, sqlInboxRepository, mapper);
    }

}

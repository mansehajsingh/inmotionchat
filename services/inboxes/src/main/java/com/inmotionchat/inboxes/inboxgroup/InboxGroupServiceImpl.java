package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InboxGroupServiceImpl extends AbstractDomainService<InboxGroup, InboxGroupDTO> implements InboxGroupService {

    protected static Logger log = LoggerFactory.getLogger(InboxGroupService.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper();

    @Autowired
    protected InboxGroupServiceImpl(SQLInboxGroupRepository sqlInboxGroupRepository) {
        super(InboxGroup.class, InboxGroupDTO.class, log, sqlInboxGroupRepository, mapper);
    }

}

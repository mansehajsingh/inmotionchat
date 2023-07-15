package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.InboxGroupAssignmentsDTO;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.data.postgres.inbox.InboxGroupAssignment;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.inboxes.inbox.InboxService;
import com.inmotionchat.inboxes.inboxgroupassignment.SQLInboxGroupAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Service
public class InboxGroupServiceImpl extends AbstractDomainService<InboxGroup, InboxGroupDTO> implements InboxGroupService {

    protected static Logger log = LoggerFactory.getLogger(InboxGroupService.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper();

    protected final SQLInboxGroupAssignmentRepository sqlInboxGroupAssignmentRepository;

    protected final InboxService inboxService;

    protected final ThrowingTransactionTemplate transactionTemplate;

    @Autowired
    protected InboxGroupServiceImpl(SQLInboxGroupRepository sqlInboxGroupRepository,
                                    SQLInboxGroupAssignmentRepository sqlInboxGroupAssignmentRepository,
                                    InboxService inboxService,
                                    PlatformTransactionManager transactionManager) {
        super(InboxGroup.class, InboxGroupDTO.class, log, sqlInboxGroupRepository, mapper);
        this.sqlInboxGroupAssignmentRepository = sqlInboxGroupAssignmentRepository;
        this.inboxService = inboxService;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
    }

    @Override
    public List<InboxGroupAssignment> assignInboxes(Long tenantId, Long inboxGroupId, InboxGroupAssignmentsDTO dto) throws NotFoundException, ConflictException {
        InboxGroup inboxGroup = retrieveById(tenantId, inboxGroupId);

        List<InboxGroupAssignment> groupAssignments = dto.inboxes().stream().map(id -> {
            Inbox inbox = new Inbox();
            inbox.setId(id);

            return new InboxGroupAssignment(inbox, inboxGroup);
        }).toList();

       return this.sqlInboxGroupAssignmentRepository.storeAll(groupAssignments);
    }

    @Override
    public List<Inbox> retrieveInboxes(Long tenantId, Long inboxGroupId) throws NotFoundException {
        InboxGroup inboxGroup = retrieveById(tenantId, inboxGroupId);

        return this.sqlInboxGroupAssignmentRepository.findAllByGroup(inboxGroup)
                .stream()
                .map(InboxGroupAssignment::getInbox)
                .filter(inbox -> inbox.getTenant().getId().equals(tenantId))
                .toList();
    }

    @Override
    public Inbox removeInbox(Long tenantId, Long inboxGroupId, Long inboxId) throws NotFoundException, ConflictException, DomainInvalidException {
        Inbox inbox = this.inboxService.retrieveById(tenantId, inboxId);

        this.transactionTemplate.execute(status -> {
            sqlInboxGroupAssignmentRepository.deleteByGroupIdAndInboxId(inboxGroupId, inboxId);
            return null;
        });

        return inbox;
    }

}

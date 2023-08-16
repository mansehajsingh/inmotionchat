package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditLog;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.dto.InboxGroupAssignmentsDTO;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.data.postgres.inbox.InboxGroupAssignment;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.inboxes.inbox.InboxService;
import com.inmotionchat.inboxes.inboxgroupassignment.SQLInboxGroupAssignmentRepository;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Service
public class InboxGroupServiceImpl extends AbstractDomainService<InboxGroup, InboxGroupDTO> implements InboxGroupService {

    protected static Logger log = LoggerFactory.getLogger(InboxGroupService.class);

    protected final SQLInboxGroupAssignmentRepository sqlInboxGroupAssignmentRepository;

    protected final InboxService inboxService;

    @Autowired
    protected InboxGroupServiceImpl(SQLInboxGroupRepository sqlInboxGroupRepository,
                                    SQLInboxGroupAssignmentRepository sqlInboxGroupAssignmentRepository,
                                    InboxService inboxService,
                                    PlatformTransactionManager transactionManager,
                                    AuditManager auditManager,
                                    InboxGroupAuditActionProvider auditActionProvider,
                                    IdentityContext identityContext) {
        super(InboxGroup.class, InboxGroupDTO.class, log, transactionManager, identityContext, sqlInboxGroupRepository, auditManager, auditActionProvider);
        this.sqlInboxGroupAssignmentRepository = sqlInboxGroupAssignmentRepository;
        this.inboxService = inboxService;
    }

    @Override
    public List<InboxGroupAssignment> assignInboxes(Long tenantId, Long inboxGroupId, InboxGroupAssignmentsDTO dto) throws NotFoundException, DomainInvalidException, ConflictException, UnauthorizedException {
        InboxGroup inboxGroup = retrieveById(tenantId, inboxGroupId);

        List<InboxGroupAssignment> groupAssignments = dto.inboxes().stream().map(id -> {
            Inbox inbox = new Inbox();
            inbox.setId(id);

            return new InboxGroupAssignment(inbox, inboxGroup);
        }).toList();

        return this.transactionTemplate.execute(status -> {
            List<InboxGroupAssignment> createdAssignments = this.sqlInboxGroupAssignmentRepository.storeAll(groupAssignments);

            this.auditManager.save(new AuditLog(
                    AuditAction.ASSIGN_INBOXES,
                    tenantId,
                    identityContext.getRequester().userId(),
                    inboxGroup,
                    Map.of("inboxIds", dto.inboxes().toArray())
            ));
            return createdAssignments;
        });
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
    public Inbox removeInbox(Long tenantId, Long inboxGroupId, Long inboxId) throws NotFoundException, ConflictException, DomainInvalidException, UnauthorizedException {
        Inbox inbox = this.inboxService.retrieveById(tenantId, inboxId);
        InboxGroup inboxGroup = retrieveById(tenantId, inboxGroupId);

        this.transactionTemplate.execute(status -> {
            InboxGroupAssignment assignment = sqlInboxGroupAssignmentRepository.findByGroupIdAndInboxId(inboxGroupId, inboxId).orElseThrow(
                    () -> new NotFoundException("This inbox does not exist under this inbox group."));
            sqlInboxGroupAssignmentRepository.deleteById(assignment.getId());
            this.auditManager.save(new AuditLog(
                    AuditAction.REMOVE_INBOX_ASSIGNMENT,
                    tenantId,
                    identityContext.getRequester().userId(),
                    inboxGroup,
                    Map.ofEntries(
                            Map.entry("inboxGroupAssignmentId", assignment.getId()),
                            Map.entry("inboxId", inboxId)
                    )
            ));
            return null;
        });

        return inbox;
    }

}

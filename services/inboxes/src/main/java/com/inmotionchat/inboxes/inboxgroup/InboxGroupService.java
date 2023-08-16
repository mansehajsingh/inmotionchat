package com.inmotionchat.inboxes.inboxgroup;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.InboxGroupAssignmentsDTO;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.inbox.Inbox;
import com.inmotionchat.core.data.postgres.inbox.InboxGroup;
import com.inmotionchat.core.data.postgres.inbox.InboxGroupAssignment;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;

import java.util.List;

public interface InboxGroupService extends DomainService<InboxGroup, InboxGroupDTO> {

    List<InboxGroupAssignment> assignInboxes(Long tenantId, Long inboxGroupId, InboxGroupAssignmentsDTO dto) throws NotFoundException, ConflictException, DomainInvalidException, UnauthorizedException;

    List<Inbox> retrieveInboxes(Long tenantId, Long inboxGroupId) throws NotFoundException;

    Inbox removeInbox(Long tenantId, Long inboxGroupId, Long inboxId) throws NotFoundException, ConflictException, DomainInvalidException, UnauthorizedException;

}

package com.inmotionchat.core.data.postgres.inbox;

import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.junit.Test;

public class InboxGroupTests {

    private static final String validName = "Ibx Group";

    @Test
    public void validateInboxGroup_Success() throws DomainInvalidException {
        InboxGroupDTO inboxGroupDTO = new InboxGroupDTO(validName);
        InboxGroup group = new InboxGroup(5L, inboxGroupDTO);
        group.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateInboxGroup_NullName() throws DomainInvalidException {
        InboxGroupDTO inboxGroupDTO = new InboxGroupDTO(null);
        InboxGroup group = new InboxGroup(5L, inboxGroupDTO);
        group.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateInboxGroup_EmptyName() throws DomainInvalidException {
        InboxGroupDTO inboxGroupDTO = new InboxGroupDTO("");
        InboxGroup group = new InboxGroup(5L, inboxGroupDTO);
        group.validate();
    }

}

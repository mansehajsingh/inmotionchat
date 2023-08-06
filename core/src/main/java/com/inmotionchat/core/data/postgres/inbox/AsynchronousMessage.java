package com.inmotionchat.core.data.postgres.inbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.AsyncMessageDTO;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.misc.RegExpPatterns;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asynchronous_messages", schema = Schema.InboxManagement)
public class AsynchronousMessage extends AbstractDomain<AsynchronousMessage> {

    private String title;

    private String replyTo;

    private String content;

    @ManyToOne
    @JsonIgnore
    private InboxGroup inboxGroup;

    @ManyToOne
    @JsonIgnore
    private Tenant tenant;

    public AsynchronousMessage() {}

    public AsynchronousMessage(InboxGroup inboxGroup, String title, String replyTo, String content) {
        this.tenant = inboxGroup.getTenant();
        this.inboxGroup = inboxGroup;
        this.title = title;
        this.replyTo = replyTo;
        this.content = content;
    }

    public AsynchronousMessage(Long tenantId, AsyncMessageDTO proto) {
        this.tenant = new Tenant(tenantId);
        this.inboxGroup = AbstractDomain.forId(InboxGroup.class, proto.inboxGroupId());
        this.title = proto.title();
        this.replyTo = proto.replyTo();
        this.content = proto.content();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String description) {
        this.content = description;
    }

    public InboxGroup getInboxGroup() {
        return inboxGroup;
    }

    public void setInboxGroup(InboxGroup inboxGroup) {
        this.inboxGroup = inboxGroup;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> titleRule = StringRule.forField("title").isNotNull().isNotEmpty();
        AbstractRule<String> replyToRule = StringRule.forField("replyTo")
                .isNotNull().isNotEmpty().matches(RegExpPatterns.EMAIL, "not provided with valid email format");
        AbstractRule<String> contentRule = StringRule.forField("content").isNotNull().isNotEmpty().maximumLength(4000);

        if (this.inboxGroup.getId() == null)
            throw new DomainInvalidException(new Violation("inboxGroup", null, "Must provide an inbox group."));

        List<Violation> violations = new ArrayList<>();

        violations.addAll(titleRule.collectViolations(title));
        violations.addAll(replyToRule.collectViolations(replyTo));
        violations.addAll(contentRule.collectViolations(content));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Invitation;
import com.inmotionchat.core.domains.Membership;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.domains.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "invitations",
        schema = Schema.OrganizationManager,
        uniqueConstraints = {
                @UniqueConstraint(name = LogicalConstraints.Invitation.MAXIMUM_ACTIVE_INVITES_REACHED,
                        columnNames = {"invitee_id", "organization_id"})
        }
)
public class SQLInvitation extends AbstractDomain<Invitation> implements Invitation {

    @ManyToOne
    private SQLUser invitee;

    @ManyToOne
    private SQLMembership inviter;

    @ManyToOne
    private SQLOrganization organization;

    public SQLInvitation() {}

    public SQLInvitation(Membership inviter, User invitee, Organization organization) {
        this.setInviter(inviter);
        this.setInvitee(invitee);
        this.setOrganization(organization);
    }

    @Override
    public Membership getInviter() {
        return inviter;
    }

    @Override
    public void setInviter(Membership inviter) {
        this.inviter = SQLMembership.fromId(inviter.getId());
    }

    @Override
    public User getInvitee() {
        return invitee;
    }

    @Override
    public void setInvitee(User invitee) {
        this.invitee = SQLUser.fromId(invitee.getId());
    }

    @Override
    public Organization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(Organization organization) {
        this.organization = SQLOrganization.fromId(organization.getId());
    }

    @Override
    public Invitation copy() {
        SQLInvitation copy = new SQLInvitation();
        copy.setId(this.id);
        copy.setInvitee(this.invitee);
        copy.setInviter(this.inviter);
        copy.setMetadata(this.metadata());
        return copy;
    }

}

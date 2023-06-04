package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.MembershipDTO;
import com.inmotionchat.core.domains.Membership;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.domains.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "memberships",
        schema = Schema.OrganizationManager,
        uniqueConstraints = {
                @UniqueConstraint(name = LogicalConstraints.Membership.ONE_MEMBERSHIP_PER_ORG_AND_USER,
                        columnNames = {"user_id", "organization_id"})
        }
)
public class SQLMembership extends AbstractArchivableDomain<Membership> implements Membership {

    @ManyToOne
    private SQLUser user;

    @ManyToOne
    private SQLOrganization organization;

    public static SQLMembership fromId(Long id) {
        SQLMembership membership = new SQLMembership();
        membership.setId(id);
        return membership;
    }

    public SQLMembership() {}

    public SQLMembership(User user, Organization organization) {
        this.user = SQLUser.fromId(user.getId());
        this.organization = SQLOrganization.fromId(organization.getId());
    }

    public SQLMembership(MembershipDTO prototype) {
        this.user = AbstractDomain.forId(SQLUser.class, prototype.userId());
        this.organization = AbstractDomain.forId(SQLOrganization.class, prototype.organizationId());
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = SQLUser.fromId(user.getId());
    }

    @Override
    public Organization getOrganization() {
        return this.organization;
    }

    @Override
    public void setOrganization(Organization organization) {
        this.organization = SQLOrganization.fromId(organization.getId());
    }

    @Override
    public SQLMembership copy() {
        SQLMembership copy = new SQLMembership();
        copy.setId(this.id);
        copy.setUser(this.user);
        copy.setOrganization(this.organization);
        copy.setMetadata(this.metadata());
        return copy;
    }

}

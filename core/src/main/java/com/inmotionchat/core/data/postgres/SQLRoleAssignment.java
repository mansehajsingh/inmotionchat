package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Membership;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.RoleAssignment;
import jakarta.persistence.*;

@Entity
@Table(
        name = "role_assignments", schema = Schema.OrganizationManager,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = LogicalConstraints.RoleAssignment.ONE_ROLE_ASSIGNMENT_PER_MEMBERSHIP,
                        columnNames = {"role_id", "membership_id"}
                )
        }
)
public class SQLRoleAssignment extends AbstractDomain<RoleAssignment> implements RoleAssignment {

    @OneToOne
    private SQLMembership membership;

    @ManyToOne
    private SQLRole role;

    public SQLRoleAssignment() {}

    public SQLRoleAssignment(Membership membership, Role role) {
        this.membership = AbstractDomain.forId(SQLMembership.class, membership.getId());
        this.role = AbstractDomain.forId(SQLRole.class, role.getId());
    }

    @Override
    public Membership getMembership() {
        return this.membership;
    }

    @Override
    public void setMembership(Membership membership) {
        this.membership = AbstractDomain.forId(SQLMembership.class, membership.getId());
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setRole(Role role) {
        this.role = AbstractDomain.forId(SQLRole.class, role.getId());
    }

    @Override
    public RoleAssignment copy() {
        SQLRoleAssignment copy = new SQLRoleAssignment();
        copy.setId(this.id);
        copy.setMembership(this.membership);
        copy.setRole(this.role);
        copy.setMetadata(this.metadata());
        return copy;
    }

}

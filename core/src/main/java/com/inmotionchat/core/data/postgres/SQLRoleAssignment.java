package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import jakarta.persistence.*;

@Entity
@Table(
        name = "role_assignments",
        schema = Schema.IdentityPlatform,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = LogicalConstraints.RoleAssignment.ONE_ROLE_ASSIGNMENT_PER_USER,
                        columnNames = {"user_id", "tenant_id"}
                )
        }
)
public class SQLRoleAssignment {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private SQLUser user;

    @ManyToOne
    private SQLRole role;

    @ManyToOne
    private SQLTenant tenant;

    public SQLRoleAssignment() {}

    public SQLRoleAssignment(SQLUser user, SQLRole role) {
        this.user = user;
        this.role = role;
        this.tenant = new SQLTenant(role.getTenant().getId());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SQLUser getUser() {
        return this.user;
    }

    public void setUser(SQLUser user) {
        this.user = user;
    }

    public SQLRole getRole() {
        return this.role;
    }

    public void setRole(SQLRole role) {
        this.role = role;
    }

}

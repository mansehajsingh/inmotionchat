package com.inmotionchat.core.data.postgres.identity;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.AbstractEntity;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;

@Entity
@Table(
        name = "role_assignments",
        schema = Schema.IdentityPlatform,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = ConstraintPrefix.UNIQUE + LogicalConstraints.RoleAssignment.ONE_ROLE_ASSIGNMENT_PER_USER,
                        columnNames = {"user_id"}
                )
        }
)
public class RoleAssignment extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "user"))
    private User user;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "role"))
    private Role role;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    private Tenant tenant;

    public RoleAssignment() {}

    public RoleAssignment(User user, Role role) {
        this.user = user;
        this.role = role;
        this.tenant = role.getTenant();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

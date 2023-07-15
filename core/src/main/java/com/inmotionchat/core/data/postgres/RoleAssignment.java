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
                        columnNames = {"user_id"}
                )
        }
)
public class RoleAssignment {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne
    private Role role;

    @ManyToOne
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

package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        schema = Schema.IdentityPlatform,
        uniqueConstraints = {
                @UniqueConstraint(name = LogicalConstraints.User.EMAIL_EXISTS, columnNames = "email"),
                @UniqueConstraint(name = LogicalConstraints.User.UNIQUE_UID, columnNames = "uid")
        }
)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String uid;

    @ManyToOne
    private Tenant tenant;

    private String email;

    private String displayName;

    public User() {}

    public User(String uid, Tenant tenant, String email, String displayName) {
        this.uid = uid;
        this.tenant = tenant;
        this.email = email;
        this.displayName = displayName;
    }

    public Long getId() {
        return this.id;
    }

    public String getUid() {
        return this.uid;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  "User[" +
                "id="           + id                + ", " +
                "uid="          + uid               + ", " +
                "tenant(id)="   + tenant.getId()    + ", " +
                "email="        + email             + ", " +
                "displayName="  + displayName       + "]";
    }

}

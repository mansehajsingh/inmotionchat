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
public class SQLUser {

    @Id
    @GeneratedValue
    private Long id;

    private String uid;

    @ManyToOne
    private SQLTenant tenant;

    private String email;

    private String displayName;

    public SQLUser() {}

    public SQLUser(Long id) {
        this.id = id;
    }

    public SQLUser(String uid, SQLTenant tenant, String email, String displayName) {
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

    public SQLTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(SQLTenant tenant) {
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
        return  "SQLUser[" +
                "id="           + id                + ", " +
                "uid="          + uid               + ", " +
                "tenant(id)="   + tenant.getId()    + ", " +
                "email="        + email             + ", " +
                "displayName="  + displayName       + "]";
    }

}

package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Tenant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenants", schema = Schema.IdentityPlatform)
public class SQLTenant implements Tenant {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public SQLTenant() {}

    public SQLTenant(String name) {
        this.name = name;
    }

    public SQLTenant(Tenant tenant) {
        this.id = tenant.getId();
        this.name = tenant.getName();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}

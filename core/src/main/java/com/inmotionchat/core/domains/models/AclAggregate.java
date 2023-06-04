package com.inmotionchat.core.domains.models;

import com.inmotionchat.core.domains.AclPermission;
import com.inmotionchat.core.domains.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AclAggregate {

    private final Role role;

    private final Map<String, AclPermission> aclsBySimpleName;

    public AclAggregate(Role role) {
        this.role = role;
        this.aclsBySimpleName = new HashMap<>();
    }

    public Role getRole() {
        return this.role;
    }

    protected List<AclPermission> getAcls() {
        return this.aclsBySimpleName.values().stream().toList();
    }

    public void addAcl(AclPermission aclPermission) {
        this.aclsBySimpleName.put(aclPermission.getDomainSimpleName(), aclPermission);
    }

    public boolean observesAclsFor(Class ...domains) {
        for (Class dom : domains) {
            String simpleName = dom.getSimpleName();
            if (this.aclsBySimpleName.containsKey(simpleName))
                continue;
            return false;
        }
        return true;
    }

    public AclPermission getAcl(Class domain) {
        return this.aclsBySimpleName.get(domain.getSimpleName());
    }

    /**
     * Merges new AclAggregate into self, overwriting existing Acls with new aggregate's acls
     * @param other
     */
    public void merge(AclAggregate other) {
        for (AclPermission aclPermission : other.getAcls()) {
            this.aclsBySimpleName.put(aclPermission.getDomainSimpleName(), aclPermission);
        }
    }

}

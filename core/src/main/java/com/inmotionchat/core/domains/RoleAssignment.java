package com.inmotionchat.core.domains;

public interface RoleAssignment extends Domain<RoleAssignment> {

    Membership getMembership();

    void setMembership(Membership membership);

    Role getRole();

    void setRole(Role role);

}

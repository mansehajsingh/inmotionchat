package com.inmotionchat.core.domains;

public interface Membership extends ArchivableDomain<Membership> {

    User getUser();

    void setUser(User user);

    Organization getOrganization();

    void setOrganization(Organization organization);

}

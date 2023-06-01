package com.inmotionchat.core.domains;

public interface Invitation extends Domain<Invitation> {

    Membership getInviter();

    void setInviter(Membership inviter);

    User getInvitee();

    void setInvitee(User invitee);

    Organization getOrganization();

    void setOrganization(Organization organization);

}

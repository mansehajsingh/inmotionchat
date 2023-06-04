package com.inmotionchat.organizations.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.MembershipDTO;
import com.inmotionchat.core.domains.Membership;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface MembershipService extends DomainService<Membership, MembershipDTO> {

    Membership createInitialRoot(MembershipDTO membershipDTO, Role rootRole) throws DomainInvalidException, ConflictException, NotFoundException;

}

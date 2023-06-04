package com.inmotionchat.organizations.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleAssignmentDTO;
import com.inmotionchat.core.domains.RoleAssignment;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;

public interface RoleAssignmentService extends DomainService<RoleAssignment, RoleAssignmentDTO> {

    RoleAssignment assignInitialRoot(RoleAssignmentDTO prototype) throws DomainInvalidException, ConflictException;

}

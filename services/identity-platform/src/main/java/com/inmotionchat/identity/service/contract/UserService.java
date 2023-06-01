package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;

import java.util.UUID;

public interface UserService extends ArchivingDomainService<User, UserDTO> {

    void verify(Long id, UUID verificationCode) throws NotFoundException, UnauthorizedException, ConflictException, DomainInvalidException;

}

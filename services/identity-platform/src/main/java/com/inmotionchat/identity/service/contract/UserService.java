package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.dto.VerifyDTO;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface UserService {

    String createEmailPasswordUser(UserDTO prototype) throws NotFoundException, DomainInvalidException, ConflictException;

    void verifyEmailPasswordUser(String uid, VerifyDTO verifyDTO) throws ConflictException, NotFoundException, DomainInvalidException;

    User retrieveById(Long tenantId, Long id) throws NotFoundException;

}

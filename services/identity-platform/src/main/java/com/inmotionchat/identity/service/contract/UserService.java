package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.dto.VerifyDTO;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface UserService {

    String createEmailPasswordUser(UserDTO prototype) throws NotFoundException, DomainInvalidException, ConflictException, UnauthorizedException;

    void verifyEmailPasswordUser(String uid, VerifyDTO verifyDTO) throws ConflictException, NotFoundException, DomainInvalidException, UnauthorizedException;

    User retrieveById(Long tenantId, Long id) throws NotFoundException;

    Page<User> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters);

}

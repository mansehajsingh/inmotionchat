package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface DomainService<T, DTO> {

    T retrieveById(Long tenantId, Long id) throws NotFoundException;

    Page<? extends T> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters);

    T create(Long tenantId, DTO prototype) throws DomainInvalidException, ConflictException, NotFoundException, ServerException, UnauthorizedException;

    T update(Long tenantId, Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException, UnauthorizedException;

    T delete(Long tenantId, Long id) throws NotFoundException, ConflictException, DomainInvalidException, UnauthorizedException;

}

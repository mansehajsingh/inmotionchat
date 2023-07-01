package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.*;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface DomainService<T, DTO> {

    T retrieveById(Long id) throws NotFoundException;

    Page<? extends T> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters);

    Page<? extends T> search(Long tenantId, Pageable pageable, SearchCriteria<?> ...criteria);

    T create(Long tenantId, DTO prototype) throws DomainInvalidException, ConflictException, NotFoundException, ServerException;

    T update(Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException, UnauthorizedException;

    T delete(Long id) throws NotFoundException, ConflictException;

}

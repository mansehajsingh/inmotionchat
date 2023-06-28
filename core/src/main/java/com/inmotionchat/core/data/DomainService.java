package com.inmotionchat.core.data;

import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface DomainService<T extends Domain<T>, DTO> {

    T retrieveById(Long id) throws NotFoundException;

    Page<? extends T> search(Pageable pageable, MultiValueMap<String, Object> parameters);

    Page<? extends T> search(Pageable pageable, SearchCriteria<?> ...criteria);

    T create(DTO prototype) throws DomainInvalidException, ConflictException, NotFoundException, ServerException;

    T update(Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException;

    T delete(Long id) throws NotFoundException, ConflictException;

}

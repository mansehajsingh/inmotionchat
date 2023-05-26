package com.inmotionchat.core.data;

import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface DomainService<T extends Domain> {

    public T retrieveById(Long id);

    public Page<T> search(MultiValueMap<String, String> parameters, Pageable pageable);

    public T create(T prototype) throws DomainInvalidException;

    public T update(T prototype) throws DomainInvalidException;

    public T delete(Long id) throws DomainInvalidException;

}

package com.inmotionchat.core.web;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractResource<T extends Domain<T>, DTO> {

    public static final String PATH = "/api/v1";

    protected DomainService<T, DTO> domainService;

    protected AbstractResource(DomainService<T, DTO> domainService) {
        this.domainService = domainService;
    }

    @PostMapping
    public IdResponse create(@RequestBody DTO dto) throws ConflictException, DomainInvalidException, NotFoundException {
        return new IdResponse(this.domainService.create(dto).getId());
    }

    @GetMapping("/{id}")
    public T get(@PathVariable Long id) throws NotFoundException {
        return this.domainService.retrieveById(id);
    }

}

package com.inmotionchat.core.data;

import com.inmotionchat.core.domains.ArchivableDomain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface ArchivingDomainService<T extends ArchivableDomain<T>, DTO> extends DomainService<T, DTO> {

    public T restore(Long id) throws NotFoundException, ConflictException;

    public T hardDelete(Long id) throws NotFoundException;

}

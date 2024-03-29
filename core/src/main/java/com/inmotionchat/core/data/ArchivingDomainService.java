package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;

public interface ArchivingDomainService<T extends AbstractArchivableDomain<T>, DTO> extends DomainService<T, DTO> {

    T restore(Long tenantId, Long id) throws NotFoundException, ConflictException;

    T hardDelete(Long tenantId, Long id) throws NotFoundException;

}

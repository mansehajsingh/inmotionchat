package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.models.ArchivalStatus;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;

public abstract class AbstractArchivingDomainService<D extends AbstractArchivableDomain<D>, DTO>
        extends AbstractDomainService<D, DTO> implements ArchivingDomainService<D, DTO> {

    protected SQLArchivingRepository<D> archivingRepository;

    protected AbstractArchivingDomainService(Class<D> type,
                                             Class<DTO> dtoType,
                                             Logger log,
                                             SQLArchivingRepository<D> repository,
                                             SearchCriteriaMapper searchCriteriaMapper) {
        super(type, dtoType, log, repository, searchCriteriaMapper);
        this.archivingRepository = repository;
    }

    @Override
    public D restore(Long tenantId, Long id) throws NotFoundException, ConflictException {
        D domain = this.archivingRepository.findByIdAndTenantId(id, tenantId, ArchivalStatus.NOT_ARCHIVED).orElseThrow(
                () -> new NotFoundException("Could not find a resource with this id that belongs to this tenant."));

        domain.setArchivedAt(null);

        return this.repository.save(domain);
    }

    @Override
    public D hardDelete(Long tenantId, Long id) throws NotFoundException {
        D domain = this.archivingRepository.findByIdAndTenantId(id, tenantId, ArchivalStatus.NOT_ARCHIVED).orElseThrow(
                () -> new NotFoundException("Could not find a resource with this id that belongs to this tenant."));

        this.archivingRepository.deleteById(id);

        return domain;
    }

}

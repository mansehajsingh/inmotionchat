package com.inmotionchat.core.data;

import com.inmotionchat.core.audit.AuditActionProvider;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.models.ArchivalStatus;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractArchivingDomainService<D extends AbstractArchivableDomain<D>, DTO>
        extends AbstractDomainService<D, DTO> implements ArchivingDomainService<D, DTO> {

    protected SQLArchivingRepository<D> archivingRepository;

    protected AbstractArchivingDomainService(Class<D> type,
                                             Class<DTO> dtoType,
                                             Logger log,
                                             PlatformTransactionManager transactionManager,
                                             IdentityContext identityContext,
                                             SQLArchivingRepository<D> repository,
                                             AuditManager auditManager,
                                             AuditActionProvider auditActionProvider,
                                             SearchCriteriaMapper searchCriteriaMapper) {
        super(type, dtoType, log, transactionManager, identityContext, repository, auditManager, auditActionProvider, searchCriteriaMapper);
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

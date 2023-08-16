package com.inmotionchat.core.data;

import com.inmotionchat.core.audit.AuditActionProvider;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.smartpersist.SmartJPARepository;
import com.inmotionchat.smartpersist.Status;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractArchivingDomainService<D extends AbstractArchivableDomain<D>, DTO>
        extends AbstractDomainService<D, DTO> implements ArchivingDomainService<D, DTO> {

    protected SmartJPARepository<D, Long> archivingRepository;

    protected AbstractArchivingDomainService(Class<D> type,
                                             Class<DTO> dtoType,
                                             Logger log,
                                             PlatformTransactionManager transactionManager,
                                             IdentityContext identityContext,
                                             SmartJPARepository<D, Long> repository,
                                             AuditManager auditManager,
                                             AuditActionProvider auditActionProvider) {
        super(type, dtoType, log, transactionManager, identityContext, repository, auditManager, auditActionProvider);
        this.archivingRepository = repository;
    }

    @Override
    public D restore(Long tenantId, Long id) throws NotFoundException {
        D domain = this.archivingRepository.findById(tenantId, id, Status.ARCHIVED).orElseThrow(
                () -> new NotFoundException("Could not find a resource with this id that belongs to this tenant."));

        domain.setArchivedAt(null);

        return this.repository.save(domain);
    }

    @Override
    public D hardDelete(Long tenantId, Long id) throws NotFoundException {
        D domain = this.archivingRepository.findById(tenantId, id, Status.ANY).orElseThrow(
                () -> new NotFoundException("Could not find a resource with this id that belongs to this tenant."));

        this.archivingRepository.delete(domain);

        return domain;
    }

}

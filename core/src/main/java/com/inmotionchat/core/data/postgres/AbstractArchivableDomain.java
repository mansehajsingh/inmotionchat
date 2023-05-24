package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.ArchivableDomain;
import jakarta.persistence.MappedSuperclass;

import java.time.ZonedDateTime;

@MappedSuperclass
public class AbstractArchivableDomain extends AbstractDomain implements ArchivableDomain {

    protected ZonedDateTime archivedAt;

    @Override
    public Boolean isArchived() {
        return this.archivedAt != null;
    }

    @Override
    public ZonedDateTime getArchivedAt() {
        return this.archivedAt;
    }

    @Override
    public void setArchivedAt(ZonedDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

}

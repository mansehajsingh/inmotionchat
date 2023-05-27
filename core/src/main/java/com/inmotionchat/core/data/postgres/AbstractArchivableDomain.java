package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.ArchivableDomain;
import jakarta.persistence.MappedSuperclass;

import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractArchivableDomain<T extends ArchivableDomain<T>> extends AbstractDomain<T> implements ArchivableDomain<T> {

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

package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.domains.ArchivableDomain;
import com.inmotionchat.core.domains.models.Metadata;
import jakarta.persistence.MappedSuperclass;

import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractArchivableDomain<T extends ArchivableDomain<T>> extends AbstractDomain<T> implements ArchivableDomain<T> {

    @JsonIgnore
    protected ZonedDateTime archivedAt;

    protected AbstractArchivableDomain() {}

    protected AbstractArchivableDomain(Metadata metadata, ZonedDateTime archivedAt) {
        super(metadata);
        this.archivedAt = archivedAt;
    }

    @Override
    @JsonIgnore
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

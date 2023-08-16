package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.smartpersist.ArchivalColumn;
import jakarta.persistence.MappedSuperclass;

import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractArchivableDomain<T extends AbstractArchivableDomain<T>> extends AbstractDomain<T> {

    @JsonIgnore
    @ArchivalColumn
    protected ZonedDateTime archivedAt;

    protected AbstractArchivableDomain() {}

    protected AbstractArchivableDomain(ZonedDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    @JsonIgnore
    public Boolean isArchived() {
        return this.archivedAt != null;
    }

    public ZonedDateTime getArchivedAt() {
        return this.archivedAt;
    }

    public void setArchivedAt(ZonedDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

}

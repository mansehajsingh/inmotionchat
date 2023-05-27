package com.inmotionchat.core.domains;

import java.time.ZonedDateTime;

public interface ArchivableDomain<T extends ArchivableDomain<T>> extends Domain<T> {

    Boolean isArchived();

    ZonedDateTime getArchivedAt();

    void setArchivedAt(ZonedDateTime archivedAt);

}

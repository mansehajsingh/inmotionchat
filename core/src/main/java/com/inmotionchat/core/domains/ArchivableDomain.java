package com.inmotionchat.core.domains;

import java.time.ZonedDateTime;

public interface ArchivableDomain extends Domain {

    Boolean isArchived();

    ZonedDateTime getArchivedAt();

    void setArchivedAt(ZonedDateTime archivedAt);

}

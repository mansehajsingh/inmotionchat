package com.inmotionchat.core.domains;

import java.time.ZonedDateTime;

public interface ArchivableDomain extends Domain {

    Boolean isDeleted();

    ZonedDateTime getDeletedAt();

    void setDeletedAt(ZonedDateTime deletedAt);

}

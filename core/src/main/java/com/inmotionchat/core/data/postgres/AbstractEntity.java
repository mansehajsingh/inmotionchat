package com.inmotionchat.core.data.postgres;

import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public class AbstractEntity {

    protected UUID entityUID = UUID.randomUUID();;

    public UUID getEntityUID() {
        return this.entityUID;
    }

}

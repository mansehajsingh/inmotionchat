package com.inmotionchat.core.data;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public class SQLRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    private EntityManager entityManager;

    public SQLRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public void detach(T t) {
        this.entityManager.detach(t);
    }

}

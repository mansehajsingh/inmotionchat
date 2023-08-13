package com.inmotionchat.smartpersist;

import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public class SmartJPARepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements SmartJPARepository<T, ID> {

    protected EntityManager entityManager;

    public SmartJPARepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    private void detach(T t) {
        this.entityManager.detach(t);
    }

    @Override
    public Optional<T> findOne(SmartQuery<T> query) {
        Optional<T> opt = findOne(query.build());
        opt.ifPresent(this::detach);
        return opt;
    }

    @Override
    public Optional<T> findOne(Long tenantId, SmartQuery<T> query) {
        query.parameters.add("tenant", tenantId);
        return findOne(query);
    }

    @Override
    public Page<T> findAll(Pageable pageable, SmartQuery<T> query) {
        return findAll(query.build(), pageable);
    }

    @Override
    public Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query) {
        query.parameters.add("tenant", tenantId);
        return findAll(pageable, query);
    }

    @Override
    public Boolean exists(SmartQuery<T> query) {
        return exists(query.build());
    }

    @Override
    @Transactional
    public T store(T entity) throws ConflictException, NotFoundException {
        try {
            T createdEntity = saveAndFlush(entity);
            detach(createdEntity);
            return createdEntity;
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException constraintViolationException) {
                String constraintName = constraintViolationException.getConstraintName();

                if (constraintName.startsWith(ConstraintPrefix.FKEY)) {
                    throw new NotFoundException(constraintName, "There was a related resource that does not exist.");
                } else if (constraintName.startsWith(ConstraintPrefix.UNIQUE)) {
                    throw new ConflictException(constraintName, "A conflict occurred when trying to create this resource.");
                }
            }

            throw new ConflictException();
        }
    }

    @Override
    @Transactional
    public T update(T updatedEntity) throws NotFoundException, ConflictException {
        return store(updatedEntity);
    }

}

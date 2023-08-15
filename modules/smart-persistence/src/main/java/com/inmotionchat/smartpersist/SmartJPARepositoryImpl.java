package com.inmotionchat.smartpersist;

import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class SmartJPARepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements SmartJPARepository<T, ID> {


    protected static final Logger log = LoggerFactory.getLogger(SmartJPARepositoryImpl.class);

    protected EntityManager entityManager;

    protected JpaEntityInformation<T, ID> jpaEntityInformation;

    public SmartJPARepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.jpaEntityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    private void detach(T t) {
        this.entityManager.detach(t);
    }

    private void detachAll(List<T> ts) {
        for (T t : ts) {
            detach(t);
        }
    }

    private Optional<Field> getArchivalColumnField(Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ArchivalColumn.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    private Specification<T> isArchived(Field archivalColumn) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.notEqual(root.get(archivalColumn.getName()), null);
    }

    private Specification<T> notArchived(Field archivalColumn) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(archivalColumn.getName()), null);
    }

    private void rethrowDataIntegrityException(DataIntegrityViolationException e) throws NotFoundException, ConflictException {
        if (e.getCause() instanceof ConstraintViolationException constraintViolationException) {
            String constraintName = constraintViolationException.getConstraintName();

            if (constraintName.startsWith(ConstraintPrefix.FKEY)) {
                throw new NotFoundException(constraintName, "There was a related resource that does not exist.");
            } else if (constraintName.startsWith(ConstraintPrefix.UNIQUE)) {
                throw new ConflictException(constraintName, "A conflict occurred when trying to save this resource.");
            }
        }
    }

    @Override
    public Optional<T> findOne(SmartQuery<T> query) {
        return findOne(query, Status.ANY);
    }

    @Override
    public Optional<T> findOne(SmartQuery<T> query, Status status) {
        Optional<Field> archivalColumnOpt = getArchivalColumnField(jpaEntityInformation.getJavaType());

        if (archivalColumnOpt.isEmpty() && (status == Status.ARCHIVED || status == Status.NOT_ARCHIVED)) {
            throw new RuntimeException();
        }

        Optional<T> opt = Optional.empty();

        final Field archivalColumn = archivalColumnOpt.orElse(null);

        switch (status) {
            case NOT_ARCHIVED -> {
                opt = findOne(query.build(notArchived(archivalColumn)));
            }
            case ARCHIVED -> {
                opt = findOne(query.build(isArchived(archivalColumn)));
            }
            case ANY -> {
                opt = findOne(query.build());
            }
        }

        opt.ifPresent(this::detach);
        return opt;
    }

    @Override
    public Optional<T> findOne(Long tenantId, SmartQuery<T> query) {
        return findOne(tenantId, query, Status.ANY);
    }

    @Override
    public Optional<T> findOne(Long tenantId, SmartQuery<T> query, Status status) {
        query.parameters.add("tenant", tenantId);
        return findOne(query, status);
    }

    @Override
    public Optional<T> findById(Long tenantId, ID id) {
        return findById(tenantId, id, Status.ANY);
    }

    @Override
    public Optional<T> findById(Long tenantId, ID id, Status status) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("tenant", tenantId);
        params.add("id", id);
        SmartQuery<T> query = new SmartQuery<>(jpaEntityInformation.getJavaType(), params);
        return findOne(query, status);
    }

    @Override
    public Page<T> findAll(Pageable pageable, SmartQuery<T> query) {
        return findAll(pageable, query, Status.ANY);
    }

    @Override
    public Page<T> findAll(Pageable pageable, SmartQuery<T> query, Status status) {
        Optional<Field> archivalColumnOpt = getArchivalColumnField(jpaEntityInformation.getJavaType());

        if (archivalColumnOpt.isEmpty() && (status == Status.ARCHIVED || status == Status.NOT_ARCHIVED)) {
            throw new RuntimeException();
        }

        final Field archivalColumn = archivalColumnOpt.orElse(null);

        return switch (status) {
            case NOT_ARCHIVED -> findAll(query.build(notArchived(archivalColumn)), pageable);

            case ARCHIVED -> findAll(query.build(isArchived(archivalColumn)), pageable);

            case ANY -> findAll(query.build(), pageable);
        };
    }

    @Override
    public Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query) {
        return findAll(pageable, tenantId, query, Status.ANY);
    }

    @Override
    public Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query, Status status) {
        query.parameters.add("tenant", tenantId);
        return findAll(pageable, query, status);
    }

    @Override
    public Boolean exists(SmartQuery<T> query) {
        return exists(query, Status.ANY);
    }

    @Override
    public Boolean exists(SmartQuery<T> query, Status status) {
        Optional<Field> archivalColumnOpt = getArchivalColumnField(jpaEntityInformation.getJavaType());

        if (archivalColumnOpt.isEmpty() && (status == Status.ARCHIVED || status == Status.NOT_ARCHIVED)) {
            throw new RuntimeException();
        }

        final Field archivalColumn = archivalColumnOpt.orElse(null);

        return switch (status) {
            case NOT_ARCHIVED -> exists(query.build(notArchived(archivalColumn)));
            case ARCHIVED -> exists(query.build(isArchived(archivalColumn)));
            case ANY -> exists(query.build());
        };
    }

    @Override
    @Transactional
    public T istore(T entity) throws ConflictException, NotFoundException {
        try {
            T createdEntity = saveAndFlush(entity);
            detach(createdEntity);
            return createdEntity;
        } catch (DataIntegrityViolationException e) {
            rethrowDataIntegrityException(e);
            throw new ConflictException();
        }
    }

    @Override
    @Transactional
    public List<T> istoreAll(List<T> entities) throws ConflictException, NotFoundException {
        try {
            List<T> createdEntities = saveAllAndFlush(entities);
            detachAll(createdEntities);
            return createdEntities;
        } catch (DataIntegrityViolationException e) {
            rethrowDataIntegrityException(e);
            throw new ConflictException();
        }
    }

    @Override
    @Transactional
    public T iupdate(T updatedEntity) throws NotFoundException, ConflictException {
        return istore(updatedEntity);
    }

}

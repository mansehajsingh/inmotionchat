package com.inmotionchat.smartpersist;

import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SmartJPARepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    Optional<T> findOne(SmartQuery<T> query);

    Optional<T> findOne(SmartQuery<T> query, Status status);

    Optional<T> findOne(Long tenantId, SmartQuery<T> query);

    Optional<T> findOne(Long tenantId, SmartQuery<T> query, Status status);

    Optional<T> findById(Long tenantId, ID id);

    Optional<T> findById(Long tenantId, ID id, Status status);

    Page<T> findAll(Pageable pageable, SmartQuery<T> query);

    Page<T> findAll(Pageable pageable, SmartQuery<T> query, Status status);

    Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query);

    Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query, Status status);

    Boolean exists(SmartQuery<T> query);

    Boolean exists(SmartQuery<T> query, Status status);

    T store(T entity) throws NotFoundException, ConflictException;

    List<T> storeAll(List<T> entities) throws NotFoundException, ConflictException;

    T update(T updatedEntity) throws NotFoundException, ConflictException;

}

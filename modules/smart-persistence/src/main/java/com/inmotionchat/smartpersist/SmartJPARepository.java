package com.inmotionchat.smartpersist;

import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SmartJPARepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    Optional<T> findOne(SmartQuery<T> query);

    Optional<T> findOne(Long tenantId, SmartQuery<T> query);

    Page<T> findAll(Pageable pageable, SmartQuery<T> query);

    Page<T> findAll(Pageable pageable, Long tenantId, SmartQuery<T> query);

    Boolean exists(SmartQuery<T> query);

    T store(T entity) throws NotFoundException, ConflictException;

    T update(T updatedEntity) throws NotFoundException, ConflictException;



}

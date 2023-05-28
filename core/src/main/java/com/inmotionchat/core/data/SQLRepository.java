package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.util.query.JPASearchCriteriaParser;
import com.inmotionchat.core.util.query.SearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SQLRepository<T extends AbstractDomain> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    default Optional<T> filterOne(SearchCriteria<?> ...criteria) {
        return findOne((root, criteriaQuery, criteriaBuilder) -> {
            Predicate buildablePredicate = null;

            for (SearchCriteria<?> searchCriteria : criteria) {

                if (buildablePredicate == null) {
                    buildablePredicate = JPASearchCriteriaParser.parse(criteriaBuilder, root, searchCriteria);
                } else {
                    buildablePredicate = criteriaBuilder.and(buildablePredicate, JPASearchCriteriaParser.parse(criteriaBuilder, root, searchCriteria));
                }

            }

            return buildablePredicate;
        });
    }

    default Page<T> filter(Pageable pageable, SearchCriteria<?> ...criteria) {
        return findAll((root, query, criteriaBuilder) -> {
            Predicate buildablePredicate = null;

            for (SearchCriteria<?> searchCriteria : criteria) {

                if (buildablePredicate == null) {
                    buildablePredicate = JPASearchCriteriaParser.parse(criteriaBuilder, root, searchCriteria);
                } else {
                    buildablePredicate = criteriaBuilder.and(buildablePredicate, JPASearchCriteriaParser.parse(criteriaBuilder, root, searchCriteria));
                }

            }

            return buildablePredicate;
        }, pageable);
    }

    default T store(T entity) throws ConflictException {
        try {
            return saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintEx = ((ConstraintViolationException) e.getCause());
            String constraint = constraintEx.getConstraintName();
            throw new ConflictException(constraint, "A conflict occurred when trying to create this resource.");
        }
    }

    default T update(T entity) throws ConflictException {
        try {
            return saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintEx = ((ConstraintViolationException) e.getCause());
            String constraint = constraintEx.getConstraintName();
            throw new ConflictException(constraint, "A conflict occurred when trying to create this resource.");
        }
    }

}

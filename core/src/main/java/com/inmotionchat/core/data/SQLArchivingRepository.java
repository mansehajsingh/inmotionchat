package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.models.ArchivalStatus;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.inmotionchat.core.util.query.NullConstant.NULL;
import static com.inmotionchat.core.util.query.Operation.EQUALS;
import static com.inmotionchat.core.util.query.Operation.NOT_EQUALS;

@NoRepositoryBean
public interface SQLArchivingRepository<T extends AbstractArchivableDomain<T>> extends SQLRepository<T> {

    default SearchCriteria<?>[] copyAndAddCriteria(SearchCriteria<?>[] criteria, SearchCriteria<?> criteriaToAdd) {
        SearchCriteria<?>[] newCriteria = Arrays.copyOf(criteria, criteria.length + 1);
        newCriteria[criteria.length] = criteriaToAdd;
        return newCriteria;
    }

    default Optional<T> findById(Long id, ArchivalStatus archivalStatus) {
        return filterOne(archivalStatus, new SearchCriteria<>("id", EQUALS, id));
    }

    default Optional<T> filterOne(ArchivalStatus archivalStatus, SearchCriteria<?>... criteria) {
        if (archivalStatus == ArchivalStatus.ANY) {
            return SQLRepository.super.filterOne(criteria);
        } else if (archivalStatus == ArchivalStatus.ARCHIVED) {
            return SQLRepository.super.filterOne(
                    copyAndAddCriteria(criteria, new SearchCriteria<>("archivedAt", NOT_EQUALS, NULL)));
        } else {
            return SQLRepository.super.filterOne(copyAndAddCriteria(criteria,
                    new SearchCriteria<>("archivedAt", EQUALS, NULL)));
        }
    }

    @Override
    default List<T> filter(SearchCriteria<?>... criteria) {
        return filter(ArchivalStatus.NOT_ARCHIVED, criteria);
    }

    @Override
    default Page<T> filter(Pageable pageable, SearchCriteria<?>... criteria) {
        return filter(pageable, ArchivalStatus.NOT_ARCHIVED, criteria);
    }

    default Page<T> filter(Pageable pageable, ArchivalStatus archivalStatus, SearchCriteria<?> ...criteria) {
        return switch (archivalStatus) {
            case ANY -> SQLRepository.super.filter(pageable, criteria);
            case ARCHIVED -> SQLRepository.super.filter(pageable,
                    copyAndAddCriteria(criteria, new SearchCriteria<>("archivedAt", NOT_EQUALS, NULL)));
            case NOT_ARCHIVED -> SQLRepository.super.filter(pageable,
                    copyAndAddCriteria(criteria, new SearchCriteria<>("archivedAt", EQUALS, NULL)));
        };
    }

    default List<T> filter(ArchivalStatus archivalStatus, SearchCriteria<?> ...criteria) {
        return switch (archivalStatus) {
            case ANY -> SQLRepository.super.filter(criteria);
            case ARCHIVED -> SQLRepository.super.filter(
                    copyAndAddCriteria(criteria, new SearchCriteria<>("archivedAt", NOT_EQUALS, NULL)));
            case NOT_ARCHIVED -> SQLRepository.super.filter(
                    copyAndAddCriteria(criteria, new SearchCriteria<>("archivedAt", EQUALS, NULL)));
        };
    }

}

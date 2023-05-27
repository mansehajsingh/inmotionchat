package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.domains.models.ArchivalStatus;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Arrays;
import java.util.Optional;

import static com.inmotionchat.core.util.query.NullConstant.NULL;
import static com.inmotionchat.core.util.query.Operation.EQUALS;
import static com.inmotionchat.core.util.query.Operation.NOT_EQUALS;

@NoRepositoryBean
public interface SQLArchivingRepository<T extends AbstractArchivableDomain> extends SQLRepository<T> {

    default Optional<T> findById(Long id, ArchivalStatus archivalStatus) {
        return filterOne(archivalStatus, new SearchCriteria<>("id", EQUALS, id));
    }

    default Optional<T> filterOne(ArchivalStatus archivalStatus, SearchCriteria<?>... criteria) {
        if (archivalStatus == ArchivalStatus.ANY) {
            return SQLRepository.super.filterOne(criteria);
        } else if (archivalStatus == ArchivalStatus.ARCHIVED) {
            SearchCriteria<?>[] extraCriteria = Arrays.copyOf(criteria, criteria.length + 1);
            extraCriteria[criteria.length] = new SearchCriteria<>("archivedAt", NOT_EQUALS, NULL);
            return SQLRepository.super.filterOne(extraCriteria);
        } else {
            SearchCriteria<?>[] extraCriteria = Arrays.copyOf(criteria, criteria.length + 1);
            extraCriteria[criteria.length] = new SearchCriteria<>("archivedAt", EQUALS, NULL);
            return SQLRepository.super.filterOne(extraCriteria);
        }
    }

}

package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import static com.inmotionchat.core.data.SQLRepository.generateSpec;

@Repository
public interface SQLUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    default Page<User> filter(Pageable pageable, SearchCriteria<?> ...searchCriteria) {
        return findAll(generateSpec(searchCriteria), pageable);
    }

}

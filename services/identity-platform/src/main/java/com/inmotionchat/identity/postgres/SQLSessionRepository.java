package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.SQLSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLSessionRepository extends JpaRepository<SQLSession, Long>, JpaSpecificationExecutor<SQLSession> {

    default List<SQLSession> findAllByUser(Long userId) {
        return findAll((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), userId));
    }

}

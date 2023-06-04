package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.SQLPermission;
import com.inmotionchat.core.data.postgres.SQLRole;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SQLPermissionRepository
        extends JpaRepository<SQLPermission, Long>, JpaSpecificationExecutor<SQLPermission> {

    default List<SQLPermission> findAll(Long roleId, String ...domains) {

        if (domains.length == 0)
            return new ArrayList<>();

        return findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate buildablePredicate = null;

           for (String domain : domains) {

               if (buildablePredicate == null) {
                   buildablePredicate = criteriaBuilder.equal(root.get("domain"), domain);
               } else {
                   buildablePredicate = criteriaBuilder.or(
                           buildablePredicate, criteriaBuilder.equal(root.get("domain"), domain));
               }
           }

            return criteriaBuilder.and(buildablePredicate,
                    criteriaBuilder.equal(root.get("role"), AbstractDomain.forId(SQLRole.class, roleId)));
        });
    }

}

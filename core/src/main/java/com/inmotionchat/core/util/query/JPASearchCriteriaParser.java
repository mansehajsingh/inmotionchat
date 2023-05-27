package com.inmotionchat.core.util.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class JPASearchCriteriaParser {

    public static Predicate parse(CriteriaBuilder criteriaBuilder, Root root, SearchCriteria<?> searchCriteria) {

        Object value = searchCriteria.getValue();

        if (value instanceof String) {
            return parseString(criteriaBuilder, root, (SearchCriteria<String>) searchCriteria);
        } else if (value instanceof NullConstant) {
            return parseNullConstraint(criteriaBuilder, root, (SearchCriteria<NullConstant>) searchCriteria);
        }

        throw new UnsupportedOperationException();
    }

    private static Predicate parseString(CriteriaBuilder criteriaBuilder, Root root, SearchCriteria<String> searchCriteria) {
        switch (searchCriteria.getOperation()) {
            case EQUALS:
            case NOT_EQUALS:
                return parseSimple(criteriaBuilder, root, searchCriteria);
            case CONTAINS:
                return criteriaBuilder.like(root.get(searchCriteria.getKey()),
                        "%" + searchCriteria.getValue() + "%");
            case STARTS_WITH:
                return criteriaBuilder.like(root.get(searchCriteria.getKey()), searchCriteria.getValue() + "%");
            case ENDS_WITH:
                return criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue());
        }
        throw new UnsupportedOperationException();
    }

    private static Predicate parseNullConstraint(CriteriaBuilder criteriaBuilder, Root root, SearchCriteria<NullConstant> searchCriteria) {
        if (searchCriteria.getOperation() == Operation.EQUALS) {
            return criteriaBuilder.isNull(root.get(searchCriteria.getKey()));
        } else if (searchCriteria.getOperation() == Operation.NOT_EQUALS) {
            return criteriaBuilder.isNotNull(root.get(searchCriteria.getKey()));
        }
        throw new UnsupportedOperationException();
    }

    private static <T> Predicate parseSimple(CriteriaBuilder criteriaBuilder, Root root, SearchCriteria<T> searchCriteria) {
        switch (searchCriteria.getOperation()) {
            case EQUALS:
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NOT_EQUALS:
                return criteriaBuilder.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
        }
        throw new UnsupportedOperationException();
    }

}

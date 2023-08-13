package com.inmotionchat.smartpersist;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SmartQuery<T> {

    protected Class<T> type;

    protected MultiValueMap<String, Object> parameters;

    public SmartQuery(Class<T> type, MultiValueMap<String, Object> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    private List<Predicate> handleDate(String searchKey, Root<T> root, CriteriaBuilder criteriaBuilder) throws ParseException {
        String suffix = searchKey.endsWith("After") ? "After" : "Before";
        String columnName = searchKey.substring( 0, searchKey.length() - suffix.length() );
        List<Predicate> predicates = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (Object objValue : parameters.get(searchKey)) {
            if (objValue instanceof String value) {

                if (suffix.equals("After")) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(columnName), format.parse(value)));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(columnName), format.parse(value)));
                }
            }
        }

        return predicates;
    }

    private List<Predicate> handleGenericEquals(String searchKey, Root<T> root, CriteriaBuilder criteriaBuilder) throws NoSuchFieldException {
        Field field = type.getDeclaredField(searchKey);
        List<Predicate> predicates = new ArrayList<>();

        for (Object objValue : parameters.get(searchKey)) {
            predicates.add(criteriaBuilder.equal(root.get(searchKey), field.getType().cast(objValue)));
        }

        return predicates;
    }

    private boolean isForeignKey(String columnName) throws NoSuchFieldException {
        Field field = type.getDeclaredField(columnName);
        return field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class);
    }

    private boolean hasField(String field) {
        try {
            type.getDeclaredField(field);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    protected Specification<T> build() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            try {

                for (String searchKey : parameters.keySet()) {

                    if (searchKey.endsWith("After") || searchKey.endsWith("Before")) {
                        Predicate[] predicates = handleDate(searchKey, root, criteriaBuilder).toArray(new Predicate[0]);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predicates));

                    } else if (hasField(searchKey)) {
                        if (isForeignKey(searchKey)) {
                            Long fkeyId = Long.parseLong(parameters.getFirst(searchKey).toString());
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(searchKey), fkeyId));
                        } else {
                            Predicate[] predicates = handleGenericEquals(searchKey, root, criteriaBuilder).toArray(new Predicate[0]);
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predicates));
                        }

                    }

                }

            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

            return predicate;
        };
    }

}

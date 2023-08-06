package com.inmotionchat.core.util.query;

import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class SearchCriteriaMapper {

    private final Map<String, Class<?>> typeForKey;

    public SearchCriteriaMapper() {
        this.typeForKey = new HashMap<>();
    }

    public SearchCriteriaMapper key(String key, Class<?> type) {
        this.typeForKey.put(key, type);
        return this;
    }

    public Set<String> keys() {
        return this.typeForKey.keySet();
    }

    public SearchCriteria<?> map(String key, Object value) {
        if (this.typeForKey.containsKey(key)) {
            Class<?> type = this.typeForKey.get(key);

            if (type.isAssignableFrom(String.class)) {
                Pair<String, Operation> pair = parseOperationAndCleanValue((String) value);
                return new SearchCriteria<>(key, pair.getSecond(), pair.getFirst());
            } else if (Number.class.isAssignableFrom(type)) {
                Pair<Number, Operation> pair = Pair.of(Long.parseLong((String) value), Operation.EQUALS);
                return new SearchCriteria<>(key, pair.getSecond(), pair.getFirst());
            }

            throw new NoSuchElementException();
        }
        throw new NoSuchElementException();
    }

    private static Pair<String, Operation> parseOperationAndCleanValue(String value) {

        if (value.startsWith("!"))
            return Pair.of(value.substring(1), Operation.NOT_EQUALS);

        if (value.startsWith("*") && value.endsWith("*"))
            return Pair.of(value.substring(1, value.length() - 1), Operation.CONTAINS);
        else if (value.startsWith("*"))
            return Pair.of(value.substring(1), Operation.ENDS_WITH);
        else if (value.endsWith("*"))
            return Pair.of(value.substring(0, value.length() - 1), Operation.STARTS_WITH);

        return Pair.of(value, Operation.EQUALS);
    }

}

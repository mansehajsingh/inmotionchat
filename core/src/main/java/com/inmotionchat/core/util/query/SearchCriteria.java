package com.inmotionchat.core.util.query;

public class SearchCriteria<T> {

    private final String key;

    private final Operation operation;

    private final T value;

    public SearchCriteria(String key, Operation operation, T value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public T getValue() {
        return this.value;
    }

}

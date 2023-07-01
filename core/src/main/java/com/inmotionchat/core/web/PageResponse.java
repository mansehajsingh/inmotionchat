package com.inmotionchat.core.web;

import java.util.List;

public class PageResponse<T> {

    private long totalNumberOfElements;

    private int totalNumberOfPages;

    private List<T> elements;

    public PageResponse(long totalNumberOfElements, int totalNumberOfPages, List<T> elements) {
        this.totalNumberOfElements = totalNumberOfElements;
        this.totalNumberOfPages = totalNumberOfPages;
        this.elements = elements;
    }

    public long getTotalNumberOfElements() {
        return this.totalNumberOfElements;
    }

    public int getTotalNumberOfPages() {
        return this.totalNumberOfPages;
    }

    public List<T> getElements() {
        return this.elements;
    }

}

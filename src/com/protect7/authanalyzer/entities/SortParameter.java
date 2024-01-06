package com.protect7.authanalyzer.entities;

public class SortParameter {
    private SortField sortField;
    private SortDirection SortDirection;

    public SortParameter() {
    }

    public SortParameter(SortField sortField, SortDirection SortDirection) {
        this.sortField = sortField;
        this.SortDirection = SortDirection;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public SortDirection getSortDirection() {
        return SortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.SortDirection = sortDirection;
    }
}

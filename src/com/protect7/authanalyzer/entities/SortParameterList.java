package com.protect7.authanalyzer.entities;

import java.util.List;

public class SortParameterList {
    private List<SortField> sortFieldList;
    private List<Boolean> sortDestinationList;

    public SortParameterList(List<SortField> sortFieldList, List<Boolean> sortDestinationList) {
        this.sortFieldList = sortFieldList;
        this.sortDestinationList = sortDestinationList;
    }

    public List<SortField> getSortFieldList() {
        return sortFieldList;
    }

    public void setSortFieldList(List<SortField> sortFieldList) {
        this.sortFieldList = sortFieldList;
    }

    public List<Boolean> getSortDestinationList() {
        return sortDestinationList;
    }

    public void setSortDestinationList(List<Boolean> sortDestinationList) {
        this.sortDestinationList = sortDestinationList;
    }
}

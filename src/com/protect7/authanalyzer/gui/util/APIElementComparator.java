package com.protect7.authanalyzer.gui.util;

import com.protect7.authanalyzer.entities.SortDirection;
import com.protect7.authanalyzer.entities.SortField;
import com.protect7.authanalyzer.entities.SortParameter;

import java.util.Comparator;
import java.util.List;

public class APIElementComparator implements Comparator<APIElement> {
    List<SortParameter> sortParameterList;

    public APIElementComparator(List<SortParameter> sortParameterList) {
        this.sortParameterList = sortParameterList;
    }

    @Override
    public int compare(APIElement o1, APIElement o2) {
        for (SortParameter sortParameter : sortParameterList) {
            int comparison;
            SortField sortField = sortParameter.getSortField();
            SortDirection sortDirection = sortParameter.getSortDirection();
            switch (sortField) {
                case METHOD:
                    comparison = sortDirection.equals(SortDirection.ASCENDING) ?
                            o1.getMethod().compareTo(o2.getMethod()) :
                            o2.getMethod().compareTo(o1.getMethod());
                    break;
                case PATH:
                    comparison = sortDirection.equals(SortDirection.ASCENDING) ?
                            o1.getPath().compareTo(o2.getPath()) :
                            o2.getPath().compareTo(o1.getPath());
                    break;
                case QUERY_PARAMS:
                    comparison = sortDirection.equals(SortDirection.ASCENDING) ?
                            o1.getQueryParams().compareTo(o2.getQueryParams()) :
                            o2.getQueryParams().compareTo(o1.getQueryParams());
                    break;
                case HTTP_VERSION:
                    comparison = sortDirection.equals(SortDirection.ASCENDING) ?
                            o1.getHttpVersion().compareTo(o2.getHttpVersion()) :
                            o2.getHttpVersion().compareTo(o1.getHttpVersion());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown sort field: " + sortParameter.getSortField());
            }
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0; // 如果所有比较都相同，则返回0
    }

}

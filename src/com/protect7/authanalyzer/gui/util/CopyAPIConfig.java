package com.protect7.authanalyzer.gui.util;

import com.protect7.authanalyzer.entities.*;

import java.util.List;

public class CopyAPIConfig {
    List<API_ELEMENT> apiElementList;
    String separator;
    DeduplicationOperation deduplicationOperation;
    List<API_ELEMENT> duplicationAPIELEMENTList;
    DestinationInfo copyToDestinationInfo;
    List<SortParameter> sortParameterList;

    public CopyAPIConfig() {

    }

    public List<API_ELEMENT> getApiElementList() {
        return apiElementList;
    }

    public void setApiElementList(List<API_ELEMENT> apiELEMENTList) {
        this.apiElementList = apiELEMENTList;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public List<API_ELEMENT> getDuplicationAPIElementList() {
        return duplicationAPIELEMENTList;
    }

    public void setDuplicationAPIElementList(List<API_ELEMENT> duplicationAPIELEMENTList) {
        this.duplicationAPIELEMENTList = duplicationAPIELEMENTList;
    }

    public DeduplicationOperation getDeduplicationOperation() {
        return deduplicationOperation;
    }

    public void setDeduplicationOperation(DeduplicationOperation deduplicationOperation) {
        this.deduplicationOperation = deduplicationOperation;
    }

    public List<SortParameter> getSortParameterList() {
        return sortParameterList;
    }

    public void setSortParameterList(List<SortParameter> sortParameterList) {
        this.sortParameterList = sortParameterList;
    }

    public DestinationInfo getCopyToDestinationInfo() {
        return copyToDestinationInfo;
    }

    public void setCopyToDestinationInfo(DestinationInfo copyToDestinationInfo) {
        this.copyToDestinationInfo = copyToDestinationInfo;
    }
}

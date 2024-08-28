package com.protect7.authanalyzer.entities;

public class DestinationInfo {
    CopyDestination copyDestination;
    String filePath;

    public DestinationInfo(){

    }

    public DestinationInfo(CopyDestination copyDestination, String filePath) {
        this.copyDestination = copyDestination;
        this.filePath = filePath;
    }

    public CopyDestination getCopyDestination() {
        return copyDestination;
    }

    public void setCopyDestination(CopyDestination copyDestination) {
        this.copyDestination = copyDestination;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

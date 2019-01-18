package com.duiya.model;

import java.io.Serializable;

public class Picture implements Serializable {
    private String fileName;

    private String fileOwner;

    private String fileState;

    private String fileFserver;

    private Long fileTime;

    @Override
    public String toString() {
        return "Picture{" +
                "fileName='" + fileName + '\'' +
                ", fileOwner='" + fileOwner + '\'' +
                ", fileState='" + fileState + '\'' +
                ", fileFserver='" + fileFserver + '\'' +
                ", fileTime=" + fileTime +
                '}';
    }

    public Picture(String fileName, String fileOwner, String fileState, String fileFserver, Long fileTime) {
        this.fileName = fileName;
        this.fileOwner = fileOwner;
        this.fileState = fileState;
        this.fileFserver = fileFserver;
        this.fileTime = fileTime;
    }

    public Picture() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(String fileOwner) {
        this.fileOwner = fileOwner;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getFileFserver() {
        return fileFserver;
    }

    public void setFileFserver(String fileFserver) {
        this.fileFserver = fileFserver;
    }

    public Long getFileTime() {
        return fileTime;
    }

    public void setFileTime(Long fileTime) {
        this.fileTime = fileTime;
    }
}

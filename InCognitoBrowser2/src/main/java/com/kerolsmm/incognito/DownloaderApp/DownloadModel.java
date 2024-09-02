package com.kerolsmm.incognito.DownloaderApp;

public class DownloadModel {


    public String mimeType;
    public String url;
    public  String attachment;

    public String fileName;

    public DownloadModel () {


    }

    public DownloadModel(String mimeType , String url , String attachment , String fileName) {
        this.attachment = attachment;
        this.mimeType = mimeType;
        this.url = url;
        this.fileName  = fileName;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }
}

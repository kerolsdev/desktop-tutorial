package com.kerolsmm.incognito.DownloaderApp;

public class DownloadItem {


    public int progress;
    public String name;


    public DownloadItem (String name , int progress) {
        this.name = name ;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public String getName() {
        return name;
    }

}

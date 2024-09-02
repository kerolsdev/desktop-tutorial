package com.kerolsmm.incognitopro.DownloaderApp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MvvmDownloader extends ViewModel {

     MutableLiveData<ArrayList<DownloadItem>> mutableLiveData = new MutableLiveData<ArrayList<DownloadItem>>();
     MutableLiveData<DownloadItem> ItemDownloader = new MutableLiveData<DownloadItem>();

    public void setItemDownloader(DownloadItem itemDownloader) {
        ItemDownloader.postValue( itemDownloader );
    }

    public void setMutableLiveData(ArrayList<DownloadItem> arrayList) {
        this.mutableLiveData.postValue(arrayList);
    }

    public MutableLiveData<ArrayList<DownloadItem>> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<DownloadItem> getItemDownloader() {
        return ItemDownloader;
    }



    public void UpdateItem (int Progress) {
        if (mutableLiveData.getValue() != null && mutableLiveData.getValue().size() > 0){
            mutableLiveData.getValue().get(0).progress = Progress;
    }

    }
}

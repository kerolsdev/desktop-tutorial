package com.wifinet.internetcheck.wifiana;

import android.net.wifi.ScanResult;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class liveDataApp  extends ViewModel {


      MutableLiveData<ArrayList<ScanResult>> scanResultMutableLiveData = new MutableLiveData<ArrayList<ScanResult>>();

    public void setScanResultMutableLiveData(ArrayList<ScanResult> scanResultMutableLiveData) {
        this.scanResultMutableLiveData.setValue(scanResultMutableLiveData);
    }

    public MutableLiveData<ArrayList<ScanResult>> getScanResultMutableLiveData() {
        return scanResultMutableLiveData;
    }

}

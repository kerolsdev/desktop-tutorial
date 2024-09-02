package com.wifinet.internetcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;


public class WifiScanReceiverApp extends BroadcastReceiver {


    GetResult getResult;
    WifiManager wifiManager;
    Runnable runnable;
    boolean isRun = false;
    Handler handler = new Handler(Looper.getMainLooper());

    public WifiScanReceiverApp( WifiManager wifiManager , GetResult getResult) {
        this.wifiManager = wifiManager;
        this.getResult = getResult;


    }

    public WifiScanReceiverApp() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        getResult.results((ArrayList<ScanResult>) wifiManager.getScanResults());

    }


    public interface GetResult {
        void results (ArrayList<ScanResult> arrayList);
    }

}

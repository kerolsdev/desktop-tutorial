package com.wifinet.internetcheck;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class StartScanAuto {


    WifiManager wifiManager;
    Context context;
    Runnable runnable;
    boolean isRun = false;
    WifiScanReceiverApp wifiScanReceiverApp;
    Handler handler = new Handler(Looper.getMainLooper());

    public StartScanAuto(Context context) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.context = context;
    }


    public void init(GetResultUi getResult) {

        wifiScanReceiverApp = new WifiScanReceiverApp(wifiManager, new WifiScanReceiverApp.GetResult() {
            @Override
            public void results(ArrayList<ScanResult> arrayList) {
                getResult.results(arrayList);
                startAgain();
            }
        });

    }

    public ArrayList<ScanResult> GetResults() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return new ArrayList<>();
        }

        return (ArrayList<ScanResult>) wifiManager.getScanResults();

    }

    public void startAgain () {
        isRun = true;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (wifiManager != null && isRun) {
                    try {
                        if (context != null && wifiScanReceiverApp != null) context.unregisterReceiver(wifiScanReceiverApp);
                        if (wifiManager.startScan()) {
                            if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        } else {
                            if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                            if (wifiManager != null)  wifiManager.startScan();
                        }

                    } catch (RuntimeException runtimeException) {
                        Log.e("TAG", "run: ", runtimeException);
                    }
                }
            }
        };

        if (handler != null) handler.postDelayed(runnable , 10000);
    }


    public void onRefresh () {
        try {

        isRun = false;
        if (handler != null && runnable != null) handler.removeCallbacks(runnable);
        if (context != null && wifiScanReceiverApp != null) context.unregisterReceiver(wifiScanReceiverApp);
        if (wifiManager.startScan()) {
            if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        } else {
            if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            if (wifiManager != null)  wifiManager.startScan();
        }

        }catch (RuntimeException runtimeException) {
            Log.e("TAG", "onRefresh: ", runtimeException );
        }
    }


    public void  onResumeScan ( ) {
        try {

            if (wifiManager.startScan()) {
                if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            } else {
                if (context != null && wifiScanReceiverApp != null) context.registerReceiver(wifiScanReceiverApp, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                if (wifiManager != null)  wifiManager.startScan();
            }

        } catch (RuntimeException runtimeException) {
            Log.e("TAG", "onResumeScan: ", runtimeException );

        }
    }
    public void  onPauseScan ( ) {
        try {

        isRun = false;
        if (handler != null && runnable != null) handler.removeCallbacks(runnable);
        if (context != null && wifiScanReceiverApp != null) context.unregisterReceiver(wifiScanReceiverApp);

        }catch (RuntimeException runtimeException) {
            Log.e("TAG", "onPauseScan: ", runtimeException );

        }
    }

    public interface GetResultUi {
        void results (ArrayList<ScanResult> arrayList);
    }

}


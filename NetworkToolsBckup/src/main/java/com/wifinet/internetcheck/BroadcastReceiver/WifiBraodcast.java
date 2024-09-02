package com.wifinet.internetcheck.BroadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.wifinet.internetcheck.Activity.WifiScanActivity;

import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;
import com.wifinet.internetcheck.R;

public class WifiBraodcast extends BroadcastReceiver {
    WifiScanActivity wifiScanActivity;
    public WifiBraodcast(WifiScanActivity wifiScanActivity) {
        this.wifiScanActivity = wifiScanActivity;
    }
    public WifiBraodcast () {
    }
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                 //This implies the WiFi connection is through
                           if( wifiScanActivity.swipeRefreshLayout != null && wifiScanActivity.TextView_State_NetWork != null) {
                               wifiScanActivity.swipeRefreshLayout.setEnabled(true);
                               wifiScanActivity.TextView_State_NetWork.setText(context.getString(R.string.Swipe));
                           }
                } else {

                        if (wifiScanActivity.swipeRefreshLayout != null && wifiScanActivity.TextView_State_NetWork != null) {
                            wifiScanActivity.swipeRefreshLayout.setEnabled(false);
                            wifiScanActivity.TextView_State_NetWork.setText(context.getString(R.string.NO_INTERNET));
                        }
                }
            }
        }


    }




package com.wifinet.internetcheck.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import com.wifinet.internetcheck.Activity.HotspotScanActivity;
import com.wifinet.internetcheck.Activity.WifiScanActivity;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.fragment.HotsoptWifiFragment;
import com.wifinet.internetcheck.fragment.WifiScanFragment;

import java.util.ArrayList;
import java.util.Objects;
public class HotspotBroadcast extends BroadcastReceiver {
    HotspotScanActivity hotspotScanActivity;

    public HotspotBroadcast(HotspotScanActivity hotspotScanActivity) {
        this.hotspotScanActivity = hotspotScanActivity;
    }

    public HotspotBroadcast() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
            // get Wi-Fi Hotspot state here
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            if (WifiManager.WIFI_STATE_ENABLED == state % 10) {
                if (hotspotScanActivity.swipeRefreshLayout_Hotspot != null && hotspotScanActivity.TextView_State_NetWork_Hotspot != null) {
                    hotspotScanActivity.swipeRefreshLayout_Hotspot.setEnabled(true);
                    hotspotScanActivity.TextView_State_NetWork_Hotspot.setText(context.getString(R.string.Swipe)); }
                // Wifi is enabled
            } else {
                if (hotspotScanActivity.swipeRefreshLayout_Hotspot != null && hotspotScanActivity.TextView_State_NetWork_Hotspot != null) {
                    hotspotScanActivity.swipeRefreshLayout_Hotspot.setEnabled(false);
                    hotspotScanActivity.TextView_State_NetWork_Hotspot.setText(context.getString(R.string.NO_INTERNET));
                }
            }
            /* hotspot is Fales */


        }

    }
}
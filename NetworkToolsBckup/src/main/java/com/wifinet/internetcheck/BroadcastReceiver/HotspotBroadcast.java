package com.wifinet.internetcheck.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;


import com.wifinet.internetcheck.Activity.HotspotScanActivity;
import com.wifinet.internetcheck.R;

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
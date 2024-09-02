package com.wifinet.internetcheck.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wifinet.internetcheck.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.HashMap;
import java.util.Objects;

public class WifiAnthylis extends AppCompatActivity {

    WifiManager mWifiManager;
    WifiInfo wifiInfo;
    TextView SpeedView, Bssid, Ssid, Strength;
    private static final String SERIES_KEY = "KEY";
    private long timeStart = 0;
    LinearLayout mChartChannels;
    private boolean isStart = true;
    private Runnable runnable;
    private HashMap<String, XYSeries> series = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_anthylis);
        //wifiScanReceiver = new WifiScanReceiver();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

       /* MobileAds.initialize(this);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/


        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = mWifiManager.getConnectionInfo();


        mChartChannels = (LinearLayout) findViewById(R.id.cinf_channel_chart);
        Strength = (TextView) findViewById(R.id.Strength_Result);
        SpeedView = (TextView) findViewById(R.id.Speed_Result);
        Bssid = (TextView) findViewById(R.id.Bssid_result);
        Ssid = (TextView) findViewById(R.id.Ssd_result);

        runnable = new Runnable() {
            @Override
            public void run() {

                if (isStart) {
                    try {
                        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifiInfo = mWifiManager.getConnectionInfo();
                        Update(wifiInfo);
                    } catch (RuntimeException runtimeException) {
                        onBackPressed();
                    }
                    new Handler().postDelayed(this, 200);
                }
            }
        };


        new Handler().postDelayed(runnable,500);

    }

    @SuppressLint("StringFormatMatches")
    public void Update(WifiInfo wifiInfo) {
        Ssid.setText(wifiInfo.getSSID());
        Bssid.setText(wifiInfo.getBSSID());
        SpeedView.setText(String.format(getString(R.string.Mbps_textView), wifiInfo.getLinkSpeed()));
        Strength.setText(String.format(getString(R.string.dBm_textView), wifiInfo.getRssi()));

        WifiChart wifiChart = new WifiChart();
        wifiChart.init();

        String ssid = wifiInfo.getSSID();
        if(wifiInfo.getBSSID() == null)
        {
            ssid = "EXAMPLE";
        }

        double rssi = wifiInfo.getRssi();
        if(rssi < -100)
            rssi = -100;

        wifiChart.setValues(
                (System.nanoTime() - timeStart) / 1000000000,
                (int) rssi,
                ssid
        );

        mChartChannels.addView(wifiChart.getmChartView(), 0);
      /*  List<ScanResult> wifiScanList = mWifiManager.getScanResults();

        if(wifiScanList == null)
        {
            return;
        }

        String cap = "";
        int freq = 1;

        for (int i = 0; i < wifiScanList.size(); i++)
        {
            if(wifiScanList.get(i).BSSID.equals(wifiInfo.getBSSID()))
            {
                cap = wifiScanList.get(i).capabilities;
                freq = wifiScanList.get(i).frequency;
            }
        }*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e("TAG", "Update: " + wifiInfo.getMacAddress());


    }


    private class WifiChart
    {
        private XYMultipleSeriesDataset mDataset;
        private XYMultipleSeriesRenderer mRenderer;

        private GraphicalView mChartView;

        private final String LABEL_X = getString(R.string.sg_labelx);
        private final String LABEL_Y = getString(R.string.sg_labely);

        public WifiChart()
        {
            mDataset = new XYMultipleSeriesDataset();
            mRenderer = new XYMultipleSeriesRenderer();

            mChartView =  ChartFactory.getLineChartView(getApplicationContext(), mDataset, mRenderer);
        }

        public void init()
        {
            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            mRenderer.setPanEnabled(false, false);
            mRenderer.setYAxisMax(-40);
            mRenderer.setYAxisMin(-100);
            mRenderer.setYLabels(3);
            mRenderer.setYTitle(LABEL_Y);
            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(300);
            mRenderer.setXLabels(15);
            mRenderer.setXTitle(LABEL_X);
            mRenderer.setLabelsTextSize(21f);
            mRenderer.setShowGrid(true);
            mRenderer.setShowLabels(true);
            mRenderer.setShowLegend(false);
            mRenderer.setShowCustomTextGrid(true);
        }

        public void setValues(long time, int dBm, String ssid)
        {
            if(time > 300)
            {
                timeStart = System.nanoTime();
                time = 0;
                series.clear();
            }

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setColor(Color.BLUE);
            renderer.setDisplayBoundingPoints(true);

            renderer.setLineWidth(1);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(1);

            XYSeries xySerieseries;

            if(series.containsKey(SERIES_KEY))
            {
                xySerieseries = series.get(SERIES_KEY);
                xySerieseries.add(time, dBm);
                series.remove(SERIES_KEY);
                series.put(SERIES_KEY, xySerieseries);
            }
            else
            {
                xySerieseries = new XYSeries(ssid);
                xySerieseries.add(time ,dBm);
                series.put(SERIES_KEY, xySerieseries);
            }

            mDataset.addSeries(0, xySerieseries);
            mRenderer.addSeriesRenderer(0, renderer);
        }

        public View getmChartView()
        {
            mChartView =  ChartFactory.getLineChartView(getApplicationContext(), mDataset, mRenderer);
            return mChartView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeStart = System.nanoTime();
       // registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(wifiScanReceiver);
        timeStart = System.nanoTime();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class WifiScanReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {    mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
             wifiInfo = mWifiManager.getConnectionInfo();
             Update(wifiInfo);
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        isStart = false;
        new Handler().removeCallbacks(runnable);
        super.onBackPressed();
    }
}

/*  AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.card_color));
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                        FrameLayout template = (FrameLayout) findViewById(R.id.my_template_info);
                        template.setVisibility(View.VISIBLE);
                        AddViewNativeAdmob addViewNativeAdmob = new AddViewNativeAdmob(WifiAnthylis.this);
                        addViewNativeAdmob.displayNativeAd(template, nativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdManagerAdRequest.Builder().build());*/
/*
package com.wifinet.internetcheck.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.wifinet.internetcheck.Adapter.WpsAdapter;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.StartScanAuto;

import java.util.ArrayList;
import java.util.Objects;

public class WPS extends AppCompatActivity implements WpsAdapter.onClickAdapter {

    private RecyclerView lv;
    private ArrayList<ScanResult> scanResultList = new ArrayList<ScanResult>();
    private WpsAdapter wlanAdapter;
    private StartScanAuto startScanAuto;
    ProgressBar progressBar;
  */
/*  private InterstitialAd mInterstitialAd;*//*





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wps);


        try {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } catch (RuntimeException runtimeException) {

        }






 */
/*       AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*//*


     */
/*   loadAd();*//*



        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);
        scanResultList = new ArrayList<ScanResult>();
        progressBar = findViewById(R.id.ProgressbarWps);
        lv = (RecyclerView) findViewById(R.id.listwps);
        wlanAdapter = new WpsAdapter(this, scanResultList, this);
        lv.setAdapter(wlanAdapter);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            if (!gpsStatus) {
                // notify user
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.gps_network_not_enabled)
                        .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }

        startScanAuto =  new StartScanAuto(this);
        startScanAuto.init(new StartScanAuto.GetResultUi() {
            @Override
            public void results(ArrayList<ScanResult> arrayList) {

             */
/*   if (mInterstitialAd != null) {
                    mInterstitialAd.show(WPS.this);
                }*//*



                if (arrayList.size() > 0 ) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    scanResultList.clear();
                    scanResultList.addAll(arrayList);
                    wlanAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setEnabled(true);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startScanAuto.onRefresh();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScanAuto.onResumeScan();
    }


    @Override
    protected void onPause() {
        super.onPause();
       startScanAuto.onPauseScan();

    }

 */
/*   public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getString(R.string.InterstitialAd),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;

                    }
                });

    }*//*

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void itemViewClick(String mac , String ssid , String wps) {
        if (!mac.isEmpty()) {
            Intent intent = new Intent(WPS.this ,WPS_DATA.class);
            if (ssid.isEmpty()){
                intent.putExtra("ssid","hidden network");

            }else  {
                intent.putExtra("ssid",ssid);

            }
            boolean isWps = wps.contains("WPS") || wps.contains("wps");

            intent.putExtra("wps",isWps);
            intent.putExtra("mac",mac);
            startActivity(intent);
        }

    }

}

*/

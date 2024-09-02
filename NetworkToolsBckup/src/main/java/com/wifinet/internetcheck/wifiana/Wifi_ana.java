package com.wifinet.internetcheck.wifiana;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.StartScanAuto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Wifi_ana extends AppCompatActivity {

    private WifiManager wm;


    ArrayList<ScanResult> scanResultListOrig = new ArrayList<>();
    Timer timer;
    ProgressBar progressBar;
    private liveDataApp mlive  = new liveDataApp();
    private InterstitialAd mInterstitialAd;
    private Handler handler = new Handler(Looper.getMainLooper());
    StartScanAuto startScanAuto;
    SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_ana);

        try {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }catch (RuntimeException runtimeException) {

        }

        loadAd();


         swipeRefreshLayout = findViewById(R.id.swiperefreshAuto);
         swipeRefreshLayout.setEnabled(false);
         progressBar = findViewById(R.id.ProgressbarApp);
         TabLayout tabLayout = findViewById(R.id.tabs);
         ViewPager2 viewPager2 = findViewById(R.id.viewPager);
         ViewStateAdapter viewStateAdapter = new ViewStateAdapter(this);
         viewPager2.setAdapter(viewStateAdapter);

        mlive = new ViewModelProvider(this).get(liveDataApp.class);
        wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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

       new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
           @Override
           public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                 if (position == 0 ) {
                     tab.setText("List");
                 }else if (position == 1 ) {
                     tab.setText("2.4GhZ");

                 }else if (position == 2 ) {
                     tab.setText("5GhZ");
                 }else
                 {
                     tab.setText("6GhZ");
                 }
           }
       }).attach();

        startScanAuto = new StartScanAuto(this);
        startScanAuto.init(new StartScanAuto.GetResultUi() {
            @Override
            public void results(ArrayList<ScanResult> arrayList) {



                if (arrayList.size() > 0 ) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    scanResultListOrig.clear();
                    scanResultListOrig.addAll(arrayList);
                    swipeRefreshLayout.setEnabled(true);
                }

                 mlive.setScanResultMutableLiveData(scanResultListOrig);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (arrayList.size() > 0) {
                    if (mInterstitialAd != null){
                        mInterstitialAd.show(Wifi_ana.this);
                    }
                }
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (scanResultListOrig.size() > 0 && !swipeRefreshLayout.isRefreshing()){
                   toggleRefreshing(state == ViewPager.SCROLL_STATE_IDLE);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startScanAuto.onPauseScan();

    }

    public void loadAd() {
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

    }

    public void toggleRefreshing(boolean enabled) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enabled);
        }
    }

}
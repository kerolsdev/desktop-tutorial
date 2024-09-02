package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.WifiHackerMethod;

import java.util.Objects;

public class ScanWifiHacker extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
  /*   public static InterstitialAd mInterstitialAd;*/
    TextView text_view_single,text_view_connection,text_view_Encrypted;
    View Result_Done;
    TextView text_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_wifi_hacker);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        text_pro = findViewById(R.id.text_pro);

/*
        FrameLayout frameLayout = findViewById(R.id.banner_footer2);
*/



        progressBar = findViewById(R.id.Pro_Loading);
        textView = findViewById(R.id.text_pro_loading);

        progressBar.setProgress(0);
        textView.setText("0 %");
        text_view_connection = findViewById(R.id.Connection_Result);
        text_view_Encrypted = findViewById(R.id.Encrypted_Result);
        text_view_single = findViewById(R.id.Single_Result);
        Result_Done = findViewById(R.id.Result_Done_View);


        WifiHackerMethod wifiHackerMethod = new WifiHackerMethod(progressBar, textView, this);
        //method before Start
        wifiHackerMethod.setText_pro(text_pro);
        wifiHackerMethod.setConnection_view(text_view_connection);
        wifiHackerMethod.setEncrypted_view(text_view_Encrypted);
        wifiHackerMethod.setResult_Done_View(Result_Done);
        wifiHackerMethod.setSingle_view(text_view_single);
        wifiHackerMethod.setScanWifiHacker(this);
        // Start
     /*   AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        FrameLayout template = (FrameLayout)findViewById(R.id.my_template_scan);
                        template.setVisibility(View.VISIBLE);
                        AddViewNativeAdmob addViewNativeAdmob = new AddViewNativeAdmob(ScanWifiHacker.this);
                        addViewNativeAdmob.displayNativeAd(template,nativeAd);
                        template.setVisibility(View.VISIBLE);

                    }
                })
                .build();
        adLoader.loadAd(new AdManagerAdRequest.Builder().build());*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                wifiHackerMethod.StartScan();
            }
        },1000);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
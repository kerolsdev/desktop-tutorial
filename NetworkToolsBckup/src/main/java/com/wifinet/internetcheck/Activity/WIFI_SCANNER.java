package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.WIFI_Scanner_Dialog;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WIFI_SCANNER extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);


/*        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/





        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {

                        try {
                            String ssid = result.getText().split("S:")[1].split(";")[0];
                            String pass = result.getText().split("P:")[1].split(";")[0];
                            String type = result.getText().split("T:")[1].split(";")[0];

                            WIFI_Scanner_Dialog wifi_scanner_dialog = new WIFI_Scanner_Dialog(ssid,pass,type,mCodeScanner);
                            wifi_scanner_dialog.show(getSupportFragmentManager(),wifi_scanner_dialog.getTag());
                            Log.e("TAG", "run: " + result.getText()  );
                        }catch (RuntimeException runtimeException) {
                            Toast.makeText(getApplicationContext(),"Couldn't Scan QR Code",Toast.LENGTH_SHORT).show();
                            mCodeScanner.releaseResources();
                            mCodeScanner.startPreview();
                        }

                    /*        String[] ssidArray = result.getText().split(";S:");
                            int i = 1;
                            Log.e("TAG", "run: " + ssidArray[1].split(";P:")[0] );
                            String ssid = ssidArray[1].split(";P:")[0];
                            String pass = ssidArray[1].split(";P:")[1].split(";H:;")[0];
                            ConnectToNetworkWPA("Mf","Mf*111222333444#");*/

                             /*ConnectToNetworkWPA("Mf","Mf*111222333444#");*/

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
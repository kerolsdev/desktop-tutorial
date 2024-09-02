/*
package com.wifinet.internetcheck.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.dialogs.BottomDialogAuto;
import com.wifinet.internetcheck.dialogs.BottomDialogCustom;
import com.wifinet.internetcheck.wpspin.NetworkAddress;
import com.wifinet.internetcheck.wpspin.WPSpin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WPS_DATA extends AppCompatActivity implements View.OnClickListener {


    private WifiManager wifi ;
    private ArrayList<String> arrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wps_data);


        try {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (RuntimeException runtimeException) {

        }

        MaterialButton btn_pass = findViewById(R.id.btn_password);
        MaterialButton btn_wps = findViewById(R.id.btn_wps);
        MaterialButton btn_wps_auto = findViewById(R.id.btn_wps_auto);
        CardView mac_card = findViewById(R.id.mac_copy_ana);
        CardView ssid_card = findViewById(R.id.ssid_copy_ana);
        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1.add("96813232");
        arrayList = arrayList1;

        if (getIntent().getBooleanExtra("wps",true)){
            btn_wps_auto.setVisibility(View.VISIBLE);
            btn_wps.setVisibility(View.VISIBLE);
        }else  {
            btn_wps_auto.setVisibility(View.GONE);
            btn_wps.setVisibility(View.GONE);
        }

//        getIntent().getStringExtra("mac")

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    arrayList = (ArrayList<String>) new WPSpin().getList(new NetworkAddress(getIntent().getStringExtra("mac")),true);
                }catch (RuntimeException runtimeException) {
                        ArrayList<String> arrayList1 = new ArrayList<String>();
                        arrayList1.add("96813232");
                        arrayList = arrayList1;
                }
            }
        }).start();

        ClipboardManager  clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        TextView mac = findViewById(R.id.text_mac_ana);
        TextView  ssid = findViewById(R.id.text_ssid_ana);
        if (getIntent().getStringExtra("ssid").isEmpty()) {
            ssid.setText(getString(R.string.UnknownAna));
        }else {
            ssid.setText(getIntent().getStringExtra("ssid"));
        }
        mac.setText(getIntent().getStringExtra("mac"));

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mac_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = ClipData.newPlainText("textmac", getIntent().getStringExtra("mac"));
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }

                Toast.makeText(WPS_DATA.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        ssid_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = ClipData.newPlainText("textssid", getIntent().getStringExtra("ssid"));
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }

                    Toast.makeText(WPS_DATA.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });




        btn_pass.setOnClickListener(this);
        btn_wps.setOnClickListener(this);
        btn_wps_auto.setOnClickListener(this);


                }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_wps) {


            if (wifi.isWifiEnabled()) {

                BottomDialogCustom bottomDialogCustom = new BottomDialogCustom(getIntent().getStringExtra("mac"));
                bottomDialogCustom.show(getSupportFragmentManager(),bottomDialogCustom.getTag());

            } else {
                Toast.makeText(this, "WiFi is Disable", Toast.LENGTH_SHORT).show();

            }

        }else if (v.getId()  == R.id.btn_wps_auto) {

            if (wifi.isWifiEnabled()) {
                BottomDialogAuto bottomDialogAuto = new BottomDialogAuto(getIntent().getStringExtra("mac"),arrayList);
                bottomDialogAuto.show(getSupportFragmentManager(),bottomDialogAuto.getTag());

            } else {
                Toast.makeText(this, "WiFi is Disable", Toast.LENGTH_SHORT).show();

            }
        } else  {
             isWifiOpen();
        }
    }

    private void isWifiOpen ( ){
        try {

                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

        } catch (ActivityNotFoundException activityNotFoundException) {

                try {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                }catch (RuntimeException runtimeException) {

                }



        }
    }
}*/

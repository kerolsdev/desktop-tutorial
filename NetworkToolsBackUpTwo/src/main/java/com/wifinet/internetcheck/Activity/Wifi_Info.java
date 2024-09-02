package com.wifinet.internetcheck.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifinet.internetcheck.GetWifiInfo;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.Utils;

import java.util.Objects;

import static com.wifinet.internetcheck.My_DeviceInfo.macAddress;

public class Wifi_Info extends AppCompatActivity implements View.OnClickListener {


    TextView mac_Device , ip_Device;
    TextView mac_Wifi , ip_Wifi,Server_Address_Wifi , Subnet_Mask , Host_ip_Address ,  Dns_1_wifi , Dns_2_wifi,Location;
    ImageView mac_device_Copy , mac_wifi_Copy , Server_Address_Wifi_Copy , Subnet_Mask_Copy , Location_Copy ,Host_ip_Address_Copy , Dns_1_wifi_Copy , Dns_2_wifi_Copy , ip_Device_Copy ,ip_Wifi_Copy ;
    ClipboardManager clipboard;
    private ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi__info);
        Utils utils = new Utils(this);
       /* MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.card_color));
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                        FrameLayout template = (FrameLayout) findViewById(R.id.my_template_info);
                        template.setVisibility(View.VISIBLE);
                        AddViewNativeAdmob addViewNativeAdmob = new AddViewNativeAdmob(Wifi_Info.this);
                        addViewNativeAdmob.displayNativeAd(template,nativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdManagerAdRequest.Builder().build());*/


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

        clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);

        WifiManager manager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        GetWifiInfo getWifiInfo = new GetWifiInfo(this);

        mac_Device = findViewById(R.id.mac_Device);
        ip_Device = findViewById(R.id.ip_Device);
        Location_Copy = findViewById(R.id.Location_Copy);

        mac_Device.setText(macAddress(new StringBuilder()));
        ip_Device.setText(getWifiInfo.intToIp(manager.getConnectionInfo().getIpAddress()));

        mac_Wifi = findViewById(R.id.mac_Wifi);
        ip_Wifi = findViewById(R.id.ip_Wifi);
        Server_Address_Wifi = findViewById(R.id.Server_Wifi);
        Subnet_Mask  = findViewById(R.id.Subnet_Mask_Wifi);
        Host_ip_Address = findViewById(R.id.Host_ip_Wifi);
        Location = findViewById(R.id.Location);
        Dns_1_wifi = findViewById(R.id.Dns1_Wifi);
        Dns_2_wifi = findViewById(R.id.Dns2_Wifi);
        if (getWifiInfo.getMacAddressWifi() != null) {
            if (!getWifiInfo.getMacAddressWifi().toString().isEmpty()) {
                mac_Wifi.setText(getWifiInfo.getMacAddressWifi());
            }else  {
                mac_Wifi.setText("00:00:00");

            }

        }else  {
            mac_Wifi.setText("00:00:00");

        }
        Dns_1_wifi.setText(getWifiInfo.getDns1());
        Dns_2_wifi.setText(getWifiInfo.getDns2());
        ip_Wifi.setText(getWifiInfo.getIpAddressWifi());
        Subnet_Mask.setText(getWifiInfo.getNetMask());
        Server_Address_Wifi.setText(getWifiInfo.getServerAddress());
        getWifiInfo.publicIpAddress(Host_ip_Address);
        getWifiInfo.getLocation(Location);

        mac_device_Copy = findViewById(R.id.Copy_mac_Device);
        mac_wifi_Copy = findViewById(R.id.Copy_mac_Wifi);
        ip_Wifi_Copy = findViewById(R.id.Copy_Ip_Wifi);
        ip_Device_Copy = findViewById(R.id.Copy_Ip_Device);
        Dns_1_wifi_Copy = findViewById(R.id.Copy_Dns1_Wifi);
        Dns_2_wifi_Copy = findViewById(R.id.Copy_Dns2_Wifi);
        Subnet_Mask_Copy = findViewById(R.id.Copy_Subnet_Mask_Wifi);
        Server_Address_Wifi_Copy = findViewById(R.id.Copy_Server_Wifi);
        Host_ip_Address_Copy = findViewById(R.id.Copy_Host_ip_Wifi);

        mac_device_Copy.setOnClickListener(this);
        mac_wifi_Copy.setOnClickListener(this);
        ip_Wifi_Copy.setOnClickListener(this);
        ip_Device_Copy.setOnClickListener(this);
        Dns_1_wifi_Copy.setOnClickListener(this);
        Dns_2_wifi_Copy.setOnClickListener(this);
        Subnet_Mask_Copy.setOnClickListener(this);
        Server_Address_Wifi_Copy.setOnClickListener(this);
        Host_ip_Address_Copy.setOnClickListener(this);
        Location_Copy.setOnClickListener(this);
        utils.SaveValue();



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.Copy_mac_Device) {
            clipData = ClipData.newPlainText("text", mac_Device.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + mac_Device.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_mac_Wifi) {
            clipData = ClipData.newPlainText("text", mac_Wifi.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + mac_Wifi.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Ip_Wifi) {
            clipData = ClipData.newPlainText("text", ip_Wifi.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + ip_Wifi.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Ip_Device) {
            clipData = ClipData.newPlainText("text", ip_Device.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + ip_Device.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Dns1_Wifi) {
            clipData = ClipData.newPlainText("text", Dns_1_wifi.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Dns_1_wifi.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Dns2_Wifi) {
            clipData = ClipData.newPlainText("text", Dns_2_wifi.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Dns_2_wifi.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Subnet_Mask_Wifi) {
            clipData = ClipData.newPlainText("text", Subnet_Mask.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Subnet_Mask.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Server_Wifi) {
            clipData = ClipData.newPlainText("text", Server_Address_Wifi.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Server_Address_Wifi.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Copy_Host_ip_Wifi) {
            clipData = ClipData.newPlainText("text", Host_ip_Address.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Host_ip_Address.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Location_Copy) {
            clipData = ClipData.newPlainText("text", Location.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(), "Copied " + Location.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (interstitialAd != null){
            interstitialAd.destroy();
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


}


package com.wifinet.internetcheck;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.budiyev.android.codescanner.CodeScanner;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.wifinet.internetcheck.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WIFI_Scanner_Dialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private String SSID;
    private String Pass;
    private String SecurityWifi;
    private ClipboardManager clipboard;
    private ClipData clipData;
    private CodeScanner codeScanner;

    public  WIFI_Scanner_Dialog (String SSID, String pass, String securityWifi , CodeScanner codeScanner) {

        this.Pass = pass;
        this.SecurityWifi = securityWifi;
        this.SSID = SSID;
        this.codeScanner = codeScanner;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wifi_scan_qr_bottom,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        TextView Text_SSid  = view.findViewById(R.id.ssid);
        TextView Text_pass = view.findViewById(R.id.pass);
        TextView  Text_wps = view.findViewById(R.id.security);
        MaterialButton btn_connect = view.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);

        MaterialButton btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        CardView copy_one = view.findViewById(R.id.SsId_copy);
        CardView copy_two = view.findViewById(R.id.PASS_copy);
        CardView copy_three = view.findViewById(R.id.wps_copy);

        copy_one.setOnClickListener(this);
        copy_two.setOnClickListener(this);
        copy_three.setOnClickListener(this);

        Text_pass.setText(Pass);
        Text_wps.setText(SecurityWifi);
        Text_SSid.setText(SSID);



    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.SsId_copy ) {

            clipData = ClipData.newPlainText("text", SSID);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.PASS_copy ) {


            clipData = ClipData.newPlainText("text", Pass);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.wps_copy ) {


            clipData = ClipData.newPlainText("text", SecurityWifi);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }

            Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.btn_connect) {

            Toast.makeText(getActivity(), "Copied password", Toast.LENGTH_SHORT).show();


            try {
                clipData = ClipData.newPlainText("text", Pass);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ConnectToNetworkWPA(SSID, Pass);
                }

                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } catch (ActivityNotFoundException activityNotFoundException) {
                try {
                    clipData = ClipData.newPlainText("text", Pass);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clipData);
                    }
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (RuntimeException ignored) {

                }

            }
        }
        if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean ConnectToNetworkWPA(String networkSSID, String password) {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes

            conf.preSharedKey = "\"" + password + "\"";

            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.d("connecting", conf.SSID + " " + conf.preSharedKey);

            WifiManager wifiManager = (WifiManager) requireActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);

            Log.d("after connecting", conf.SSID + " " + conf.preSharedKey);


            @SuppressLint("MissingPermission") List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.d("re connecting", i.SSID + " " + conf.preSharedKey);

                    break;
                }
            }

            //WiFi Connection success, return true
            return true;
        } catch (RuntimeException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        codeScanner.releaseResources();
        codeScanner.startPreview();
    }
}

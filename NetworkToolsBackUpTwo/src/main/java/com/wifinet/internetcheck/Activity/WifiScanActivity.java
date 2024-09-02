package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.RuntimeExecutionException;


import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import com.wifinet.internetcheck.Adapter.DeviceWifiAdapter;
import com.wifinet.internetcheck.BroadcastReceiver.WifiBraodcast;
import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;

import com.wifinet.internetcheck.Model.ModelAdapterDevice;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.wifinet.internetcheck.DeleteCacheClass.deleteCache;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.MY_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.OTHER_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.ROUTER;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.WIFI;
import static com.wifinet.internetcheck.My_DeviceInfo.modelAdapterDevice;

public class WifiScanActivity extends AppCompatActivity {
    private static final String baseURL = "https://api.macvendors.com/";
    WifiBraodcast wifiBraodcast;
    IntentFilter intentFilter;
    private View Layout_loading;
    private RecyclerView recyclerView;
    private ArrayList<ModelAdapterDevice> arrayList;
    private ArrayList<String> macVendor;
    private SubnetDevices subnetDevices;
    private TextView num_device;
    TrustManager[] trustAllCerts;
    private View layout_Devices_list;
    public SwipeRefreshLayout swipeRefreshLayout;
    public TextView TextView_State_NetWork;
    private InterstitialAd mInterstitialAd;
    SSLContext sc;
    Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        utils = new Utils(this);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if found Wifi */
                if (ConnectionDetector.isNetWorkWifiOn(getApplicationContext())) {
/*
                    loadAd();
*/
                    SearchDevice();
                    swipeRefreshLayout.setRefreshing(false);
                } else {

                    Toast.makeText(getApplicationContext(), getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });




        /* Admob ads Banner*/
 /*       MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        /*Admob Ads InterstitialAd */

        layout_Devices_list = findViewById(R.id.layout_Devices_List);
        Layout_loading = findViewById(R.id.loading_layout);
        recyclerView = findViewById(R.id.Device_List);
        num_device = findViewById(R.id.num_device);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        TextView_State_NetWork = findViewById(R.id.text_state_Network);
        arrayList = new ArrayList<>();
        macVendor = new ArrayList<>();

        // Broadcast Preparation
        wifiBraodcast = new WifiBraodcast(WifiScanActivity.this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

       /*  trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };*/

// Install the all-trusting trust manager


// Create all-trusting host name verifier
     /*   HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("something.com", session);
            }
        };*/
        loadAd();
        SearchDevice();


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiBraodcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiBraodcast);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (arrayList.size() > 0) {
            arrayList.clear();
            macVendor.clear();
        }
        if (subnetDevices != null) {
            subnetDevices.cancel();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    private void SearchDevice() {


        try {
            // clear for Array for new Start
            arrayList.clear();
            macVendor.clear();
            //cancel  for SubnetDevice new Start
            if (subnetDevices != null) {
                subnetDevices.cancel();
            }

            Layout_loading.setVisibility(View.VISIBLE);
            layout_Devices_list.setVisibility(View.GONE);
            //
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            final DhcpInfo dhcp = wm.getDhcpInfo();
            final String address = Formatter.formatIpAddress(dhcp.gateway);
            subnetDevices = SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
                @Override
                public void onDeviceFound(Device device) {

                    // Stub: Found subnet device

                }

                @Override
                public void onFinished(ArrayList<Device> devicesFound) {
                    //  getMac(devicesFound.get(0).ip,getApplicationContext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            utils.SaveValue();
                        /*    if (mInterstitialAd != null) {
                                mInterstitialAd.show(WifiScanActivity.this);
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }*/

                        }
                    });
                    if (devicesFound.size() > 0) {
                        for (int i = 0; i < devicesFound.size(); i++) {
                            try {
                                /*Get Vendor name for device*/
                                StringBuilder result = new StringBuilder();
                                URL url = new URL(baseURL + devicesFound.get(i).mac);
                                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
                                conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
                                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String line = "";
                                while ((line = rd.readLine()) != null) {
                                    result.append(line);
                                }
                                rd.close();
                                macVendor.add(result.toString());
                            } catch (FileNotFoundException e) {
                                try {
                                    /*Get Vendor name for device*/
                                    StringBuilder result = new StringBuilder();
                                    URL url = new URL(baseURL + devicesFound.get(i).mac);
                                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
                                    conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String line = "";
                                    while ((line = rd.readLine()) != null) {
                                        result.append(line);
                                    }
                                    rd.close();
                                    macVendor.add(result.toString());
                                } catch (FileNotFoundException fileNotFoundException) {
                                    macVendor.add("N/A");

                                } catch (ProtocolException protocolException) {
                                    protocolException.printStackTrace();
                                } catch (MalformedURLException malformedURLException) {
                                    malformedURLException.printStackTrace();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                Log.d("WifiSacnActivity", "failed for found Vendor name");
                            } catch (MalformedURLException malformedURLException) {
                                malformedURLException.printStackTrace();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }

                            try {
                                /*my device info*/
                                if (modelAdapterDevice(ip).getDevice_Ip().equals(devicesFound.get(i).ip)) {
                                    arrayList.add(modelAdapterDevice(ip));
                                    /*the router info*/
                                } else if (address.equals(devicesFound.get(i).ip)) {
                                    arrayList.add(new ModelAdapterDevice(devicesFound.get(i).mac, macVendor.get(i), devicesFound.get(i).ip, ROUTER, WIFI));
                                } else {
                                    arrayList.add(new ModelAdapterDevice(devicesFound.get(i).mac, macVendor.get(i), devicesFound.get(i).ip, OTHER_DEVICE, WIFI));
                                }
                            } catch (RuntimeException indexOutOfBoundsException) {
                                Log.d("WifiSacnActivity", "error on Array macVendor");
                                arrayList.add(new ModelAdapterDevice(devicesFound.get(i).mac, "N/A", devicesFound.get(i).ip, OTHER_DEVICE, WIFI));

                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*devices found*/
                                num_device.setText("Devices  " + devicesFound.size());
                                recyclerView.setAdapter(new DeviceWifiAdapter(arrayList, getSupportFragmentManager()));
                                Layout_loading.setVisibility(View.GONE);
                                layout_Devices_list.setVisibility(View.VISIBLE);
                                if (subnetDevices != null) {
                                    subnetDevices.cancel();
                                }
                                /*Delete Auto dir Cashe Files*/
                                deleteCache(getApplicationContext());

                            }
                        });

                    }


                    // Stub: Finished scanning
                }
            });
            /*If an error occurred cause of No Found Internet*/
        } catch (IllegalStateException | IllegalAccessError | NullPointerException illegalStateException) {
            Log.e("IllegalStateException", "onReceive: WifiScanActivity");
            Toast.makeText(getApplicationContext(), getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT).show();
            /*If an error occurred*/
        } catch (RuntimeException R3) {
            Log.e("RuntimeException", "onReceive: WifiScanActivity ");
            Toast.makeText(getApplicationContext(), getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT).show();
        }


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
                        WifiScanActivity.this.mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        WifiScanActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        WifiScanActivity.this.mInterstitialAd = null;
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
/*    public static void getMac(String ip , Context context) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {


        if (ip != null) {
            BufferedReader br = null;

            try {
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(Paths.get("/proc/net/arp").toString()), "r", null);
                br = new BufferedReader(new FileReader("/proc/net/arp"));
            } catch (FileNotFoundException e) {
                Log.e("TAG", "file not found", e);
            }
            String line;
            try {
                if (br != null) {
                    while ((line = br.readLine()) != null) {
                        String[] splitted = line.split(" +");
                        if (splitted.length >= 4 && ip.equals(splitted[0])) {
                            String mac = splitted[3];
                            if (mac.matches("..:..:..:..:..:..")) {
                                Log.e("TAG", "onDeviceFound: " + mac );
                            } else {
                                Log.e("TAG", "onDeviceFound: " + "getMac(device.ip)" );
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


            }
        }).start();
    }*/
  /*  protected String getMacAddress(String ipAddress) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/proc/net/arp")));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(ipAddress)) {
                 // this string still would need to be sanitized
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

   public String getMacAddr(InetAddress[] addr) {
        String macAddress = "";
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(addr[0]);
            byte[] macArray = network.getHardwareAddress();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < macArray.length; i++) {
                str.append(String.format("%02X%s", macArray[i], (i < macArray.length - 1) ? " " : ""));
                macAddress = str.toString();
            }
        } catch (Exception e) {
            Log.e("TAG", e.getStackTrace().toString());
        }
        return macAddress;
    }

    public static String getHardwareAddress(String ip) {
        String NOMAC = "00:00:00:00:00:00";
        String hw = NOMAC;
        BufferedReader bufferedReader = null;
        try {
            if (ip != null) {
                String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
                Pattern pattern = Pattern.compile(ptrn);
                bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), BUF);
                String line;
                Matcher matcher;
                while ((line = bufferedReader.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        hw = matcher.group(1);
                        break;
                    }
                }
            } else {
                Log.e("TAG", "ip is null");
            }
        } catch (IOException e) {
            Log.e("TAG", "Can't open/read file ARP: " + e.getMessage());
            return hw;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            }
        }
        return hw;
    }

    private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
    private final static int BUF = 8 * 1024;*/

    private void getMac() {

        String IP_CMD = "ip neighbor";

        BufferedReader reader = null;

        try {
            Process ipProc = Runtime.getRuntime().exec(IP_CMD);
            ipProc.waitFor();
            if (ipProc.exitValue() != 0) {
                throw new Exception("Unable to access ARP entries");
            }

            reader = new BufferedReader(new InputStreamReader(ipProc.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] neighborLine = line.split("\\s+");

                // We don't have a validated ARP entry for this case.
                if (neighborLine.length <= 4) {
                    continue;
                }

                String ipaddr = neighborLine[0];
                InetAddress addr = InetAddress.getByName(ipaddr);
                if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                    continue;
                }

                String macAddress = neighborLine[4];


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
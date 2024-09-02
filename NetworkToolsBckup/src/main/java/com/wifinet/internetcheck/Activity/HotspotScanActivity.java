package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
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
import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import com.wifinet.internetcheck.Adapter.DeviceWifiAdapter;

import com.wifinet.internetcheck.BroadcastReceiver.HotspotBroadcast;
import com.wifinet.internetcheck.BroadcastReceiver.WifiBraodcast;
import com.wifinet.internetcheck.ContactCheck.WifiApManager;
import com.wifinet.internetcheck.Model.ModelAdapterDevice;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.wifinet.internetcheck.DeleteCacheClass.deleteCache;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.HOTSPOT;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.MY_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.OTHER_DEVICE;
import static com.wifinet.internetcheck.My_DeviceInfo.modelAdapterDevice;

public class HotspotScanActivity extends AppCompatActivity {
     HotspotBroadcast hotspotBroadcast;
     IntentFilter intentFilter;
    private static final String baseURL = "https://macvendors.co/api/vendorname/";
    private View Layout_loading;
    private RecyclerView recyclerView;
    private ArrayList<ModelAdapterDevice> arrayList ;
    private ArrayList<String> macVendor;
    private SubnetDevices subnetDevices;
    private TextView num_device;
    private View layout_Devices_list;
    private String TAG = "HotspotActivity";
    public SwipeRefreshLayout swipeRefreshLayout_Hotspot;
    public  TextView TextView_State_NetWork_Hotspot;
    private InterstitialAd mInterstitialAd;
    TrustManager[] trustAllCerts;
    Utils utils;


    SSLContext   sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_scan);

        getSupportActionBar().setElevation(0);

        utils = new Utils(this);
        /* Admob ads Banner*/
      /*  AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        /*Admob Ads InterstitialAd */
      /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/


    /*    mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.InterstitialAd));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
*/
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        hotspotBroadcast = new HotspotBroadcast(this);

        swipeRefreshLayout_Hotspot  = findViewById(R.id.swiperefresh);
        swipeRefreshLayout_Hotspot.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if found WIFIHOTSPOT */
                if(new WifiApManager(Objects.requireNonNull(getApplicationContext())).isWifiApEnabled()){
                SearchDevice();
                swipeRefreshLayout_Hotspot.setRefreshing(false);}
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.NO_INTERNET),Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout_Hotspot.setRefreshing(false);
                }
            }
        });

        layout_Devices_list = findViewById(R.id.layout_Devices_List);
        Layout_loading = findViewById(R.id.loading_layout);
        recyclerView = findViewById(R.id.Device_List);
        num_device = findViewById(R.id.num_device);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        TextView_State_NetWork_Hotspot = findViewById(R.id.text_state_Network);
        arrayList = new ArrayList<>();
        macVendor = new ArrayList<>();

        trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };

      /*   loadAd();*/



         SearchDevice();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(hotspotBroadcast,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
      unregisterReceiver(hotspotBroadcast);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private  void  SearchDevice (){
        try {
            // clear for Array
            arrayList.clear();
            macVendor.clear();
            //cancel  for SubnetDevice for Restart
            if (subnetDevices != null){
                subnetDevices.cancel();
            }

            Layout_loading.setVisibility(View.VISIBLE);
            layout_Devices_list.setVisibility(View.GONE);
            // ip Address HotSpot
            subnetDevices =  SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
                @Override
                public void onDeviceFound(Device device) {

                    // Stub: Found subnet device
                }

                @Override
                public void onFinished(ArrayList<Device> devicesFound) {
/*                    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            HostnameVerifier hv =
                                    HttpsURLConnection.getDefaultHostnameVerifier();
                            return hv.verify("api.macvendors.com", session);

                        }



                    };
                    try {
                        sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        e.printStackTrace();
                    }*/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            utils.SaveValue();

                        }
                    });

                    if (devicesFound.size() > 0){

                        for (int i = 0 ; i < devicesFound.size() ; i++) {
                            /*Get Vendor name for device*/
                            try {
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
                                /*Try again Get Vendor name for device*/
                                Log.d("WifiHotspot", "Try again for get Vendor name ");
                                try {
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
                                } catch (FileNotFoundException | ProtocolException ee) {
                                    // MAC not found
                                    macVendor.add("N/A");
                                    Log.d("WifiHotspot", "failed for found Vendor name");
                                } catch (MalformedURLException malformedURLException) {
                                    malformedURLException.printStackTrace();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            } catch (IOException e) {
                                // Error during lookup, either network or API.

                            }
                            try {
                                /*my device info*/
                            if (modelAdapterDevice("192.168.43.1").getDevice_Ip().equals(devicesFound.get(i).ip)) {
                                arrayList.add(modelAdapterDevice("192.168.43.1"));

                        }else {
                                arrayList.add(new ModelAdapterDevice(devicesFound.get(i).mac,macVendor.get(i),devicesFound.get(i).ip,OTHER_DEVICE,HOTSPOT));

                            }
                            }catch (IndexOutOfBoundsException | NullPointerException indexOutOfBoundsException){
                                Log.d("WifiHotspot", "error on Array macVendor");
                                arrayList.add(new ModelAdapterDevice(devicesFound.get(i).mac,"N/A",devicesFound.get(i).ip,OTHER_DEVICE,HOTSPOT));

                            }

                        }
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                num_device.setText("Devices  " +devicesFound.size());
                                recyclerView.setAdapter(new DeviceWifiAdapter(arrayList,getSupportFragmentManager()));
                                Layout_loading.setVisibility(View.GONE);
                                layout_Devices_list.setVisibility(View.VISIBLE);
                                if (subnetDevices != null){
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
        } catch (IllegalStateException | IllegalAccessError | NullPointerException  illegalStateException) {

            Log.e(TAG, "onReceive: "  );
            Toast.makeText(getApplicationContext(),"no Found Internet please Try again",Toast.LENGTH_SHORT).show();
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
/*    private void getARPIps()   {
        val result = mutableListOf<Pair<String, String>>()
        try {
//        val args = listOf("ip", "neigh")
//        val cmd = ProcessBuilder(args)
//        val process: Process = cmd.start()
            val process = Runtime.getRuntime().exec("ip neigh")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.forEachLine {
                if (!it.contains("FAILED")) {
                    val split = it.split("\\s+".toRegex())
                    if (split.size > 4 && split[0].matches(Regex("([0-9]{1,3}\\.){3}[0-9]{1,3}"))) {
                        result.add(Pair(split[0], split[4]))
                    }
                }
            }
            val errReader = BufferedReader(InputStreamReader(process.errorStream))
            errReader.forEachLine {
                Log.e(TAG, it)
                // post the error message to server
            }
            reader.close()
            errReader.close()
            process.destroy()
        } catch (e: Exception){
            e.printStackTrace()
            // post the error message to server
        }
        return result
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
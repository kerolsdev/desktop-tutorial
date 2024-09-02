package com.wifinet.internetcheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.wifinet.internetcheck.Activity.ScanWifiHacker;
import com.wifinet.internetcheck.Activity.WifiScanActivity;
import com.wifinet.internetcheck.Activity.Wifi_Info;
import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import static android.os.Looper.getMainLooper;

import javax.net.ssl.HttpsURLConnection;

public class WifiHackerMethod {



    ProgressBar progressBar;
    private   int Error_Encrypted  ;
    private   String Result_Encrypted ;
    private  int Error_Single;
    private  String Result_Single;
    private   String Result_Connection;
    private  int Error_Connection;
    TextView textView;
    Context context;
/*    private InterstitialAd mInterstitialAd;*/
    int index;
    boolean isConnect = false;
    TextView Single_view ;
    TextView Encrypted_view;
    TextView Connection_view;
    View Result_Done_View;
    Handler mainHandler = new Handler(getMainLooper());
   // InterstitialAd interstitialAd;
    ScanWifiHacker scanWifiHacker;
    TextView text_pro ;


     public WifiHackerMethod ()
     {



     }

    public WifiHackerMethod (ProgressBar progressBar , TextView textView , Context context )
    {
        this.progressBar = progressBar;
        this.textView = textView;
        this.context = context;

    }

    @SuppressLint("SetTextI18n")
    public void StartScan(/*InterstitialAd mInterstitialAd*/) {
         /* loadAd();*/
          progressBar.setProgress(0);
          textView.setText( ""+ progressBar.getProgress() + " %");



          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  getEncrypted(context);
              }
          },1000);




    }



    public void getEncrypted (Context context) {

        try {

        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") List<ScanResult> networkList = wifi.getScanResults();

//get current connected SSID for comparison to ScanResult
          mainHandler.post(new Runnable() {
              @Override
              public void run() {
                  progressBar.setProgress(30);
                  textView.setText(progressBar.getProgress() + " %");
              }
          });


          WifiInfo wi = wifi.getConnectionInfo();
          String currentSSID = wi.getSSID().replace("\"", "");
          if (networkList != null) {
              Log.e("TA1G", "getSingle: " );

              for (ScanResult network : networkList) {
                  Log.e("T2AG", "getSingle: " );

                  //check if current connected SSID
                  index = index + 1;
                  if (currentSSID.equals(network.SSID)) {
                      //get capabilities of current connection
                      String capabilities = network.capabilities;

                      if (capabilities.contains("WPA2")) {
                          //do something
                          setResult_Encrypted(0, "good");

                      } else if (capabilities.contains("WPA")) {
                          //do something
                          setResult_Encrypted(0, "good");

                      } else if (capabilities.contains("WEP")) {
                          //do something
                          setResult_Encrypted(0, "good");

                      } else {

                          setResult_Encrypted(1, context.getString(R.string.There));
                      }
                      isConnect = true;
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              getSingle(context);
                          }
                      }, 500);
                  }

              }
              if (!isConnect) {
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          setResult_Encrypted(0, "good");
                          getSingle(context);
                      }
                  }, 500);

              }
          } else {
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      setResult_Encrypted(0, "good");
                      getSingle(context);
                  }
              }, 500);
          }


      }catch (RuntimeException  runtimeException) {

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        setResult_Encrypted(1, "no Found Wifi");
        getSingle(context);
    }
},500);


      }
            }

    public void getSingle (Context context) {

        try {

            Log.e("TAG", "getSingle: " );
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            // Level of a Scan Result
            @SuppressLint("MissingPermission") List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {
                int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
                System.out.println("Level is " + level + " out of 5");
            }

            // Level of current connection
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(rssi, 5);
            System.out.println("Level is " + level + " out of 5");
            if (level == 1) {
                setResult_Single(1, context.getString(R.string.Wifi_signal));

            } else {
                setResult_Single(0, "good");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(70);
                    textView.setText(progressBar.getProgress() + " %");
                    getConnection(context);
                }
            },2000);

        }catch (RuntimeException runtimeException) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(70);
                    textView.setText(progressBar.getProgress() + " %");
                    getConnection(context);
                }
            },2000);


        }
    }

    public void getConnection (Context context) {
         new Thread(new Runnable() {
             @Override
             public void run() {

                 publicIpAddress();
                 try {
                     Thread.sleep(500);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }).start();
    }

    public void setResult_Encrypted (int Error , String Result) {
        this.Result_Encrypted = Result;
        this.Error_Encrypted = Error;

    }

    public void setResult_Single(int Error ,String result_Single) {
         Result_Single = result_Single;
         Error_Single = Error;
    }

    public void setResult_Connection(int Error , String result_Connection) {
        Result_Connection = result_Connection;
        Error_Connection = Error;
    }

    public int getError_Encrypted() {
        return Error_Encrypted;
    }

    public int getError_Single() {
        return Error_Single;
    }

    public int getError_Connection() {
        return Error_Connection;
    }

    public String getResult_Connection() {
        return Result_Connection;
    }

    public String getResult_Encrypted() {
        return Result_Encrypted;
    }

    public String getResult_Single() {
        return Result_Single;
    }

    public void Finish () {


        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setProgress(0);
        textView.setText("0 %");
        getText_pro().setVisibility(View.INVISIBLE);
        Utils utils = new Utils(scanWifiHacker);


        if (getError_Single() != 0) {

            getSingle_view().setVisibility(View.VISIBLE);
            getSingle_view().setText(getResult_Single());


        } else if (getError_Single() == 0) {

            getSingle_view().setVisibility(View.GONE);

        }


        if (getError_Encrypted() != 0) {

            getEncrypted_view().setVisibility(View.VISIBLE);
            getEncrypted_view().setText(getResult_Encrypted());

        } else if (getError_Encrypted() == 0) {

            getEncrypted_view().setVisibility(View.GONE);

        }


        if (getError_Connection() != 0) {

            getConnection_view().setVisibility(View.VISIBLE);
            getConnection_view().setText(getResult_Connection());

        } else if (getError_Connection() == 0) {

            getConnection_view().setVisibility(View.GONE);

        }

        if (getError_Connection() == 0 && getError_Encrypted() == 0 && getError_Single() == 0) {

            getResult_Done_View().setVisibility(View.VISIBLE);

        }
        utils.SaveValue();

    }


    public String intToIp(int i) {

        return ((i >> 24 ) & 0xFF ) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ( i & 0xFF) ;

    }

    public void setConnection_view(TextView connection_view) {
        Connection_view = connection_view;
    }

    public void setEncrypted_view(TextView encrypted_view) {
        Encrypted_view = encrypted_view;
    }


    public void setSingle_view(TextView single_view) {
        Single_view = single_view;
    }

    public void setResult_Done_View(View result_Done_View) {
        Result_Done_View = result_Done_View;
    }

    public View getResult_Done_View() {
        return Result_Done_View;
    }


    public TextView getConnection_view() {
        return Connection_view;
    }



    public TextView getEncrypted_view() {
        return Encrypted_view;
    }

    public TextView getSingle_view() {
        return Single_view;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }


    public void setScanWifiHacker(ScanWifiHacker scanWifiHacker) {
        this.scanWifiHacker = scanWifiHacker;
    }

    public ScanWifiHacker getScanWifiHacker() {
        return scanWifiHacker;
    }


    public void setText_pro(TextView text_pro) {
        this.text_pro = text_pro;
    }

    public TextView getText_pro() {
        return text_pro;
    }

/*    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                scanWifiHacker,
                context.getString(R.string.InterstitialAd),
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

    }*/
    public static boolean isConnected(Context context) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }

        return false;

    }

    public void publicIpAddress () {
                        try {
                            StringBuilder result = new StringBuilder();
                            URL url = new URL("https://api.ipify.org/");
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
                            String finalLine = result.toString();

                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setResult_Connection(0, "Done");
                                    Finish();
                                }
                            });
                        } catch (RuntimeException | IOException exception){
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    setResult_Connection(1, context.getString(R.string.there_is_no_Internet));
                                    Finish();
                                }
                            });
                        }
    }


}
//
/* try {

            if (isConnected(context)) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        setResult_Connection(0, "Done");
                        Finish();

                    }
                });

            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        setResult_Connection(1, context.getString(R.string.there_is_no_Internet));
                        Finish();

                    }
                });

            }
        }catch (RuntimeException runtimeException) {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    setResult_Connection(0, "Done");
                    Finish();

                }
            });

        }*/
package com.wifinet.internetcheck.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import com.wifinet.internetcheck.speedTest.GetSpeedTestHostsHandler;
import com.wifinet.internetcheck.speedTest.DevilDownloadTest;
import com.wifinet.internetcheck.speedTest.HttpUploadTest;
import com.wifinet.internetcheck.speedTest.PingTest;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.speedTest.GetSpeedTestHostsHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class SpeedTestActivity extends AppCompatActivity {
    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;
    private TextView Country , Server , Download , Upload , textSpeed , textSpeedMb , PingText ;
    private Button startButton;
    private LinearProgressIndicator progressBar;
    private static final int APP_PERMISSION_REQUEST = 102;
    private DecimalFormat dec;
    private DecimalFormat decmbs;
    boolean doubleBackToExitPressedOnce = false;
 /*   private AdView mAdView;*/
    private TextView TypeNet;


  /*  private InterstitialAd mInterstitialAd;*/
    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_test);




        /**install InterstitialAd ads*/
/*        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        *//**setup banner ads*//*
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        try {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        }catch (RuntimeException runtimeException) {

        }

      /*  loadAd();*/

        /**startButton*/

        startButton = (Button) findViewById(R.id.start_test);
        Upload = findViewById(R.id.upload);
        Download = findViewById(R.id.download);
        textSpeed = findViewById(R.id.textSpeed);
        textSpeedMb = findViewById(R.id.textSpeedMb);
        Country = findViewById(R.id.Country);
        Server = findViewById(R.id.Server);
        progressBar = findViewById(R.id.progressBar);
        PingText = findViewById(R.id.Ping);
        TypeNet = findViewById(R.id.TypeNet);
        textSpeedMb = findViewById(R.id.textSpeedMb);







        dec = new DecimalFormat("#.##");
        decmbs = new DecimalFormat("#.###");

        tempBlackList = new HashSet<>();
        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();


        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mainrun();

            }
        });
    }


    public void mainrun() {
        try {


            startButton.setEnabled(false);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);

            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();
            }

            new Thread(new Runnable() {


                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });

                    //Get egcodes.speedtest hosts
                    int timeCount = 600; //1min
                    while (!getSpeedTestHostsHandler.isFinished()) {
                        timeCount--;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                        if (timeCount <= 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                    startButton.setEnabled(true);
                                    startButton.setText("Restart Test");
                                }
                            });
                            getSpeedTestHostsHandler = null;
                            return;
                        }
                    }

                    try {

                        //Find closest server
                        HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                        HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                        double selfLat = getSpeedTestHostsHandler.getSelfLat();
                        double selfLon = getSpeedTestHostsHandler.getSelfLon();
                        double tmp = 19349458;
                        double dist = 0.0;
                        int findServerIndex = 0;
                        for (int index : mapKey.keySet()) {
                            if (tempBlackList.contains(mapValue.get(index).get(5))) {
                                continue;
                            }

                            Location source = new Location("Source");
                            source.setLatitude(selfLat);
                            source.setLongitude(selfLon);

                            List<String> ls = mapValue.get(index);
                            Location dest = new Location("Dest");
                            dest.setLatitude(Double.parseDouble(ls.get(0)));
                            dest.setLongitude(Double.parseDouble(ls.get(1)));

                            double distance = source.distanceTo(dest);
                            if (tmp > distance) {
                                tmp = distance;
                                dist = distance;
                                findServerIndex = index;
                            }
                        }

                        String testAddr = mapKey.get(findServerIndex).replace("http://", "https://");
                        final List<String> info = mapValue.get(findServerIndex);
                        final double distance = dist;

                        if (info == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), getString(R.string.Error), Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Server.setText(info.get(5));
                                Country.setText(info.get(3));
                            }
                        });


                        //Reset value, graphics
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PingText.setText("--");
                                Download.setText("--");
                                Upload.setText("--");
                                TypeNet.setText("");
                                textSpeedMb.setText("");
                                progressBar.setProgress(0);
                                progressBar.setMax(100);
                                progressBar.setIndeterminate(true);
                                progressBar.setVisibility(View.VISIBLE);
                                startButton.setVisibility(View.GONE);
                            }
                        });

                        final List<Double> pingRateList = new ArrayList<>();
                        final List<Double> downloadRateList = new ArrayList<>();
                        final List<Double> uploadRateList = new ArrayList<>();
                        Boolean pingTestStarted = false;
                        Boolean pingTestFinished = false;
                        Boolean downloadTestStarted = false;
                        Boolean downloadTestFinished = false;
                        Boolean uploadTestStarted = false;
                        Boolean uploadTestFinished = false;

                        //Init Test
                        final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 3);
                        final DevilDownloadTest downloadTest = new DevilDownloadTest(testAddr.replace(testAddr.split("/")[testAddr.split("/").length - 1], ""));
                        final HttpUploadTest uploadTest = new HttpUploadTest(testAddr);


                        //Tests
                        while (true) {
                            if (!pingTestStarted) {
                                pingTest.start();
                                pingTestStarted = true;
                            }
                            if (pingTestFinished && !downloadTestStarted) {
                                downloadTest.start();
                                downloadTestStarted = true;
                            }
                            if (downloadTestFinished && !uploadTestStarted) {
                                uploadTest.start();
                                uploadTestStarted = true;
                            }


                            //Ping Test
                            if (pingTestFinished) {
                                //Failure
                                if (pingTest.getAvgRtt() == 0) {
                                    System.out.println("Ping error...");
                                } else {
                                    //Success
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(0);
                                            PingText.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                        }
                                    });
                                }
                            } else {
                                pingRateList.add(pingTest.getInstantRtt());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PingText.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                    }
                                });

                                //Update chart
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        TypeNet.setText("Ping");
                                        List<Double> tmpLs = new ArrayList<>(pingRateList);
                                        for (Double val : tmpLs) {
                                            progressBar.setProgress(val.intValue());
                                            textSpeed.setText("" + val);
                                        }


                                    }
                                });
                            }


                            //Download Test
                            if (pingTestFinished) {
                                if (downloadTestFinished) {
                                    //Failure
                                    if (downloadTest.getFinalDownloadRate() == 0) {
                                        System.out.println("Download error...");
                                    } else {
                                        //Success
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Download.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                                progressBar.setProgress(0);
                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double downloadRate = downloadTest.getInstantDownloadRate();
                                    downloadRateList.add(downloadRate);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Download.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");

                                        }

                                    });
                                    lastPosition = position;

                                    /**Update chart*/
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TypeNet.setText("Download");
                                            progressBar.setIndeterminate(false);
                                            List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                            textSpeedMb.setText("mb");
                                            int count = 0;
                                            for (Double val : tmpLs) {
                                                //downloadSeries.add(count++, val);
                                                textSpeed.setText(" " + val);
                                                progressBar.setProgress(val.intValue());
                                            }

                                        }
                                    });

                                }
                            }


                            //Upload Test
                            if (downloadTestFinished) {
                                if (uploadTestFinished) {
                                    //Failure
                                    if (uploadTest.getFinalUploadRate() == 0) {
                                        System.out.println("Upload error...");
                                    } else {
                                        //Success
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Upload.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");

                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double uploadRate = uploadTest.getInstantUploadRate();
                                    uploadRateList.add(uploadRate);


                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Upload.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                        }

                                    });
                                    lastPosition = position;

                                    //Update chart
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TypeNet.setText("Upload");
                                            List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                            for (Double val : tmpLs) {
                                                textSpeed.setText(" " + val);
                                                progressBar.setProgress(val.intValue());
                                            }

                                        }
                                    });

                                }
                            }

                            //Test bitti
                            if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                break;
                            }

                            if (pingTest.isFinished()) {
                                pingTestFinished = true;
                            }
                            if (downloadTest.isFinished()) {
                                downloadTestFinished = true;
                            }
                            if (uploadTest.isFinished()) {
                                uploadTestFinished = true;
                            }

                            if (pingTestStarted && !pingTestFinished) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TypeNet.setText("");
                                textSpeed.setText("00.0");
                                textSpeedMb.setText("");
                                startButton.setEnabled(true);
                                startButton.setText("Restart Test");
                                progressBar.setVisibility(View.GONE);
                                startButton.setVisibility(View.VISIBLE);
                               /* if (mInterstitialAd != null) {
                                    mInterstitialAd.show(SpeedTestActivity.this);
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }*/

                            }
                        });


                    } catch (RuntimeException runtimeException){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TypeNet.setText("");
                                textSpeed.setText("00.0");
                                textSpeedMb.setText("");
                                startButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                startButton.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                }
            }).start();
        }catch (RuntimeException runtimeException) {
            TypeNet.setText("");
            textSpeed.setText("00.0");
            textSpeedMb.setText("");
            startButton.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
        }
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Draw over other app permission enable.", Toast.LENGTH_SHORT).show();
        } else {

        }
    }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }



  /*  public void loadAd() {
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

    }*/
  @Override
  protected void onDestroy() {
      super.onDestroy();

  }

}


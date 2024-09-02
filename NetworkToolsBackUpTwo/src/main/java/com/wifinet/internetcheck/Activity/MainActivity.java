package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import com.wifinet.internetcheck.BuildConfig;
import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;
import com.wifinet.internetcheck.ContactCheck.WifiApManager;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.ShowDialog;
import com.wifinet.internetcheck.Utils;
import com.wifinet.internetcheck.wifiana.Wifi_ana;

import java.io.File;
import java.util.Objects;

import static com.wifinet.internetcheck.ContactCheck.ConnectionDetector.isNetWorkWifiOn;
import static com.wifinet.internetcheck.DeleteCacheClass.deleteCache;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private MenuItem prevMenuItem;
    public static DrawerLayout drawer;
    public  static  boolean isVisible = false;

    public  static  View view_main;
    MaterialCardView
             card_who_is
            ,card_firewall
            ,card_speedTest,card_info,card_wifi_hotSpot
            , card_wifi_Analyzer ,
            card_wifipassword, card_login,
            card_WIFI_SCANNER;
    MaterialCardView card_Scan;
    Utils utils;


    // for broadcast wifi and wifi hotspot
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        utils = new Utils(this);
        if (utils.Result() == true && utils.getFinish() == false) {
            ShowDialog showDialog = new ShowDialog();
            showDialog.showCustomDialog(this);
            utils.setFinish();
        }

       /* InetSocketAddress remoteInetSocketAddress = new InetSocketAddress(finalHost, srcPort);
        InetSocketAddress localInetSocketAddress = new InetSocketAddress(1234);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Method method = ConnectivityManager.class.getMethod("getConnectionOwnerUid", int.class, InetSocketAddress.class, InetSocketAddress.class);
        int uid = (int) ((Method) method).invoke(connectivityManager, IPPROTO_TCP, localInetSocketAddress, remoteInetSocketAddress);
        if (uid != INVALID_UID) {
            // UID access here
        }*/

        card_info = findViewById(R.id.WifiInfo);
        card_speedTest = findViewById(R.id.SpeedTest);
        card_who_is = findViewById(R.id.Whois);
        card_wifi_hotSpot = findViewById(R.id.WifiHotspot);
        card_Scan = findViewById(R.id.card_scan);
        card_wifi_Analyzer  = findViewById(R.id.WiFiAnalyzer);
        card_wifipassword= findViewById(R.id.wifipassword);
        card_WIFI_SCANNER = findViewById(R.id.WIFI_Qr);
        /*card_firewall = findViewById(R.id.firewall);*/
        card_login = findViewById(R.id.RouterLogin);
       /* card_wps = (MaterialCardView) findViewById(R.id.WIFIWPS);*/



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            card_wifi_hotSpot.setVisibility(View.GONE);
        }

      /*  card_firewall.setOnClickListener(this);*/
        card_login.setOnClickListener(this);
        card_wifi_hotSpot.setOnClickListener(this);
        card_speedTest.setOnClickListener(this);
        card_info.setOnClickListener(this);
        card_who_is.setOnClickListener(this);
        card_Scan.setOnClickListener(this);
        card_WIFI_SCANNER.setOnClickListener(this);
        card_wifipassword.setOnClickListener(this);



     /*   card_wps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION}, 60);
            }
        });*/


        card_wifi_Analyzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        view_main = findViewById(R.id.layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_versionApp).setTitle(BuildConfig.VERSION_NAME);
        navigationView.setNavigationItemSelectedListener(this);

      /*  IronSourceAdQuality.getInstance().initialize(this, "1c42ae5cd");*/
       /* FrameLayout frameLayout = findViewById(R.id.banner_footer);*/


        /* Admob ads Banner*/



      /*  AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.card_color));
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                           FrameLayout template = (FrameLayout) findViewById(R.id.my_template_main);
                           template.setVisibility(View.VISIBLE);
                          AddViewNativeAdmob addViewNativeAdmob = new AddViewNativeAdmob(MainActivity.this);
                          addViewNativeAdmob.displayNativeAd(template,nativeAd);


                    }
                })
                .build();
        adLoader.loadAd(new AdManagerAdRequest.Builder().build());*/

     AdView mAdView = findViewById(R.id.adView);
      AdRequest adRequest = new AdRequest.Builder().build();
      mAdView.loadAd(adRequest);


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {

            int itemId = item.getItemId();/*share app apk*/
            if (itemId == R.id.nav_share) {// shareApplication();
                Intent email2 = new Intent(Intent.ACTION_SEND);
                email2.putExtra(
                        Intent.EXTRA_TEXT,
                        " Download the app for free \n\n" +
                                "https://play.google.com/store/apps/details?id=com.wifinet.internetcheck "
                );

                //need this to prompts email client only

                //need this to prompts email client only
                email2.setType("text/plain");

                startActivity(Intent.createChooser(email2, "Share App"));
                //for send email
            } else if (itemId == R.id.nav_feedback) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                /*Write here Email to */
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.Email_to)});
                /*Write Subject */
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
                /*Write Message*/
                email.putExtra(Intent.EXTRA_TEXT, getString(R.string.message));
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, getString(R.string.createChooser_title)));
            } else if (itemId == R.id.nav_RetaUS) {/*Reta us on PlayStore or Other */
                final String appPackageName = BuildConfig.APPLICATION_ID;
             /*       try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException anfe) {*/
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // }
            } else if (itemId == R.id.nav_Privacy_policy) {
                Intent intent2 = new Intent(getApplicationContext(), Privacy_Policy.class);
                startActivity(intent2);
            } else if (itemId == R.id.nav_versionApp) {
                Toast.makeText(this, BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
                    /* case R.id.nav_more_apps:
                    try {

                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("https://play.google.com/store/apps/dev?id=7310728841968332288"));
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    } catch (ActivityNotFoundException activityNotFoundException) {

                        Toast.makeText(getApplicationContext(), getString(R.string.There_app), Toast.LENGTH_LONG).show();
                    }
*/
            }
        }catch (ActivityNotFoundException activityNotFoundException) {

        }
     //   drawer.closeDrawer(GravityCompat.START);
        return true;
    }
   /*Custom Toast for Waring No Internet*/
    public static void toast (Context context , boolean Wifi_HotSpot , int Order_Name) {
          if (view_main != null) {
            Animation SlideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            Animation SlideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            View layout = (View) view_main.findViewById(R.id.layout_toast);
            MaterialButton Enable = layout.findViewById(R.id.Enable);
            Handler handler = new Handler();
            TextView textView = layout.findViewById(R.id.toast_name);

            switch (Order_Name){
                case 1 :
                    textView.setText(context.getString(R.string.WIFI_OFF));
                    break;
                case 2 :
                    textView.setText(context.getString(R.string.NO_INTERNET));
                    break;
                case 3 :
                    textView.setText(context.getString(R.string.HotSpot_off));
                    break;
                default:
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();

            }


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!isVisible) {
                        handler.postDelayed(this, 2000);
                        layout.setVisibility(View.VISIBLE);
                        layout.startAnimation(SlideDown);
                        isVisible = true;
                    } else {
                        layout.setVisibility(View.GONE);
                        handler.removeCallbacks(this);
                        layout.clearAnimation();
                        layout.startAnimation(SlideUp);
                        isVisible = false;
                    } } };


              Enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                    layout.clearAnimation();
                    layout.startAnimation(SlideUp);
                    isVisible = false;
                    /*go to setting to open Wifi */
                    try {
                        if (!Wifi_HotSpot) {
                            Intent intent = new Intent(Intent.ACTION_MAIN, null);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            intent.setComponent(cn);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            /*go to setting to open Wifi HotSpot */
                            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                            intent.setComponent(cn);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }catch (ActivityNotFoundException activityNotFoundException) {
                        if (!Wifi_HotSpot) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }catch (RuntimeException runtimeException) {

                            }

                        }


                    }
                }
            });

            if (!isVisible) {
                handler.postDelayed(runnable
                        , 2);
            }
        }
    }
    /*Share app apk */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
     try {
         Intent intent = new Intent(Intent.ACTION_SEND);
         // MIME of .apk is "application/vnd.android.package-archive".
         intent.setType("application/vnd.android.package-archive");
         // Append file and send Intent
         intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivityForResult(Intent.createChooser(intent, getString(R.string.ShareApp)), 1);
     }catch (FileUriExposedException fileUriExposedException) {
         Uri uri = FileProvider.getUriForFile(getApplicationContext(),"com.wifinet.internetcheck",new File(filePath));
         Intent intent2 = ShareCompat.IntentBuilder.from(this)
                 .setStream(uri) // uri from FileProvider
                 .getIntent()
                 .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                 .setAction(Intent.ACTION_VIEW) //Change if needed
                 .setDataAndType(uri, "*/*")
                 .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                 startActivity(Intent.createChooser(intent2,getString(R.string.ShareApp))); }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Delete Auto Cash Files*/
        deleteCache(getApplicationContext());
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.Whois) {
            if (isNetWorkWifiOn(Objects.requireNonNull(getApplication()))) {
                Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }
        } else if (id == R.id.WifiHotspot) {
            if (new WifiApManager(Objects.requireNonNull(getApplication())).isWifiApEnabled()) {
                Intent intent = new Intent(getApplicationContext(), HotspotScanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                MainActivity.toast(getApplicationContext(), true, 3);
            }
        } else if (id == R.id.WifiInfo) {
            if (isNetWorkWifiOn(Objects.requireNonNull(getApplication()))) {
                Intent intent = new Intent(this, Wifi_Info.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }
        } else if (id == R.id.SpeedTest) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        } else if (id == R.id.card_scan) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        } else if (id == R.id.wifipassword) {
            if (isNetWorkWifiOn(Objects.requireNonNull(getApplication()))) {
                Intent intent3 = new Intent(getApplicationContext(), Password.class);
                startActivity(intent3);
            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }
        } else if (id == R.id.WIFI_Qr) {
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()) {
                //TODO: Code to execute if wifi is enabled.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 4);
            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }
        } /*else if (id == R.id.firewall) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                                        == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, Renma.class);
                    startActivity(intent);
                }else
                {
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
                }
            }else {
                Intent intent = new Intent(this, Renma.class);
                startActivity(intent);
            }

        }*/ else if (id == R.id.RouterLogin){
            if (isNetWorkWifiOn(Objects.requireNonNull(getApplication()))) {
                Intent intent = new Intent(getApplicationContext(), RouterLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (isNetWorkWifiOn(Objects.requireNonNull(getApplication()))) {
                Intent intent = new Intent(getApplicationContext(), ScanWifiHacker.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }


        } else if (requestCode == 3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                Intent intent = new Intent(getApplicationContext(), SpeedTestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                MainActivity.toast(getApplicationContext(), false, 2);
            }
        } else if (requestCode == 4 &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            Intent intent3 = new Intent(getApplicationContext(), WIFI_SCANNER.class);
            startActivity(intent3);

        }else if (requestCode == 10 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()) {
                //TODO: Code to execute if wifi is enabled.
                Intent intent = new Intent(MainActivity.this, Wifi_ana.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }


        }else if (requestCode == 60 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED ) {

           /*  WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (wifi.isWifiEnabled()) {
                //TODO: Code to execute if wifi is enabled.
                Intent intent = new Intent(MainActivity.this, WPS.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            } else {
                MainActivity.toast(getApplicationContext(), false, 1);
            }*/



    }else {
            if (utils.getValueRequest()){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 1);
                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();

            }  else {
                utils.SaveValueRequest();
                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                    }


        }


    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gift,menu);
        return true;
    }*/
    /*    public void showRateApp() {
        ReviewManager reviewManager = ReviewManagerFactory.create(this);
        Task <ReviewInfo> request = reviewManager.requestReviewFlow();
         request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Getting the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Log.e("ww", "showRateApp: Failed " );

                Task <Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                });
            }else {

                Log.e("showRateApp", "showRateApp: Failed " );
            }
        });
    }*/


  /*  @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.gift) {

            new AdsShow().showCustomDialog(this);

        }

        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
     /* if (adsShow != null) {
          adsShow.createAndloadBanner();
      }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    /*    adsShow.destroyAndDetachBanner();*/

    }

}


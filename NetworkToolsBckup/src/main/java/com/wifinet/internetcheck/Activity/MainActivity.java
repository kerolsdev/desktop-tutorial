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
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import com.wifinet.internetcheck.BuildConfig;
import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;
import com.wifinet.internetcheck.ContactCheck.WifiApManager;
import com.wifinet.internetcheck.R;
import com.wifinet.internetcheck.ShowDialog;
import com.wifinet.internetcheck.Utils;
import com.wifinet.internetcheck.fragment.FragmentViewPager;
import com.wifinet.internetcheck.wifiana.Wifi_ana;

import java.io.File;
import java.util.Objects;

import static com.wifinet.internetcheck.ContactCheck.ConnectionDetector.isNetWorkWifiOn;
import static com.wifinet.internetcheck.DeleteCacheClass.deleteCache;

public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);


         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         ViewPager2 viewPager2 = findViewById(R.id.ViewpagerMain);
         BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNav);

        FragmentViewPager fragmentViewPager = new FragmentViewPager(this);
        viewPager2.setAdapter(fragmentViewPager);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home){
                    viewPager2.setCurrentItem(0);
                    return true;
                }else if (item.getItemId() == R.id.MyWifi){
                    viewPager2.setCurrentItem(1);
                    return true;
                }else if (item.getItemId()== R.id.Setting) {
                    viewPager2.setCurrentItem(2);
                    return true;
                }else {
                    return false;
                }
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                  if (position == 0) {
                       bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(true);
                  }else if (position == 1){
                      bottomNavigationView.getMenu().findItem(R.id.MyWifi).setChecked(true);
                 }else  {
                      bottomNavigationView.getMenu().findItem(R.id.Setting).setChecked(true);
                }
            }

        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Delete Auto Cash Files*/
        deleteCache(getApplicationContext());
    }



}


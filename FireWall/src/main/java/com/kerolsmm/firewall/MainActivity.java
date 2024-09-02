package com.kerolsmm.firewall;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VPN = 0;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateStatus();
        }
    };

    private ProgressBar mProgressBar;
    private RecyclerView mAppList;
    private TextView mWarningText;
    private FrameLayout frameLayout;
    private InterstitialAd mInterstitialAd;
    private AppAdapter mAppAdapter;

    private MenuItem mShowCheckedItem;

    private AppInfo.FilterCallback mAppFilter;
    private LabeledSwitch labeledSwitch;
    private Toolbar toolbar;
    private ChipGroup chipGroup;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint({"RestrictedApi", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("kerolsaa",MODE_PRIVATE);
        editor = sharedPreferences.edit();


        toolbar = findViewById(R.id.Toolbar_id);

        mProgressBar = findViewById(R.id.progress_circular);

        mAppList = (RecyclerView) findViewById(R.id.app_list);

        setSupportActionBar(toolbar);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        chipGroup = findViewById(R.id.chip_group);
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
             try {
                if (checkedIds.get(0) == R.id.chip_blocked){
                   setFilter(app -> app.checked);
                }else
                {
                  setFilter(null);
                }
             }catch (RuntimeException runtimeException) {

             }
            }
        });



        loadAd();


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        }


        labeledSwitch = findViewById(R.id.switchWidget);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                try {
                if (OfflineVpnService.isRunning) {
                    OfflineVpnService.disconnect(getApplicationContext());
                } else {
                    Intent intent = VpnService.prepare(getApplicationContext());
                    if (intent != null) {
                        startActivityForResult(intent, REQUEST_VPN);
                    } else {
                        onActivityResult(REQUEST_VPN, RESULT_OK, null);
                    }

                }
                }catch (RuntimeException ignored) {

                }
            }
        });


        try {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        }catch (RuntimeException runtimeException) {

        }
     /*   mAppList.setLayoutManager(new LinearLayoutManager(this));
        mAppList.addItemDecoration(new DividerItemDecoration(
                mAppList.getContext(),
                RecyclerView.VERTICAL
        ));*/
       resetAdapter();
       if (sharedPreferences.getBoolean("rate", false)  && !sharedPreferences.getBoolean("rate_",false)) {
              try {
                  mRateApp();
              }catch (RuntimeException runtimeException ) {

              }
       }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

        updateStatus();

        IntentFilter filter = new IntentFilter();
        filter.addAction(OfflineVpnService.ACTION_START);
        filter.addAction(OfflineVpnService.ACTION_STOP);
        registerReceiver(mBroadcastReceiver, filter);


        }catch (RuntimeException runtimeException) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mBroadcastReceiver);
        }catch (RuntimeException r) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar22);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                try {
                    if (chipGroup.getCheckedChipId() == R.id.chip_blocked) {
                        try {
                        final String _query = query.toLowerCase();
                        setFilter(app -> app.checked && app.label.toLowerCase().contains(_query)
                                || app.checked && app.packageName.toLowerCase().contains(_query));
                    }catch(RuntimeException ignored){

                    }
                }else
                    {
                        try {
                            final String _query = query.toLowerCase();
                            setFilter(app ->  app.label.toLowerCase().contains(_query)
                                    || app.packageName.toLowerCase().contains(_query));
                        }catch(RuntimeException ignored){

                        }
                    }
                }catch (RuntimeException ignored) {

                }
                return false;
            }
        });
       /* searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) { return true; }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                setFilter(null);
                return true;
            }
        });*/

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VPN) {
            if (resultCode == RESULT_OK) {
                OfflineVpnService.connect(this);
                editor.putBoolean("rate",true);
                editor.apply();
            }
        }
    }

    private void updateStatus() {
        try {
            labeledSwitch.setOn(OfflineVpnService.isRunning);
        }catch (RuntimeException runtimeException ) {

        }
    }

    private void setFilter(AppInfo.FilterCallback filter) {
        try {
            mAppFilter = filter;
            mAppAdapter.filtrate(mAppFilter);
        }catch (RuntimeException runtimeException) {

        }

    }

    private void resetAdapter() {
        try {

        mAppList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
            final List<AppInfo> apps = AppInfo.getInstalledApps(this);
            runOnUiThread(() -> {
             try {
                mAppAdapter = new AppAdapter(this, apps);
                mAppList.setAdapter(mAppAdapter);
                mAppAdapter.filtrate(mAppFilter);
                 mProgressBar.setVisibility(View.GONE);
                 mAppList.setVisibility(View.VISIBLE);

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(this);
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

             }catch (RuntimeException r) {

             }
            });
            }catch (RuntimeException ignored) {

            }
        }).start();

        }catch (RuntimeException ignored) {

        }
    }

    static void applyTheme(String id) {
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(id));
    }

    public void replaceFragments(Fragment fragmentClass) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.app_list, fragmentClass)
                .commit();
    }


    public void loadAd() {
        try {
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
        }catch (RuntimeException runtimeException) {

        }
    }

    private void mRateApp() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Log.e("TAG", "mRateApp: " );
                Task<Void> flow = manager.launchReviewFlow(this , reviewInfo);
                flow.addOnCompleteListener(task_t -> {
                    editor.putBoolean("rate_",true);
                    editor.apply();
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, log or handle the error code.
            }
        });

    }


}
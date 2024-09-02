package com.kerols.phoneboost.Activitys

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

import com.kerols.phoneboost.R
import com.kerols.phoneboost.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration
    val context: Context = this
    /*private var mInterstitialAd: InterstitialAd? = null*/
    private  var mInterstitialAd: InterstitialAd ? = null
    private var retryAttempt = 0.0

    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        val navController: NavController = Navigation.findNavController(this,
            R.id.myNavHostFragment
        )
        appBarConfiguration  = AppBarConfiguration(navController.graph)
        setupWithNavController(binding.toolbar, navController, appBarConfiguration)




        val params = ConsentRequestParameters
            .Builder()
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainActivity,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        if (loadAndShowError != null) {
                            Log.w("TAG", String.format("%s: %s",
                                loadAndShowError.errorCode,
                                loadAndShowError.message
                            ))
                        }

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w("TAG", String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message
                ))
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }




        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id == R.id.package_finish || destination.id == R.id.cleanerFinish) {
                if (mInterstitialAd != null){
                    mInterstitialAd?.show(this)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onPause() {
        super.onPause()

    }

    companion object {
        val runUi: Handler
            get() = Handler(Looper.getMainLooper())

        fun isSystemPackage(resolveInfo: ResolveInfo): Boolean {
            return resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,getString(R.string.admob_interstitial), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

    }

}


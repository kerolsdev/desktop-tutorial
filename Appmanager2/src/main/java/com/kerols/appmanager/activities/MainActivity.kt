package com.kerols.appmanager.activities


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.kerols.appmanager.R
import com.kerols.appmanager.adapters.ViewPagerAdapter
import com.kerols.appmanager.databinding.ActivityMainBinding
import com.kerols.appmanager.functions.MvvmData
import com.kerols.appmanager.model.AppModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private  var binding : ActivityMainBinding? = null

    private lateinit var  saveApp : SharedPreferences

    lateinit var editor: SharedPreferences.Editor

    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    val mvvmData : MvvmData by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbar)


        saveApp  = getSharedPreferences("appManager", MODE_PRIVATE)
        editor = saveApp.edit()


        // Make sure to set the mediation provider value to "max" to ensure proper functionality
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
                        Log.w("TAG", String.format("%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        ))

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

        val viewPagerAdapter = ViewPagerAdapter(this)

        binding?.viewpager2?.adapter = viewPagerAdapter

        binding?.viewpager2?.offscreenPageLimit = 4


        binding?.viewpager2?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {

                    0 -> {
                        binding?.BottomNav?.menu?.findItem(R.id.user)?.isChecked = true
                        binding?.toolbar?.title = "User Apps"
                        editor.putBoolean("one",true)
                        editor.apply()
                    }
                    1 -> {
                        binding?.BottomNav?.menu?.findItem(R.id.System)?.isChecked = true
                        binding?.toolbar?.title = "System Apps"
                        editor.putBoolean("two",true)
                        editor.apply()

                    }
                    2 -> {
                        binding?.BottomNav?.menu?.findItem(R.id.Battery)?.isChecked = true
                        binding?.toolbar?.title = "Battery Usage"
                        editor.putBoolean("there",true)
                        editor.apply()
                    }
                    3 -> {
                        binding?.BottomNav?.menu?.findItem(R.id.Network)?.isChecked = true
                        binding?.toolbar?.title = "Network Usage"
                        editor.putBoolean("four",true)
                        editor.apply()

                    }
                    4 -> {
                        binding?.BottomNav?.menu?.findItem(R.id.permission)?.isChecked = true
                        binding?.toolbar?.title = "Access Apps"
                    }

                }
            }
        })


        binding?.BottomNav?.setOnItemSelectedListener(mOnNavigationItemSelectedListener)



        if (saveApp.getBoolean("one",false) && saveApp.getBoolean("two",false)
            && saveApp.getBoolean("there",false) && saveApp.getBoolean("four",false)  ) {

            mRateApp()

        }

/*        val toggle = ActionBarDrawerToggle(
            this, binding!!.drawerLayout, binding!!.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        binding?.navView?.setNavigationItemSelectedListener(this)
        toggle.syncState()*/

        readByRxJava(object : Observer<ArrayList<AppModel>> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
               /* Log.e("TAG", "onComplessste: ", )*/
            }

            override fun onNext(t: ArrayList<AppModel>) {

            }
        },this)


        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            moveTaskToBack(true)
        }
    }
    private fun  readByRxJava (observer: Observer<ArrayList<AppModel>>, context: Context)
            : Observable<ArrayList<AppModel>> {


        var arrayListUser = ArrayList<AppModel>()
        var arrayListSystem = ArrayList<AppModel>()
        var arrayList = ArrayList<AppModel>()

        val observable : Observable<ArrayList<AppModel>> = Observable.create<ArrayList<AppModel>> { emitter ->

            val pm: PackageManager? = context.packageManager
            val applicationInfos : MutableList<ApplicationInfo>? =
                pm?.getInstalledApplications(PackageManager.GET_META_DATA)


            if (applicationInfos != null) {

                for (applicationInfo in applicationInfos) {

                    /*val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager


                    val appStorageStats = storageStatsManager.queryStatsForPackage (StorageManager.UUID_DEFAULT, applicationInfo.packageName, Process.myUserHandle() )
                    val size = appStorageStats.appBytes + appStorageStats.dataBytes + appStorageStats.cacheBytes*/
                   /* val appName = pm?.getApplicationLabel(applicationInfo).toString()*/
                  /*  val file: File = File(applicationInfo.publicSourceDir)
                    val size = file.length()*/
                    if (isSystem(applicationInfo)){
                        arrayListSystem.add(AppModel(0,"appName",applicationInfo.packageName,null,applicationInfo.sourceDir,applicationInfo))
                    } else {
                        arrayListUser.add(AppModel(0,"appName",applicationInfo.packageName,null,applicationInfo.sourceDir,applicationInfo))
                    }
                        arrayList.add(AppModel(0,"appName",applicationInfo.packageName,null,applicationInfo.sourceDir,applicationInfo))

                }



                Handler(Looper.getMainLooper()).post(Runnable {
                    mvvmData.setUserApp(arrayListUser)
                    mvvmData.setSystemApp(arrayListSystem)
                    mvvmData.setAllApp(arrayList)
                    emitter.onNext(arrayListSystem)
                    emitter.onComplete()
                })



            }


        }

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)


        return observable

    }

    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener{ item ->
        when (item.itemId) {
            R.id.user -> {
                binding?.viewpager2?.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.System -> {
                binding?.viewpager2?.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.Battery -> {
                binding?.viewpager2?.currentItem = 2
                return@OnItemSelectedListener true
            }
            R.id.Network -> {
                binding?.viewpager2?.currentItem = 3
                return@OnItemSelectedListener true
            }
           R.id.permission -> {
                binding?.viewpager2?.currentItem = 4
                return@OnItemSelectedListener true
            }
        }
        false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
    private fun mRateApp() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                // utils?.saveReview()
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }


            } else {
                // There was some problem, log or handle the error code.
                /*utils?.getBattery() == true && utils?.getApps() == true
            &&*/
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        binding?.adView?.loadAd(adRequest)

        binding?.adView?.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                binding?.adView?.visibility = View.VISIBLE
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

    }


}
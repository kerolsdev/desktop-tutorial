package com.kerolsmm.appmanager.activities


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewManagerFactory

import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.adapters.ViewPagerAdapter
import com.kerolsmm.appmanager.databinding.ActivityMainBinding
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.model.AppModel
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private  var binding : ActivityMainBinding? = null

    private lateinit var  saveApp : SharedPreferences

    lateinit var editor: SharedPreferences.Editor


    private val mvvmData : MvvmData by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH),5)


        setSupportActionBar(binding?.toolbar)


        saveApp  = getSharedPreferences("appManager", MODE_PRIVATE)
        editor = saveApp.edit()

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

        readByRxJava(this)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            moveTaskToBack(true)
        }
    }
    private fun  readByRxJava (context: Context)
        {
            if (!mvvmData.getUserApp().isInitialized
                && !mvvmData.getSystemApp().isInitialized) {

                Thread(Runnable {

                    val pm: PackageManager? = context.packageManager
                    val applicationInfos: MutableList<ApplicationInfo>? =
                        pm?.getInstalledApplications(PackageManager.GET_META_DATA)

                    val arrayListUser = ArrayList<AppModel>()
                    val arrayListSystem = ArrayList<AppModel>()

                    if (applicationInfos != null) {

                        for (applicationInfo in applicationInfos) {
                            val file = File(applicationInfo.sourceDir)
                            val size = file.length()
                            if (isSystem(applicationInfo)) {
                                arrayListSystem.add(
                                    AppModel(
                                        size,
                                        "appName",
                                        applicationInfo.packageName,
                                        null,
                                        applicationInfo.sourceDir,
                                        applicationInfo
                                    )
                                )
                            } else {
                                arrayListUser.add(
                                    AppModel(
                                        size,
                                        "appName",
                                        applicationInfo.packageName,
                                        null,
                                        applicationInfo.sourceDir,
                                        applicationInfo
                                    )
                                )
                            }

                        }

                        Handler(Looper.getMainLooper()).post(Runnable {
                               if (!mvvmData.getUserApp().isInitialized
                                && !mvvmData.getSystemApp().isInitialized
                            ) {
                                mvvmData.setUserApp(arrayListUser)
                                mvvmData.setSystemApp(arrayListSystem)
                            }
                            /* emitter.onNext(arrayListSystem)*/
                            //  emitter.onComplete()
                        })


                    }

                }).start()
            }
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

}
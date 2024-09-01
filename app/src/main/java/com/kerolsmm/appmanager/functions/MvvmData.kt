package com.kerolsmm.appmanager.functions

import android.Manifest
import android.app.AppOpsManager
import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.fragments.BatteryFragment
import com.kerolsmm.appmanager.model.AppBatteryOther
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.phonecleaner.Activitys.App
import java.util.Calendar
import java.util.Collections
import java.util.TreeMap
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

class MvvmData() : ViewModel() {



    private var mUsageStatsManager: UsageStatsManager? = null
    private var userApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()
    private var systemApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()
    private var allApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()


    fun getUserApp () : LiveData<ArrayList<AppModel>> {
        return userApp
    }

    fun getSystemApp () : LiveData<ArrayList<AppModel>> {
        return systemApp
    }

    fun getAllApp () : LiveData<ArrayList<AppModel>> {
        return allApp
    }

    fun setUserApp (arrayList: ArrayList<AppModel>) {
        userApp.value = arrayList
    }
    fun setSystemApp (arrayList: ArrayList<AppModel>) {
        systemApp.value = arrayList
    }
    fun setAllApp (arrayList: ArrayList<AppModel>) {
        allApp.value = arrayList
    }

 /*   private fun setBattery (arrayList: ArrayList<App>?) {
        batteryApp.value = arrayList
    }

    fun setNetwork (arrayList: ArrayList<ApplicationInfo>) {
        networkApp.value = arrayList
    }
    fun getNetwork () : MutableLiveData<ArrayList<ApplicationInfo>> {
        return networkApp
    }
    fun getBattery (): MutableLiveData<ArrayList<App>?> {
        return batteryApp
    }*/


    }




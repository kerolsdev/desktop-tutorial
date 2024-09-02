package com.kerols.appmanager.functions

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AppDataUsageModel(var appName: String? = "0",
                        var packageName: String? = "0",
                        var totalDataUsage: String? = "0",
                        var sentMobile: Long = 0,
                        var sentWifi: Long = 0,
                        var receivedMobile: Long = 0,
                        var receivedWifi: Long = 0,
                        var mobileTotal: Float? = 0f,
                        var wifiTotal: Float? = 0f,
                        var uid: Int = 0,
                        var session: Int = 0,
                        var type: Int = 0,
                        var progress: Int = 0,
                        var isSystemApp: Boolean? = false,
                        var isAppsList: Boolean? = false,
                        var dataLimit: String? = "0",
                        var dataType: String? = "0",
                        /*var list: List<AppDataUsageModel>? = null*/) : Parcelable


/*constructor() : this()*/
    /*constructor(mAppName: String?, mTotalDataUsage: String?) : this() {
        appName = mAppName
        totalDataUsage = mTotalDataUsage
    }

    constructor(
        mAppName: String?,
        mTotalDataUsage: String?,
        mMobileTotal: Float?
    ) : this() {
        appName = mAppName
        totalDataUsage = mTotalDataUsage
        mobileTotal = mMobileTotal
    }

    constructor(mAppName: String?, mPackageName: String?, uid: Int, isSystemApp: Boolean?) : this() {
        appName = mAppName
        packageName = mPackageName
        this.uid = uid
        this.isSystemApp = isSystemApp
    }

    constructor(
        mAppName: String?,
        mPackageName: String?,
        uid: Int,
        isSystemApp: Boolean?,
        isAppsList: Boolean?
    ) : this() {
        appName = mAppName
        packageName = mPackageName
        this.uid = uid
        this.isSystemApp = isSystemApp
        this.isAppsList = isAppsList
    }*/

package com.kerols.appmanager.functions

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.health.connect.datatypes.AppInfo
import android.os.Build
import android.os.Process
import android.os.UserHandle
import android.os.UserManager
import android.os.storage.StorageManager
import android.text.format.Formatter
import android.util.Log
import androidx.annotation.RequiresApi
import com.kerols.appmanager.model.AppModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File


class QueryUserApp {


    fun  readByRxJava (observer : Observer<ArrayList<AppModel>>, context : Context)
    : Observable<ArrayList<AppModel>>   {


        val observable : Observable<ArrayList<AppModel>> = Observable.create<ArrayList<AppModel>> { emitter ->


                val arrayList = ArrayList<AppModel>()
                val pm: PackageManager? = context?.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val apps: MutableList<ResolveInfo>? = pm?.queryIntentActivities(intent, 0)
                for (app in apps ?: ArrayList()) {

                    val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                    val appStorageStats = storageStatsManager.queryStatsForPackage (
                        StorageManager.UUID_DEFAULT, app.activityInfo.applicationInfo.packageName,
                        Process.myUserHandle() )

                    if (!isSystemPackage(app)){
                        /* val file = File(applicationInfo.publicSourceDir)*/
                        val size = appStorageStats.appBytes + appStorageStats.dataBytes + appStorageStats.cacheBytes
                        val appName = pm?.getApplicationLabel(app.activityInfo.applicationInfo).toString()
                        arrayList.add(AppModel(size,appName,app.activityInfo.applicationInfo.packageName,null,app.activityInfo.applicationInfo.sourceDir))
                    }

                }
                emitter.onNext(arrayList)
                emitter.onComplete()


        }

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)


        return observable

    }


     fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
       return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

        private fun isSystemPackage(resolveInfo: ResolveInfo): Boolean {
            return resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }





}
package com.kerols.appmanager.functions

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import android.os.storage.StorageManager
import com.kerols.appmanager.model.AppModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class QuerySystemApp {

    @SuppressLint("QueryPermissionsNeeded")
    fun  readByRxJava (observer : Observer<ArrayList<AppModel>>, context : Context)
            : Observable<ArrayList<AppModel>> {


        val arrayList = ArrayList<AppModel>()
        val observable : Observable<ArrayList<AppModel>> = Observable.create<ArrayList<AppModel>> { emitter ->

            val pm: PackageManager? = context.packageManager
            val applicationInfos : MutableList<ApplicationInfo>? =
                pm?.getInstalledApplications(PackageManager.GET_META_DATA)


            if (applicationInfos != null) {

                for (applicationInfo in applicationInfos) {

                    val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager


                    val appStorageStats = storageStatsManager.queryStatsForPackage (
                        StorageManager.UUID_DEFAULT, applicationInfo.packageName,
                        Process.myUserHandle() )

                    if (isSystem(applicationInfo)){
                        val size = appStorageStats.appBytes + appStorageStats.dataBytes + appStorageStats.cacheBytes
                        val appName = pm?.getApplicationLabel(applicationInfo).toString()

                        arrayList.add(AppModel(size,appName,applicationInfo.packageName,null,applicationInfo.sourceDir))
                    }

                }

                emitter.onNext(arrayList)
                emitter.onComplete()

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



}
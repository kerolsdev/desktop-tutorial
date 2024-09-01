package com.kerolsmm.appmanager.functions

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
import com.kerolsmm.appmanager.model.AppModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File


class QueryUserApp {


    fun  readByRxJava (observer : Observer<ArrayList<AppModel>>, context : Context)
    : Observable<ArrayList<AppModel>>   {


        val observable : Observable<ArrayList<AppModel>> = Observable.create<ArrayList<AppModel>> { emitter ->

                val pm: PackageManager? = context.packageManager
                val arrayList = ArrayList<AppModel>()
                val applicationInfos : MutableList<ApplicationInfo>? =
                pm?.getInstalledApplications(PackageManager.GET_META_DATA)


            if (applicationInfos != null) {

                for (app in applicationInfos) {


                    if (!isSystem(app)) {
                        val file = File(app.sourceDir)
                        val size = file.length()
                        arrayList.add(
                            AppModel(
                                size,
                                "appName",
                                app.packageName,
                                null,
                                app.sourceDir,
                                app
                            )
                        )
                    }

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

       /* private fun isSystemPackage(resolveInfo: ResolveInfo): Boolean {
            return resolveInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }*/





}
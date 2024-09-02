package com.kerols.appmanager.functions

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.RemoteException
import android.util.Log
import com.kerols.appmanager.R
import com.kerols.appmanager.functions.NetworkStatsHelper.getAppMobileDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getAppWifiDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getDeletedAppsMobileDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getDeletedAppsWifiDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getDeviceMobileDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getDeviceWifiDataUsage
import com.kerols.appmanager.functions.NetworkStatsHelper.getTetheringDataUsage
import com.kerols.appmanager.functions.Values.SESSION_TODAY
import com.kerols.appmanager.functions.Values.TYPE_MOBILE_DATA
import com.kerols.appmanager.functions.Values.TYPE_WIFI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.ParseException
import java.util.Collections


class QueryAppNetwork {



    private var mUserAppsList: ArrayList<AppDataUsageModel> = ArrayList()
    private var mSystemAppsList: ArrayList<AppDataUsageModel> = ArrayList()


    private fun App( mContext : Context ,  session : Int,  type : Int ) {

        var sent = 0L
        val systemSent = 0L
        var received = 0L
        val systemReceived = 0L
        var totalSystemSent = 0L
        var totalSystemReceived = 0L
        var totalTetheringSent = 0L
        var totalTetheringReceived = 0L
        var totalDeletedAppsSent = 0L
        var totalDeletedAppsReceived = 0L
        var tetheringTotal = 0L
        var deletedAppsTotal = 0L


        var model: AppDataUsageModel? = null
        val pm: PackageManager? = mContext.packageManager
        val date = 1
        val list : ArrayList<ApplicationInfo> = pm?.getInstalledApplications(PackageManager.GET_META_DATA) as ArrayList<ApplicationInfo>

        for (currentData in list) {
            /*val icon =  currentData.loadIcon(pm)*/
            val appName = pm?.getApplicationLabel(currentData).toString()
            if (isSystem(applicationInfo = currentData)) {
                if (type == TYPE_MOBILE_DATA) {
                    try {
                        sent = getAppMobileDataUsage(mContext, currentData.uid, session).get(0)
                        received = getAppMobileDataUsage(mContext, currentData.uid, session).get(1)
                        totalSystemSent = totalSystemSent + sent
                        totalSystemReceived = totalSystemReceived + received
                        if (sent > 0 || received > 0) {
                            model = AppDataUsageModel()
                            model.appName = appName
                            model.packageName = currentData.packageName
                            model.uid = currentData.uid
                            model.sentMobile = sent
                            model.receivedMobile = received
                            model.session = session
                            model.type = attr.type
                            val total = sent + received
                            val deviceTotal: Long =
                                getDeviceMobileDataUsage(mContext, session, date).get(2)

                            // multiplied by 2 just to increase progress a bit.
                            val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                            var progressInt: Int
                            progressInt = progress?.toInt() ?: 0
                            model.progress = progressInt
                            mSystemAppsList.add(model)
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        sent = getAppWifiDataUsage(mContext, currentData.uid, session).get(0)
                        received = getAppWifiDataUsage(mContext, currentData.uid, session).get(1)
                        totalSystemSent = totalSystemSent + sent
                        totalSystemReceived = totalSystemReceived + received
                        if (sent > 0 || received > 0) {
                            model = AppDataUsageModel()
                            model.appName = appName
                            model.packageName = currentData.packageName
                            model.uid = currentData.uid
                            model.sentMobile = sent
                            model.receivedMobile = received
                            model.session = session
                            model.type = attr.type
                            val total = sent + received
                            val deviceTotal: Long = getDeviceWifiDataUsage(mContext, session).get(2)
                            val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                            var progressInt: Int
                            progressInt = progress?.toInt() ?: 0
                            model.progress = progressInt
                            mSystemAppsList.add(model)
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
            } else {
                if (!isSystem(applicationInfo = currentData)) {
                    if (type == TYPE_MOBILE_DATA) {
                        try {
                            sent = getAppMobileDataUsage(mContext, currentData.uid, session).get(0)
                            received =
                                getAppMobileDataUsage(mContext, currentData.uid, session).get(1)
                            if (sent > 0 || received > 0) {
                                model = AppDataUsageModel()
                                model.appName = appName
                                model.packageName = currentData.packageName
                                model.uid = currentData.uid
                                model.sentMobile = sent
                                model.receivedMobile = received
                                model.session = session
                                model.type = attr.type
                                val total = sent + received
                                val deviceTotal: Long =
                                    getDeviceMobileDataUsage(mContext, session, date).get(2)
                                val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                                var progressInt: Int
                                progressInt = progress?.toInt() ?: 0
                                model.progress = progressInt
                                mUserAppsList.add(model)
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                    } else {
                        try {
                            sent = getAppWifiDataUsage(mContext, currentData.uid, session).get(0)
                            received =
                                getAppWifiDataUsage(mContext, currentData.uid, session).get(1)
                            if (sent > 0 || received > 0) {
                                model = AppDataUsageModel()
                                model.appName = appName
                                model.packageName = currentData.packageName
                                model.uid = currentData.uid
                                model.sentMobile = sent
                                model.receivedMobile = received
                                model.session = session
                                model.type = attr.type
                                val total = sent + received
                                val deviceTotal: Long =
                                    getDeviceWifiDataUsage(mContext, session).get(2)
                                val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                                var progressInt: Int
                                progressInt = progress?.toInt() ?: 0
                                model.progress = progressInt
                                mUserAppsList.add(model)
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        model = AppDataUsageModel()
        model.appName = mContext.getString(R.string.label_system_apps)
        model.packageName = mContext.getString(R.string.package_system)
        model.sentMobile = totalSystemSent
        model.receivedMobile = totalSystemReceived
        model.session = session
        model.type = attr.type

        val total = totalSystemSent + totalSystemReceived

        var deviceTotal: Long? = null
        if (type == TYPE_MOBILE_DATA) {
            try {
                deviceTotal = getDeviceMobileDataUsage(mContext, session, date).get(2)
                val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                val progressInt: Int
                progressInt = progress?.toInt() ?: 0
                model.progress = progressInt
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        } else {
            try {
                deviceTotal = getDeviceWifiDataUsage(mContext, session)[2]
                val progress = total.toDouble() / deviceTotal.toDouble() * 100 * 2
                val progressInt: Int = progress?.toInt() ?: 0
                model.progress = progressInt
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

  /*      Log.e("TAG", "App:dsda ${deviceTotal!!}  ")*/
        if (deviceTotal!! > 0) {
            mUserAppsList.add(model)
        }

        try {
            if (type == TYPE_MOBILE_DATA) {
                totalTetheringSent = getTetheringDataUsage(mContext, session).get(0)
                totalTetheringReceived = getTetheringDataUsage(mContext, session).get(1)
                tetheringTotal = totalTetheringSent + totalTetheringReceived
                val tetheringProgress =
                    tetheringTotal.toDouble() / deviceTotal!!.toDouble() * 100 * 2
                val tetheringProgressInt: Int
                tetheringProgressInt = tetheringProgress?.toInt() ?: 0
                model = AppDataUsageModel()
                model.appName = mContext.getString(R.string.label_tethering)
                model.packageName = mContext.getString(R.string.package_tethering)
                model.sentMobile = totalTetheringSent
                model.receivedMobile = totalTetheringReceived
                model.session = session
                model.type = attr.type
                model.progress = tetheringProgressInt
                if (tetheringTotal > 0) {
                    mUserAppsList.add(model)
                }
                totalDeletedAppsSent = getDeletedAppsMobileDataUsage(mContext, session).get(0)
                totalDeletedAppsReceived = getDeletedAppsMobileDataUsage(mContext, session).get(1)
            } else {
                totalDeletedAppsSent = getDeletedAppsWifiDataUsage(mContext, session).get(0)
                totalDeletedAppsReceived = getDeletedAppsWifiDataUsage(mContext, session).get(1)
            }
            deletedAppsTotal = totalDeletedAppsSent + totalDeletedAppsReceived
            val deletedProgress = deletedAppsTotal.toDouble() / deviceTotal!!.toDouble() * 100 * 2
            val deletedProgressInt: Int
            deletedProgressInt = deletedProgress?.toInt() ?: 0
            model = AppDataUsageModel()
            model.appName = mContext.getString(R.string.label_removed)
            model.packageName = mContext.getString(R.string.package_removed)
            model.sentMobile = totalDeletedAppsSent
            model.receivedMobile = totalDeletedAppsReceived
            model.session = session
            model.type = attr.type
            model.progress = deletedProgressInt
            if (deletedAppsTotal > 0) {
                mUserAppsList.add(model)
            }
            Collections.sort(mUserAppsList, object : Comparator<AppDataUsageModel?> {
              override  fun compare(o1: AppDataUsageModel?, o2: AppDataUsageModel?): Int {
                  if (o1 != null) {
                      o1.mobileTotal = (o1.sentMobile + o1.receivedMobile) / 1024f
                  }
                  if (o2 != null) {
                      o2.mobileTotal = (o2.sentMobile + o2.receivedMobile) / 1024f
                  }

                  return o1?.mobileTotal?.compareTo(o2?.mobileTotal!!)!!
                }
            })
            mUserAppsList.reverse()
            Collections.sort(mSystemAppsList, object : Comparator<AppDataUsageModel?> {
               override fun compare(o1: AppDataUsageModel?, o2: AppDataUsageModel?): Int {
                   if (o1 != null) {
                       o1.mobileTotal = (o1.sentMobile + o1.receivedMobile) / 1024f
                   }
                   if (o2 != null) {
                       o2.mobileTotal = (o2.sentMobile + o2.receivedMobile) / 1024f
                   }
                    return o1?.mobileTotal?.compareTo(o2?.mobileTotal!!)!!
                }
            })
            mSystemAppsList.reverse()
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
}




    fun appNetWork (observer : Observer<HashMap<String,ArrayList<AppDataUsageModel>>>
                    , context : Context , session: Int , type: Int) {

           val observable : Observable<HashMap<String,ArrayList<AppDataUsageModel>>> =
           Observable.create<HashMap<String,ArrayList<AppDataUsageModel>>> { emitter ->

               try {

               if (mSystemAppsList.size > 0) {
                   mSystemAppsList.clear()
               }

               if (mUserAppsList.size > 0) {
                   mUserAppsList.clear()
               }

               App(context, session, type)
               val map : HashMap <String,ArrayList<AppDataUsageModel>> = HashMap()
               map["system"] = mSystemAppsList
               map["user"] = mUserAppsList
               emitter.onNext(map)
               emitter.onComplete()

         }catch (re : RuntimeException) {

             }
       }
             observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)


   }

    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


}
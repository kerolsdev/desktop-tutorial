package com.kerolsmm.appmanager.functions

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import com.kerolsmm.appmanager.model.AppModel

object AppPermissionInfo {
    fun getInstalledAppsPermissions(context: Context , queryApps: QueryApps) {
/*
        val appPermissionsList: MutableList<String> = ArrayList()
*/
        Thread(Runnable {
            val arrayList = ArrayList<AppModel>()
            val pm: PackageManager? = context?.packageManager
            val applicationInfos : MutableList<ApplicationInfo>? =
                pm?.getInstalledApplications(PackageManager.GET_META_DATA)
            for (app in applicationInfos ?: ArrayList()) {
            // Get app app's permissions

                // Add app name and permissions to the list
                Log.e("TAG", "getInstalledAppsPermissions: ${app.packageName} ", )
                if (isSystem(app)) {
                    queryApps.onChangeSystem(app)
                }else
                {
                    queryApps.onChange(app)
                }
        }

        queryApps.onFinish()
        }).start()
    }

    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun onFinish() : AppPermissionInfo {
     return AppPermissionInfo
    }

    private fun arrayToString(array: Array<String>): String {
        val result = StringBuilder()
        for (item in array) {
            result.append(item).append(", ")
        }
        return result.toString()
    }

    interface QueryApps {
        fun  onChange (packageInfo: ApplicationInfo)
        fun  onChangeSystem (packageInfo: ApplicationInfo)
        fun onFinish ()
    }

    /* fun getInstalledAppsPermissions(context: Context , queryApps: QueryApps) {
/*
        val appPermissionsList: MutableList<String> = ArrayList()
*/
        Thread(Runnable {
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        for (packageInfo in installedPackages) {
            // Get the app's permissions
            val permissions = packageInfo.requestedPermissions
            if (permissions != null) {
                // Add app name and permissions to the list
                queryApps.onChange(packageInfo)
              /*  appPermissionsList.add(
                    packageInfo.applicationInfo.loadLabel(packageManager)
                        .toString() + ": " + arrayToString(permissions)
                )*/
            }
        }

        queryApps.onFinish()
        }).start()
    }*/

}

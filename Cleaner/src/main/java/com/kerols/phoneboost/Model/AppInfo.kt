package com.kerols.phoneboost.Model

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.text.format.Formatter
import androidx.fragment.app.FragmentActivity
import com.kerols.phoneboost.Activitys.MainActivity.Companion.isSystemPackage
import java.io.File


class AppInfo(app_Size: String, app_title: String?, app_icon: Drawable, pack: String) {
    var app_title: String?
    var app_Size: String
    var App_icon: Drawable
    var pack: String
    var isChecked = false

    init {
        App_icon = app_icon
        this.app_title = app_title
        this.app_Size = app_Size
        this.pack = pack
    }



    companion object {
        fun getArray(system: Boolean, context: FragmentActivity?): ArrayList<AppInfo> {
            val appInfo = ArrayList<AppInfo>()
            val pm: PackageManager? = context?.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val apps: MutableList<ResolveInfo>? = pm?.queryIntentActivities(intent, 0)
            for (app in apps ?: ArrayList()) {
                val file: File = File(app.activityInfo.applicationInfo.publicSourceDir)
                val size = file.length()
                val icon: Drawable = app.activityInfo.applicationInfo.loadIcon(pm)
                val appName = pm?.getApplicationLabel(app.activityInfo.applicationInfo).toString()
                val sizeText = Formatter.formatFileSize(context, size)
                if (!app.activityInfo.applicationInfo.packageName.equals(context?.packageName)) {
                    if (isSystemPackage(app) && system) {
                        appInfo.add(
                            AppInfo(
                                sizeText,
                                appName,
                                icon,
                                app.activityInfo.applicationInfo.packageName
                            )
                        )
                    } else if (!isSystemPackage(app) && !system) {
                        appInfo.add(
                            AppInfo(
                                sizeText,
                                appName,
                                icon,
                                app.activityInfo.applicationInfo.packageName
                            )
                        )
                    }
                }
            }
            return appInfo
        }
    }
}
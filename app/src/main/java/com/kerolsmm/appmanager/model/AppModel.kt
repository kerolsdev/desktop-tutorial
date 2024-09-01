package com.kerolsmm.appmanager.model

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable

data  class AppModel(var sizeApp : Long = 0 ,
                     var appTitle : String = "" ,
                     var appPackage : String = "" ,
                     var appIcon : Drawable? = null,
                     var appSource : String = "",
                     var applicationInfo: ApplicationInfo? = null,
                     var date : String = "",
                     var isCheckBoolean : Boolean = false)

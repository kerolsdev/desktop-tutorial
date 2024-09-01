package com.kerolsmm.phonecleaner.Activitys

import android.graphics.drawable.Drawable

data class App(
    var appIcon: Drawable? =  null,
    var appName: String? = null,
    var usagePercentage: Int? = null,
    var usageDuration: String? = null,
    var pack : String? = null,
    var backGroundTime : String? = null,
    var screenTime : String? = null,
)
package com.kerolsmm.appmanager.model

data class AppModelPermission (
    var userApps : ArrayList<AppModel>? = null ,
    var systemApps : ArrayList<AppModel>? = null
)
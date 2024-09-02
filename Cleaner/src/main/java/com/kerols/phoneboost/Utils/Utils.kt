package com.kerols.phoneboost.Utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class Utils(context: Context)  {

    private  var sharedPreferences: SharedPreferences
    private  var editor : SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences("cleanapp",Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveCachedApps(value : Boolean) {
        editor.putBoolean("CachedApps",value)
        editor.apply()
    }

    fun saveThumb(value : Boolean) {
        editor.putBoolean("Thumb",value)
        editor.apply()
    }

    fun saveMedia(value : Boolean) {
        editor.putBoolean("media",value)
        editor.apply()
    }
    fun getMedia() : Boolean {
        return  sharedPreferences.getBoolean("media",false)
    }

    fun saveApps(value : Boolean) {
        editor.putBoolean("apps",value)
        editor.apply()
    }


    fun getApps ()  : Boolean {
        return  sharedPreferences.getBoolean("apps",false)
    }


    fun saveReview() {
        editor.putBoolean("review",false)
        editor.apply()
    }

    fun getReview() :  Boolean {
        return sharedPreferences.getBoolean("review",true)
    }

    fun getCachedApps( ) : Boolean {
        return  sharedPreferences.getBoolean("CachedApps",false)
    }
    fun getThumb() : Boolean {
        return  sharedPreferences.getBoolean("Thumb",false)
    }


    fun saveApk(value : Boolean) {
        editor.putBoolean("apk",value)
        editor.apply()
    }

    fun saveCorpse(value : Boolean) {
        editor.putBoolean("corpse",value)
        editor.apply()
    }

    fun saveJunks (value : Boolean) {
        editor.putBoolean("cache",value)
        editor.apply()
    }

    fun saveEmpty(value : Boolean) {
        editor.putBoolean("empty",value)
        editor.apply()
    }

    fun getEmpty ()  : Boolean {
        return  sharedPreferences.getBoolean("empty", true)
    }
    fun getJunks ()  : Boolean {
        return  sharedPreferences.getBoolean("cache",true)
    }
    fun getCorpse ()  : Boolean {
        return  sharedPreferences.getBoolean("corpse",true)
    }
    fun getApk ()  : Boolean {
        return  sharedPreferences.getBoolean("apk",true)
    }


    fun saveVisibleCached (value : Boolean) {
        editor.putBoolean("VisibleCached",value)
        editor.apply()
    }

    fun getVisibleCached () : Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            sharedPreferences.getBoolean("VisibleCached",false)
        }else {
            sharedPreferences.getBoolean("VisibleCached",true)

        }

    }

}
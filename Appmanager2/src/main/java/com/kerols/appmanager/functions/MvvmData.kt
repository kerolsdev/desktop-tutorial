package com.kerols.appmanager.functions

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kerols.appmanager.model.AppModel

class MvvmData : ViewModel() {

    var userApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()
    var systemApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()
    var allApp : MutableLiveData<ArrayList<AppModel>> = MutableLiveData()


        fun getUserApp () : LiveData<ArrayList<AppModel>> {
        return userApp
    }

    fun getSystemApp () : LiveData<ArrayList<AppModel>> {
        return systemApp
    }

    fun getAllApp () : LiveData<ArrayList<AppModel>> {
        return allApp
    }


    fun setUserApp (arrayList: ArrayList<AppModel>) {
        userApp.value = arrayList
    }
    fun setSystemApp (arrayList: ArrayList<AppModel>) {
        systemApp.value = arrayList
    }
    fun setAllApp (arrayList: ArrayList<AppModel>) {
        allApp.value = arrayList
    }

}
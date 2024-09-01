package com.kerolsmm.appmanager.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kerolsmm.appmanager.functions.AppDataUsageModel
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.model.AppModelPermission
import com.kerolsmm.phonecleaner.Activitys.App

class MvvmBottomApp : ViewModel() {

    private var mutableLiveData : MutableLiveData<AppModel> = MutableLiveData()
    private var mutableLiveDataSystem : MutableLiveData<AppModel> = MutableLiveData()
    private var mutableLiveDataBattery : MutableLiveData<App> = MutableLiveData()
    private var mutableLiveDataNetwork : MutableLiveData<AppDataUsageModel> = MutableLiveData()
    private var mutableLiveDataPermission : MutableLiveData<AppModelPermission> = MutableLiveData()

    /*AppModel*/
    fun setValueAppModel (model: AppModel) {
        mutableLiveData.value = model
    }
    fun getValue () : MutableLiveData<AppModel> {
        return mutableLiveData
    }

    fun setValueAppModelSystem (model: AppModel) {
        mutableLiveDataSystem.value = model
    }
    fun getValueSystem () : MutableLiveData<AppModel> {
        return mutableLiveDataSystem
    }
    fun setValueAppModelNetwork (model: AppDataUsageModel) {
        mutableLiveDataNetwork.value = model
    }
    fun getValueNetwork () : MutableLiveData<AppDataUsageModel> {
        return mutableLiveDataNetwork
    }

    fun setValueAppModelBattery (model: App) {
        mutableLiveDataBattery.value = model
    }

    fun getValueBattery () : MutableLiveData<App> {
        return mutableLiveDataBattery
    }

    fun setValuePermission (model: AppModelPermission) {
        mutableLiveDataPermission.value = model
    }

    fun getValuePermission () : MutableLiveData<AppModelPermission> {
        return mutableLiveDataPermission
    }


}
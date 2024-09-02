package com.kerols.phoneboost.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentSearchViewModel : ViewModel() {

    private var mutableLiveData : MutableLiveData<String> = MutableLiveData()
    private var mutableLiveDataTwo : MutableLiveData<String> = MutableLiveData()

    fun setValue (text : String) {
        mutableLiveData.value = text
    }
    fun setValueTwo (text : String) {
        mutableLiveDataTwo.value = text
    }
    fun getValue() : MutableLiveData<String> {
        return  mutableLiveData
    }
    fun getValueTwo() : MutableLiveData<String> {
        return  mutableLiveDataTwo
    }

}
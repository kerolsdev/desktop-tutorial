package com.kerolsmm.incognito.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MvvmTab extends ViewModel {

    MutableLiveData<TabMvvmModel> arrayListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<UpdatePosition>mutableLiveData = new MutableLiveData<UpdatePosition>();
    MutableLiveData<String> DoAnyThing = new MutableLiveData<>();

    public MutableLiveData<TabMvvmModel> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public void setArrayListMutableLiveData(TabMvvmModel arrayList) {
        arrayListMutableLiveData.postValue(arrayList);
    }

    public void setMutableLiveData (UpdatePosition value) {
        mutableLiveData.postValue(value);
    }

    public MutableLiveData<UpdatePosition> getMutableLiveData() {
        return mutableLiveData;
    }


    public void setDoAnyThing(String  s) {
        DoAnyThing.postValue(s);
    }

    public MutableLiveData<String> getDoAnyThing() {
        return DoAnyThing;
    }
}

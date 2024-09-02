package com.kerolsmm.incognito.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomLiveData extends AndroidViewModel {
    private LiveData<List<BookMark>> task;
    public RoomLiveData(@NonNull Application application) {
        super(application);
        Data_name data_name = Data_name.getInstance(this.getApplication());
        task = data_name.kerols().mQuerybyId();
    }

    public LiveData<List<BookMark>> getTask() {
        return task;
    }
}

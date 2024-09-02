package com.kerolsmm.incognitopro.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomLiveDataDownlaod extends AndroidViewModel {
    LiveData<List<FileModel>> task;
    public RoomLiveDataDownlaod(@NonNull Application application) {
        super(application);
        Data_name data_name = Data_name.getInstance(this.getApplication());
        task = data_name.kerols().mQuerybyIdFile();
    }

    public LiveData<List<FileModel>> getTask() {
        return task;
    }
}

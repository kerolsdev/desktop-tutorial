package com.kerolsmm.appmanager.activities;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.google.android.material.color.DynamicColors;

public class AppAc extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
       // DynamicColors.applyToActivitiesIfAvailable(this);

    }
}

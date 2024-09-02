package com.kerolsmm.incognitopro.Activitys;


import static com.kerolsmm.incognitopro.DownloaderApp.NotificationDownload.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;



public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // Make sure to set the mediation provider value to "max" to ensure proper functionality

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Download";
            String descriptionText = "Download Progress";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        /*SharedPreferences sharedPreferences = getSharedPreferences("InCognitoAppPreferences",MODE_PRIVATE);
        if (sharedPreferences.getInt("InCognitoApp",111) != 1) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            defaultSharedPreferences.edit().clear().apply();
            sharedPreferences.edit().putInt("InCognitoApp",1).apply();
         //   Log.e("TAG", "onCreate:StartAppCleaner " );
        } else  {
           //  Log.e("TAG", "onCreate:FinishAppCleaner " );
          //   Toast.makeText(this, "App Per", Toast.LENGTH_SHORT).show();
        }*/


        /*AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
            }
        });*/

       // IntegrationHelper.validateIntegration(this);

     /*   IronSourceAdQuality.getInstance().initialize(this, "1c20b7f3d");

        IronSource.init(this, "1c20b7f3d", IronSource.AD_UNIT.BANNER);*/

    }

}

package com.kerolsmm.firewall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.HashSet;

public class OfflineVpnService extends VpnService {

    static boolean isRunning = false;

    private static final int FOREGROUND_ID   = 1;
    private static final String CHANNEL_ID = "ForcedOfflineNetApp";
    static final String APPS_LIST_PREFERENCE = "apps";
    static final String ACTION_START = "com.kerolsmm.net.action.START";
    static final String ACTION_STOP  = "com.kerolsmm.net.action.STOP";

    private ParcelFileDescriptor mVpn;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stop();
        } else {
            start();

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.antivirus_svgrepo_com)
                    .setContentTitle(getString(R.string.notification_text))
                    .addAction(R.drawable.ic_baseline_cancel_24, getString(R.string.action_stop),
                                   PendingIntent.getBroadcast(this, 0,
                                   new Intent(this, ActionReceiver.class).setAction(ACTION_STOP),
                                  PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentIntent(PendingIntent.getActivity(this, 0,
                            new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT))
                    .setShowWhen(false)
                    .setOngoing(true)
                    .build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(FOREGROUND_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE);
            }else  {
                startForeground(FOREGROUND_ID, notification);
            }
        }
        Log.e("TAG", "onStartCommand: " + "sss" );
        return START_NOT_STICKY;
    }

    @Override
    public void onRevoke() {
        stop();
        super.onRevoke();
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    private void start() {

        if(mVpn != null) {
            try {
                Log.e("TAG", "start: `"  + mVpn.getStatSize() );
                mVpn.close();
                isRunning = false;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        SharedPreferences prefs = getSharedPreferences("kerols",MODE_PRIVATE);

        Builder builder = new Builder();
        builder.setSession(getString(R.string.app_name));
        builder.setConfigureIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));

        /* ----- */
        builder.addAddress("10.1.10.1", 32);
        builder.addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128);

        builder.addRoute("0.0.0.0", 0);
        builder.addRoute("0:0:0:0:0:0:0:0", 0);
        /* ----- */

        if(prefs.getStringSet(APPS_LIST_PREFERENCE, new HashSet<>()).size() == 0) {
            try {
                builder.addAllowedApplication("pkg");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (String pkg : prefs.getStringSet(APPS_LIST_PREFERENCE, new HashSet<>())) {
            try {
                Log.e("TAG", "start: " + "kerolssssss" );
                builder.addAllowedApplication(pkg);
            } catch (PackageManager.NameNotFoundException ignore) {
                Log.e("TAG", "start: " + "java  aas" );
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false);
        }

        try {
            mVpn = builder.establish();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, R.string.failed_to_initialize_vpn, Toast.LENGTH_LONG).show();
            stop();
            return;
        }

        prefs.edit().putBoolean("running", true).apply();
        isRunning = true;
        sendBroadcast(new Intent(ACTION_START));
    }

    private void stop() {

        getSharedPreferences("kerols",MODE_PRIVATE).edit()
                .putBoolean("running", false).apply();

        if(mVpn == null) return;
        try {
            mVpn.close();
            mVpn = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        isRunning = false;

        stopForeground(true);
        stopSelf();

        sendBroadcast(new Intent(ACTION_STOP));
    }


    /* ----- */

    static boolean connect(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(getServiceIntent(context).setAction(ACTION_START));
        }else {
            context.startService(getServiceIntent(context).setAction(ACTION_START));
        }
        return true;
    }

    static void disconnect(Context context) {
        context.startService(getServiceIntent(context).setAction(ACTION_STOP));
    }

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, OfflineVpnService.class);
    }
}

  /*   if(PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(APPS_LIST_PREFERENCE, new HashSet<>()).size() == 0) {
            Toast.makeText(context, R.string.no_app_selected_to_run, Toast.LENGTH_LONG).show();
            return false;
        }*/
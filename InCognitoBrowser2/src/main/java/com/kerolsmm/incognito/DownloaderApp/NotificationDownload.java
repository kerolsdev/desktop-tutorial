package com.kerolsmm.incognito.DownloaderApp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kerolsmm.incognito.R;

public class NotificationDownload {


    private NotificationChannel channel;
    private NotificationCompat.Builder builder;
    public static final String CHANNEL_ID = "idDownload";

    // Function to create and show a notification

    private NotificationManager notificationManager;


    public NotificationDownload () {

    }

    public NotificationDownload (Context context) {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

   /*     NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_incognito_svgrepo_com__2_)
                .setContentTitle("Downloading...")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true);  // Don't play a sound every time

        // Show the notification
        Notification notification = builder.build();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, notification);*/

    }

    public  void createNotification(Context context) {
        // Create a NotificationChannel (for Android Oreo and later)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Download";
            String descriptionText = "Download Progress";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel  channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }*/

        try
        {
        // Create a notification
         builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_incognito_svgrepo_com__2_)
                .setContentTitle("Downloading...")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true);  // Don't play a sound every time

        // Show the notification
        Notification notification = builder.build();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
        notificationManager.notify(1, notification);

        }catch (RuntimeException r) {

        }
    }

    // Function to update the notification with progress
    public  void updateNotification(Context context, int progress) {
        try {
            if (progress > 0) {

                builder
                     //   .setContentText("Download in progress: " + progress + " % ")
                        .setProgress(100, progress, false);


            } else  {

                builder
                       // .setContentText("Download in progress: " + 0 + " % ")
                        .setProgress(100, progress, false);
            }

        // Update the notification
        Notification notification = builder.build();
       /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
            notificationManager.notify(1,notification);
        }catch (RuntimeException runtimeException) {

        }
    }

    public  void completeDownload(Context context , String fileName) {
        // Create a NotificationChannel (for Android Oreo and later)
        try {
            // Create a notification
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_incognito_svgrepo_com__2_)
                    .setContentTitle("Complete Download...")
                    .setContentText("Download in Complete: ")
                    .setContentText(fileName)
                    .setOngoing(false)
                    .setProgress(0, 0, false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOnlyAlertOnce(true);  // Don't play a sound every time

            // Show the notification
            Notification notification = builder.build();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, notification);
        }catch (RuntimeException runtimeException) {

        }
    }
    public  void Cancel(Context context) {
        // Create a NotificationChannel (for Android Oreo and later)

        try {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.cancelAll();


        }catch (RuntimeException runtimeException) {

        }
    }

}

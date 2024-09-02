package com.kerolsmm.incognito.DownloaderApp;

import static com.kerolsmm.incognito.DownloaderApp.NotificationDownload.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.kerolsmm.incognito.Data.Data_name;
import com.kerolsmm.incognito.Data.FileModel;
import com.kerolsmm.incognito.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class DownloaderService extends Service {

    PublishSubject<DownloadModel> publishSubject;
    public ArrayList<DownloadModel> downloadModel = new ArrayList<>();
    public ArrayList<DownloadItem> downloadItems = new ArrayList<>();
    Disposable disposable ;
    boolean isStop = false;

    NotificationDownload notificationDownload2;

    public FileDownloader fileDownloader ;
    Data_name dataName;
    NotificationCompat.Builder builder ;
    boolean isDestroy = false;
    int i = 0;
    private final IBinder localBinder = new MyBinder();

    MutableLiveData<ArrayList<DownloadItem>> mutableLiveData = new MutableLiveData<ArrayList<DownloadItem>>();
    MutableLiveData<DownloadItem> ItemDownloader = new MutableLiveData<DownloadItem>();
    MutableLiveData<Integer> updateProgress = new MutableLiveData <Integer>();


    NotificationManager notificationManager ;


    public boolean isRuing = false;

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent snoozeIntent = new Intent(this, DownloaderService.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("kerols", 1);
        PendingIntent snoozePendingIntent =
                PendingIntent.getService(this, 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_incognito_svgrepo_com__2_)
                .setContentTitle("Downloading...")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .addAction(0, "Stop", snoozePendingIntent)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setOnlyAlertOnce(true);

        try {
        publishSubject = PublishSubject.create();
        fileDownloader = new FileDownloader();
        notificationDownload2 = new NotificationDownload(getApplicationContext());
        dataName = Data_name.getInstance(getApplicationContext());


    }catch (RuntimeException runtimeException ) {

    }
        DisposableObserver<DownloadModel> disposableObserver = new DisposableObserver<DownloadModel>() {
            @Override
            public void onNext(DownloadModel value) {
                // Handle the emitted value
                downloadModel.add(value);
                i = 0;
                while (i < downloadModel.size()) {


                 //  Log.e("TAG", "onNext:sdsd " + downloadModel.size() );

                             fileDownloader.startWithMime(downloadModel.get(0).url, downloadModel.get(0).mimeType
                            , downloadModel.get(0).attachment,downloadModel.get(0).fileName
                            , new FileDownloadListener() {
                        @Override
                        public void onStart() {
                            isDestroy = false;
                         //   notificationDownload.createNotification(getApplicationContext());
                            if (downloadItems.size() > 0) {
                                setItemDownloader(new DownloadItem(downloadModel.get(0).fileName,0));
                                downloadItems.remove(0);
                                setMutableLiveData(new ArrayList<>(downloadItems));
                            }
                        }

                    @Override
                     public void onStop(File file) {

                      if (file.exists()) {
                          file.delete();
                      }
                       /* if ( downloadModel.size() > 0) {downloadModel.remove(0);}*/
                      //  notificationDownload.Cancel(getApplicationContext());
                        fileDownloader.stop(false);

                    }

                       @Override
                        public void onProgress(int progress) {
                        /*    if (isDestroy){
                              //  notificationDownload.Cancel(getApplicationContext());
                            } else {*/
                                setItemDownloaderUpdate(progress);
                             //   notificationDownload.updateNotification(getApplicationContext(), progress);
                                if (progress > 0) {

                                    builder
                                            .setContentText("Download in progress: " + progress + " % ")
                                            .setProgress(100, progress, false);


                                } else  {

                                    builder
                                             .setContentText("Download in progress: " + 0 + " % ")
                                            .setProgress(100, progress, false);
                                }

                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                notificationManager.notify(1,builder.build());
                            //   startForeground(1,builder.build(),ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
                           }else {

                               notificationManager.notify(1,builder.build());

                              // startForeground(1,builder.build());
                           }
                           setItemDownloaderUpdate(progress);
                           // }
                        }

                        @Override
                        public void onComplete(@Nullable File filePath) {
                            isDestroy = true;

                            dataName.kerols().insertFile(new FileModel(filePath.getName(),downloadModel.get(0).url,filePath.getPath()));
                            scanFile(filePath.getPath());

                            if (!downloadModel.isEmpty()) {
                                downloadModel.remove(0);
                            }
                               // Thread.sleep(1500);
                               if (downloadModel.isEmpty()) {
                                   stopForeground(true);
                                   stopSelf();
                                   notificationDownload2.completeDownload(getApplicationContext(),filePath.getName());
                               } else  {
                                  // notificationDownload.completeDownload(getApplicationContext(),filePath.getName());
                               }

                            setMutableLiveData(downloadItems);
                            setItemDownloader(null);
                            fileDownloader.stop(false);

                        }

                        @Override
                        public void onError(@Nullable String errorMessage) {
                          //  Log.e("TAG", "onErrorss: " + "yes Error AppStartUp" );
                            // Thread.sleep(200);
                            // setMutableLiveData(downloadItems);
                            isDestroy = true;
                            setItemDownloader(null);
                            fileDownloader.stop(false);
                           // Log.e("TAG", "onErrorsdsadsa: " + downloadModel.size());
                            if (isStop) {

                                if (!downloadModel.isEmpty()) {
                                    downloadModel.clear();
                                }
                                stopForeground(true);
                                stopSelf();


                            }else  {

                            if (!downloadModel.isEmpty() && downloadModel.size() != 1) {
                                downloadModel.remove(0);
                            } else {
                                if (!downloadModel.isEmpty()) {downloadModel.remove(0);}
                                stopForeground(true);
                                stopSelf();
                               }

                            }
                          //  notificationDownload.Cancel(getApplicationContext());
                        }
                    });


                }

            }

            @Override
            public void onError(Throwable e) {
                // Handle errors
              //  System.err.println("Error: " + e.getMessage());
               // if (downloadItems.size() > 0) {downloadItems.remove(0);}
              //  if ( downloadModel.size() > 0) {downloadModel.remove(0);}
               // setMutableLiveData(downloadItems);
               // setItemDownloader(null);
                isDestroy = true;
               // notificationDownload.Cancel(getApplicationContext());
                stopForeground(true);
                stopSelf();
                isRuing = false;
            }

            @Override
            public void onComplete() {
              //  System.err.println("onComplete: ");
               // if (downloadItems.size() > 0) {downloadItems.remove(0);}
               // if ( downloadModel.size() > 0) {downloadModel.remove(0);}
               // setMutableLiveData(downloadItems);
               // setItemDownloader(null);
                isDestroy = true;
               // notificationDownload.Cancel(getApplicationContext());
                isRuing = false;

            }
        };

          disposable = publishSubject.observeOn(Schedulers.io()).subscribeWith(disposableObserver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRuing = true;
        isStop = false;

     /*   if (Objects.equals(intent.getAction(), "stop")) {
             fileDownloader.stop(true);
        }else {*/
        if (intent.hasExtra("kerols"))
        {
           // downloadModel.clear();
            isStop = true;
            fileDownloader.stop(true);

        }else {
            isStop = false;
            try {

                String mimeType = intent.getStringExtra("mimeType");
                String url = intent.getStringExtra("url");
                String attachment = intent.getStringExtra("attachment");
                String filename = intent.getStringExtra("filename");

 /*       Log.e("TAG", "onStartCommand: " + filename);
        Log.e("TAG", "onStartCommand: " + attachment);*/

                if (downloadModel.size() > 0) {
                    downloadModel.add(new DownloadModel(mimeType, url, attachment, filename));
                    downloadItems.add(new DownloadItem(filename, 0));
                    setMutableLiveData(downloadItems);
                } else {


                     // Don't play a sound every time

                    // Show the notification
                    Notification notification = builder.build();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
                    } else {
                        startForeground(1, notification);
                    }
                    publishSubject.onNext(new DownloadModel(mimeType, url, attachment, filename));
                    downloadItems.add(new DownloadItem(filename, 0));
                    setMutableLiveData(downloadItems);
                }

            } catch (RuntimeException runtimeException) {
                Log.e("TAG", "onStartCommand: ", runtimeException);
            }
            //   }
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
       isDestroy = true;

       isRuing = false;

        if (publishSubject != null) {
            publishSubject.onComplete();
        }
        if (disposable != null) {
            disposable.dispose();
        }


      //  notificationDownload.Cancel(getApplicationContext());

        }catch (RuntimeException runtimeException) {

        }
    }

    public class MyBinder extends Binder {

        public   DownloaderService getService() {
            return DownloaderService.this;

        }
    }
    public void setItemDownloader(DownloadItem itemDownloader) {
        ItemDownloader.postValue( itemDownloader );
    }

    public void setItemDownloaderUpdate(int progress) {
        updateProgress.postValue( progress );
    }


    public void setMutableLiveData(ArrayList<DownloadItem> arrayList) {
        this.mutableLiveData.postValue(arrayList);
    }

    public MutableLiveData<ArrayList<DownloadItem>> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<DownloadItem> getItemDownloader() {
        return ItemDownloader;
    }

    public MutableLiveData<Integer> getUpdateProgress() {
        return updateProgress;
    }
    private void scanFile(String path) {

        MediaScannerConnection.scanFile(this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                     //   Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}

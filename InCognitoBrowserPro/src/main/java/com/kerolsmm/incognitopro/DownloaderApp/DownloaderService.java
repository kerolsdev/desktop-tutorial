package com.kerolsmm.incognitopro.DownloaderApp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.kerolsmm.incognitopro.Data.Data_name;
import com.kerolsmm.incognitopro.Data.FileModel;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class DownloaderService extends Service {

    PublishSubject<DownloadModel> publishSubject;
    public ArrayList<DownloadModel> downloadModel = new ArrayList<>();
    public ArrayList<DownloadItem> downloadItems = new ArrayList<>();
    Disposable disposable ;

    NotificationDownload notificationDownload;

    public FileDownloader fileDownloader ;
    Data_name dataName;

    boolean isDestroy = false;
    int i = 0;
    private final IBinder localBinder = new MyBinder();

    MutableLiveData<ArrayList<DownloadItem>> mutableLiveData = new MutableLiveData<ArrayList<DownloadItem>>();
    MutableLiveData<DownloadItem> ItemDownloader = new MutableLiveData<DownloadItem>();
    MutableLiveData<Integer> updateProgress = new MutableLiveData <Integer>();


    public boolean isRuing = false;

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
try {


        publishSubject = PublishSubject.create();
        fileDownloader = new FileDownloader();
        notificationDownload = new NotificationDownload(getApplicationContext());
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
                            notificationDownload.createNotification(getApplicationContext());
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
                        notificationDownload.Cancel(getApplicationContext());
                        fileDownloader.stop(false);

                    }

                       @Override
                        public void onProgress(int progress) {
                            if (isDestroy){
                              //  notificationDownload.Cancel(getApplicationContext());
                            }else {
                                setItemDownloaderUpdate(progress);
                                notificationDownload.updateNotification(getApplicationContext(), progress);
                                setItemDownloaderUpdate(progress);
                            }
                        }

                        @Override
                        public void onComplete(@Nullable File filePath) {
                            isDestroy = true;

                            dataName.kerols().insertFile(new FileModel(filePath.getName(),downloadModel.get(0).url,filePath.getPath()));
                            scanFile(filePath.getPath());

                            if ( downloadModel.size() > 0) { downloadModel.remove(0); }

                            try {
                                Thread.sleep(1500);
                                notificationDownload.completeDownload(getApplicationContext(),filePath.getName());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            setMutableLiveData(downloadItems);
                            setItemDownloader(null);
                            fileDownloader.stop(false);

                        }

                        @Override
                        public void onError(@Nullable String errorMessage) {
                            if ( downloadModel.size() > 0) { downloadModel.remove(0); }
                            // Thread.sleep(200);
                            // setMutableLiveData(downloadItems);
                            isDestroy = true;
                            setItemDownloader(null);
                            fileDownloader.stop(false);
                            notificationDownload.Cancel(getApplicationContext());
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
                notificationDownload.Cancel(getApplicationContext());
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
                notificationDownload.Cancel(getApplicationContext());
                isRuing = false;

            }
        };

          disposable = publishSubject.observeOn(Schedulers.io()).subscribeWith(disposableObserver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRuing = true;

        try {


        String mimeType = intent.getStringExtra("mimeType");
        String url = intent.getStringExtra("url");
        String attachment = intent.getStringExtra("attachment");
        String filename = intent.getStringExtra("filename");

 /*       Log.e("TAG", "onStartCommand: " + filename);
        Log.e("TAG", "onStartCommand: " + attachment);*/

        if (downloadModel.size() > 0) {
             downloadModel.add(new DownloadModel(mimeType,url,attachment,filename));
             downloadItems.add(new DownloadItem(filename,0));
             setMutableLiveData(downloadItems);
        } else {
            publishSubject.onNext(new DownloadModel(mimeType,url,attachment,filename));
            downloadItems.add(new DownloadItem(filename,0));
            setMutableLiveData(downloadItems);
        }

    }catch (RuntimeException runtimeException) {
            Log.e("TAG", "onStartCommand: ", runtimeException);
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


        notificationDownload.Cancel(getApplicationContext());

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

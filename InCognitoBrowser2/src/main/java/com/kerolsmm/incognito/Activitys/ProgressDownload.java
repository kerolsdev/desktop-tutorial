package com.kerolsmm.incognito.Activitys;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.kerolsmm.incognito.DownloaderApp.DownloadItem;
import com.kerolsmm.incognito.DownloaderApp.DownloadModel;
import com.kerolsmm.incognito.DownloaderApp.DownloaderService;
import com.kerolsmm.incognito.DownloaderApp.FileDownloader;
import com.kerolsmm.incognito.DownloaderApp.ListAdapterDownload;
import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.databinding.ActivityProgressDownloadBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ProgressDownload extends AppCompatActivity implements ListAdapterDownload.OnClickVoid {

/*
    com.kerolsmm.incognito.databinding.FragmentProgressDownloadBinding binding;
*/
    private ServiceConnection boundServiceConnection;


    boolean isBound = false;

    FileDownloader fileDownloader;

    ListAdapterDownload listAdapterDownload;

    ArrayList<DownloadModel> downloadModels = new ArrayList<>();
    ArrayList<DownloadItem> downloadItems2 = new ArrayList<>();
    boolean isNullEmpty = false;

    private DownloaderService.MyBinder binderBridge;

    private ActivityProgressDownloadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.ToolbarDownload);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listAdapterDownload = new ListAdapterDownload(new ArrayList<DownloadItem>(), this);
        binding.list.setAdapter(listAdapterDownload);


        boundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                isBound = true;


                binderBridge = (DownloaderService.MyBinder) service;


                fileDownloader = binderBridge.getService().fileDownloader;

                binderBridge.getService().getItemDownloader().observe(ProgressDownload.this, new Observer<DownloadItem>() {
                    @Override
                    public void onChanged(DownloadItem downloadItem) {
                        if (downloadItem != null) {
                            binding.progress.setVisibility(View.VISIBLE);
                            binding.textDownloadName.setVisibility(View.VISIBLE);
                            binding.split.setVisibility(View.VISIBLE);

                            if (binderBridge.getService().downloadModel.size() > 0) {
                                binding.textEmpty.setVisibility(View.GONE);
                            }

                            binding.cancelDownload.setVisibility(View.VISIBLE);
                            binding.textDownloadName.setText(downloadItem.name);
                            binding.progress.setProgress(downloadItem.progress);
                        } else {

                            if (binderBridge.getService().downloadModel.size() == 0) {
                                binding.textEmpty.setVisibility(View.VISIBLE);
                                binding.list.setVisibility(View.GONE);
                            }

                            binding.cancelDownload.setVisibility(View.GONE);
                            binding.progress.setVisibility(View.GONE);
                            binding.textDownloadName.setVisibility(View.GONE);
                            binding.split.setVisibility(View.GONE);

                        }
                    }
                });

                binderBridge.getService().getUpdateProgress().observe(ProgressDownload.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer != null) {
                            binding.progress.setProgress(integer);
                        }
                    }
                });


                binderBridge.getService().getMutableLiveData().observe(ProgressDownload.this, new Observer<ArrayList<DownloadItem>>() {
                    @Override
                    public void onChanged(ArrayList<DownloadItem> downloadItems) {
                        if (downloadItems != null) {
                            downloadItems2 = binderBridge.getService().downloadItems;
                            downloadModels = binderBridge.getService().downloadModel;

                            if (downloadItems.size() > 0) {
                                listAdapterDownload.setDownloadItems(downloadItems);
                                binding.list.setVisibility(View.VISIBLE);
                                // listAdapterDownload.notifyDataSetChanged();
                                // binding.textEmpty.setVisibility(View.GONE);
                            } else {
                                listAdapterDownload.setDownloadItems(downloadItems);
                                binding.list.setVisibility(View.GONE);
                                // binding.textEmpty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.list.setVisibility(View.GONE);
                        }
                    }

                });


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };


        binding.cancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileDownloader.stop(true);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isBound) {
            Intent intent = new Intent(this, DownloaderService.class);
            bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClickPosition(int position) {

        try {

            downloadItems2.remove(position);
            downloadModels.remove(position + 1);
            listAdapterDownload.setDownloadItems(downloadItems2);

        } catch (RuntimeException runtimeException) {

        }

    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


}
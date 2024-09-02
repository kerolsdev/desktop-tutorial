package com.kerolsmm.incognito.DownloaderApp;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.databinding.FragmentProgressDownloadBinding;

import java.util.ArrayList;


public class FragmentProgressDownload extends Fragment implements ListAdapterDownload.OnClickVoid {


    boolean isBound  = false;

    FileDownloader fileDownloader;

    ListAdapterDownload listAdapterDownload;

    ArrayList<DownloadModel> downloadModels = new ArrayList<>();
    ArrayList<DownloadItem> downloadItems2 = new ArrayList<>();
    boolean isNullEmpty = false;

    private DownloaderService.MyBinder binderBridge ;

    public FragmentProgressDownload () {

    }

    FragmentProgressDownloadBinding binding;
    private  ServiceConnection boundServiceConnection ;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgressDownloadBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listAdapterDownload = new ListAdapterDownload(new ArrayList<DownloadItem>(),this);
        binding.list.setAdapter(listAdapterDownload);



        boundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                isBound = true;



                binderBridge = (DownloaderService.MyBinder) service ;


                fileDownloader = binderBridge.getService().fileDownloader ;

                binderBridge.getService().getItemDownloader().observe(getViewLifecycleOwner(), new Observer<DownloadItem>() {
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
                        }else  {

                            if (binderBridge.getService().downloadModel.size() == 0) {
                                binding.textEmpty.setVisibility(View.VISIBLE);
                            }
                            binding.cancelDownload.setVisibility(View.GONE);
                            binding.progress.setVisibility(View.GONE);
                            binding.textDownloadName.setVisibility(View.GONE);
                            binding.split.setVisibility(View.GONE);

                        }
                    }
                });

                binderBridge.getService().getUpdateProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer != null) {
                            binding.progress.setProgress(integer);
                        }
                    }
                });


                binderBridge.getService().getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<DownloadItem>>() {
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
                            } else  {
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
    public void onStart() {
        super.onStart();
        try {

       if (isMyServiceRunning(DownloaderService.class)) {
            Intent intent = new Intent(requireContext(), DownloaderService.class);
            requireActivity().bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
        }else  {
           Log.e("TAG", "onStart:No " );
       }

        }catch (RuntimeException runtimeException) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
        if (isBound) {
            isBound = false;
            requireActivity().unbindService(boundServiceConnection);
        }

        }catch (RuntimeException runtimeException) {

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClickPosition(int position) {

        try {

            downloadItems2.remove(position);
            downloadModels.remove(position + 1);
            listAdapterDownload.setDownloadItems(downloadItems2);

        }catch (RuntimeException runtimeException) {

        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
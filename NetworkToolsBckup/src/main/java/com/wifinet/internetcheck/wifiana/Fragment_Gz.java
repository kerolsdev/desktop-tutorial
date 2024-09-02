package com.wifinet.internetcheck.wifiana;


import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wifinet.internetcheck.R;

import java.util.ArrayList;

public class Fragment_Gz
        extends Fragment
{

    LevelDiagram24GHz levelDiagram;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gz_ana, container, false);
        levelDiagram = (LevelDiagram24GHz) view.findViewById(R.id.levelDiagram24GHz);
        liveDataApp mlive = new ViewModelProvider(getActivity()).get(liveDataApp.class);
        mlive.getScanResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ScanResult>>() {
            @Override
            public void onChanged(ArrayList<ScanResult> scanResults) {
                levelDiagram.updateDiagram(scanResults);
            }
        });


        return view;
    }

}
package com.wifinet.internetcheck.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.wifinet.internetcheck.Activity.HotspotScanActivity;
import com.wifinet.internetcheck.Activity.MainActivity;

import com.wifinet.internetcheck.ContactCheck.WifiApManager;
import com.wifinet.internetcheck.R;

import java.util.Objects;


public class HotsoptWifiFragment extends Fragment {
     private MaterialButton button;
     private  View view;
     String TAG = "Hotspot";
     public HotsoptWifiFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hotspot_scan,container,false);
         button = view.findViewById(R.id.button_hotspot);
         button.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("NewApi")
           @Override
           public void onClick(View v) {
               /*Check Hotspot Turn on or off*/
              if (new WifiApManager(Objects.requireNonNull(getActivity())).isWifiApEnabled()) {
                  Intent intent = new Intent(getActivity(), HotspotScanActivity.class);
                  getActivity().startActivity(intent);
                  getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
              }else {
                  MainActivity.toast(getActivity(),true,3);
              }

            }
        });return  view;
     }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }


}

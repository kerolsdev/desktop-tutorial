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
import com.wifinet.internetcheck.Activity.MainActivity;
import com.wifinet.internetcheck.Activity.WifiScanActivity;
import com.wifinet.internetcheck.R;

import java.util.Objects;

import static com.wifinet.internetcheck.ContactCheck.ConnectionDetector.isNetWorkWifiOn;

public class WifiScanFragment extends Fragment {
 private MaterialButton button;
 private View view;
 private String TAG = "WifiScanFragment";

   public WifiScanFragment(){

   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_wifi_scan,container,false);
        button = view.findViewById(R.id.button_wifi);
        button.setOnClickListener(new View.OnClickListener() {
             @SuppressLint("NewApi")
             @Override
             public void onClick(View v) {
                 /*Check Wifi Turn on or off*/
                 if (isNetWorkWifiOn(Objects.requireNonNull(getActivity()))) {
                     Intent intent = new Intent(getActivity(), WifiScanActivity.class);
                     getActivity().startActivity(intent);
                     getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                 }else{
                     MainActivity.toast(getActivity(),false,1);


                 }

             }
         });
        return  view;
   }
    @Override
    public void onPause() {
        super.onPause();
   }

    @Override
    public void onResume() {
        super.onResume();
   }
}

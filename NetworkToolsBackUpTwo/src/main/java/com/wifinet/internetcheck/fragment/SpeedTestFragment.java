
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
import com.wifinet.internetcheck.Activity.SpeedTestActivity;
import com.wifinet.internetcheck.ContactCheck.ConnectionDetector;
import com.wifinet.internetcheck.R;
public class SpeedTestFragment extends Fragment {
     public MaterialButton button;
     public  SpeedTestFragment () {

     }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_speedtest,container,false);
        button = view.findViewById(R.id.button_speedTest);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

/*Check Internet Turn on or off*/

              if (new ConnectionDetector(getActivity()).isConnectingToInternet()){
                  Intent intent = new Intent(getActivity(), SpeedTestActivity.class);
                  getActivity().startActivity(intent);
                  getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
              }else  {
                  MainActivity.toast(getActivity(),false,2);

              }
            }
        });
        return  view;
    }




}


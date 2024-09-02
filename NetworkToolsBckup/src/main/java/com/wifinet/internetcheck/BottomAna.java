package com.wifinet.internetcheck;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomAna extends BottomSheetDialogFragment {

    private ClipboardManager clipboard;
    private  ClipData clipData;
    String ssidString  = "";
    String  macString  = "";

    public BottomAna () {

    }


    public BottomAna (String ssidString , String macString) {
       this.ssidString = ssidString;
       this.macString = macString;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.bottom_ana,container,false);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        TextView mac = view.findViewById(R.id.text_mac_ana);
        TextView  ssid = view.findViewById(R.id.text_ssid_ana);
        if (ssidString.isEmpty()) {
            ssid.setText(getString(R.string.UnknownAna));
        }else {
            ssid.setText(ssidString);
        }
        mac.setText(macString);
        CardView mac_card = view.findViewById(R.id.mac_copy_ana);
        CardView ssid_card = view.findViewById(R.id.ssid_copy_ana);

        mac_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipData = ClipData.newPlainText("textmac", macString);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ssid_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipData = ClipData.newPlainText("textssid", ssidString);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}

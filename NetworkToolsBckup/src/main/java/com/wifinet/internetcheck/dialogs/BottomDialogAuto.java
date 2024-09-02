/*
package com.wifinet.internetcheck.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;
import java.util.List;

public class BottomDialogAuto  extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {



    private String mac;
    private ArrayList<String>  pins;
    DialogFullScreen dialogFullScreen;
    private String pin;



    public BottomDialogAuto (String mac , ArrayList<String> pins) {
        this.pins = pins;
        this.mac = mac;
    }


    public BottomDialogAuto () {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_dialog_wps_auto,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);
        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(BottomDialogAuto.this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(requireContext(),R.layout.simple_list, pins);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        MaterialButton btn_connect = view.findViewById(R.id.btn_connect);
        MaterialButton btn_cancel = view.findViewById(R.id.btn_cancel);
        dialogFullScreen = new DialogFullScreen();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                dialogFullScreen.show(getParentFragmentManager(),dialogFullScreen.getTag());

                WifiUtils.withContext(requireContext())
                        .connectWithWps(mac, pin)
                        .onConnectionWpsResult(BottomDialogAuto.this::checkResult)
                        .start();

            }
        });

    }

    public void checkResult(boolean isSuccess)
    {
        dialogFullScreen.dismiss();
        if (isSuccess)
            Toast.makeText(requireContext(), "Succeed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pin = pins.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
*/

/*
package com.wifinet.internetcheck.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.wifinet.internetcheck.R;

public class BottomDialogCustom extends BottomSheetDialogFragment {

    private String mac;

    public BottomDialogCustom (String mac) {

        this.mac = mac;
    }


    public BottomDialogCustom () {

    }


    DialogFullScreen dialogFullScreen;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_dialog_custom_wps,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        EditText editText = view.findViewById(R.id.edit);
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
                        .connectWithWps(mac, editText.getText().toString())
                        .onConnectionWpsResult(BottomDialogCustom.this::checkResult)
                        .start();

            }
        });

    }

    private void checkResult(boolean isSuccess)
    {
        dialogFullScreen.dismiss();
        if (isSuccess)
            Toast.makeText(requireContext(), "Succeed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
    }

}*/

package com.kerolsmm.incognitopro.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.Utilts.SettingValue;

public class QuickSettingDialog extends BottomSheetDialogFragment {


    WebView webView;
    private SettingValue settingValue;



    public QuickSettingDialog(WebView webView) {
        this.webView =  webView;
    }

    public QuickSettingDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        settingValue = new SettingValue(requireActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quick_setting_dialog,container,false);
    }


}

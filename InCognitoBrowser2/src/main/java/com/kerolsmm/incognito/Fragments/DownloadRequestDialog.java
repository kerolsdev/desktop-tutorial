package com.kerolsmm.incognito.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.Utilts.SettingValue;
import com.kerolsmm.incognito.Utilts.StaticValue;

public class DownloadRequestDialog extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.download_request_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        SettingValue settingValue = new SettingValue(requireActivity());
        MaterialSwitch materialSwitch = view.findViewById(R.id.switch_download_request);
        TextView download_request = view.findViewById(R.id.check_Download_state);
        ImageView imageView = view.findViewById(R.id.back_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        if (settingValue.isDownloadRequest()) {
            download_request.setText(getString(R.string.Allowed));
            materialSwitch.setChecked(true);
        }
        else  {
            download_request.setText(getString(R.string.Blocked));
            materialSwitch.setChecked(false);

        }
        materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setDownloadRequest(b);
                settingValue.getEditor().apply();
                if (b)
                {download_request.setText(getString(R.string.Allowed));}
                else
                {download_request.setText(getString(R.string.Blocked));}
                StaticValue.ChangeValue = true;
            }
        });
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
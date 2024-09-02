package com.kerolsmm.incognito.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.Utilts.SettingValue;
import com.kerolsmm.incognito.Utilts.StaticValue;

public class AccessibilityDialog  extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.accessibilitydialog, container, false);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        SettingValue settingValue = new SettingValue(requireContext());
        SeekBar seekBar = view.findViewById(R.id.Font_Seekbar);
        TextView font_reach = view.findViewById(R.id.font_reach);
        ImageView cancel = view.findViewById(R.id.back_icon);

        Button button = view.findViewById(R.id.btn_restert);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(100);
                StaticValue.ChangeValue = true;
                font_reach.setText("%" + 100);
                settingValue.setSeekbar(100);
                settingValue.getEditor().apply();
            }
        });

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               requireActivity().getSupportFragmentManager().popBackStack();
           }
       });

        seekBar.setProgress(settingValue.getSeekbarFont());
        font_reach.setText("%" + settingValue.getSeekbarFont());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
             /*   if (i != 100){StaticValue.ChangeValue = true;}else {StaticValue.ChangeValue = false;}*/
                StaticValue.ChangeValue = true;
                font_reach.setText("%" + i);
                settingValue.setSeekbar(i);
                settingValue.getEditor().apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


     }


    /**
     * The system calls this only when creating the layout in a dialog.
     */
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

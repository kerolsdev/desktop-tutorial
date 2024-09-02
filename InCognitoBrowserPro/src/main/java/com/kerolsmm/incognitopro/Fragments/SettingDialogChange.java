package com.kerolsmm.incognitopro.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.Utilts.SettingValue;

public class SettingDialogChange  extends BottomSheetDialogFragment {


    private SettingValue settingValue;
    private onChanged mOnChanged;
    private String value;


    public SettingDialogChange () {

    }


    public SettingDialogChange (onChanged mOnChanged ,String value) {
        this.mOnChanged = mOnChanged;
        this.value = value;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingValue = new SettingValue(requireActivity());
        return inflater.inflate(R.layout.item_setting,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        MaterialSwitch switchMaterial = view.findViewById(R.id.switchSetting);
        TextView textView = view.findViewById(R.id.check_state);
        ImageView textView1 = view.findViewById(R.id.Cancel_item_setting);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        switchMaterial.setText(value);
        switchMaterial.setChecked(settingValue.getValuePublic(value));
        if (settingValue.getValuePublic(value))
            textView.setText(getString(R.string.Allowed));
        else {textView.setText(R.string.Blocked);}

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                  settingValue.setValuePublic(value,b);
                  settingValue.getEditor().apply();
                 if (b) textView.setText(getString(R.string.Allowed)); else {textView.setText(R.string.Blocked);}
                 mOnChanged.onChecked(settingValue.getValuePublic(value));

            }

        });
    }

    public interface onChanged {
        void onChecked (boolean Check);
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });

        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

/*        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }*/
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }



}


package com.kerolsmm.incognitopro.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.kerolsmm.incognitopro.R;

public class DownloadRequstAfter extends DialogFragment {


    String FileName;
    String url;
    private onDownloadClick downloadClick;
    String path;

    public DownloadRequstAfter () {


    }

    public DownloadRequstAfter (String Filename , onDownloadClick downloadClick) {
        this.downloadClick = downloadClick;
        this.FileName = Filename;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setLayout( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return inflater.inflate(R.layout.download_request_bootm_dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        MaterialButton button  = view.findViewById(R.id.Download_for_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadClick.onClickForDownload();
                dismiss();
            }
        });

        ImageView cancel = view.findViewById(R.id.back_info_dialog_download);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView textView = view.findViewById(R.id.File_name_for_download);
        textView.setText(FileName);



    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen);;
    }


    public interface onDownloadClick {
        void onClickForDownload ();
    }



 /*   private void startDownload(String url, String filename) {
        if (!hasOrRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "null",
                3)) {
            return;
        }
        if (filename == null) {
            filename = URLUtil.guessFileName(url, null, null);
        }
        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(url));
        } catch (IllegalArgumentException e) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Can't Download URL")
                    .setMessage(url)
                    .setPositiveButton(Html.fromHtml("<font color='#006AFC'>ok</font>"),
                            (dialog1, which1) -> {
                            })
                    .show();
            return;
        }
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setTitle(filename);

        String cookie = CookieManager.getInstance().getCookie(url);
        if (cookie != null) {
            request.addRequestHeader("Cookie", cookie);
        }
        DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        assert dm != null;
        dm.enqueue(request);

    }

    boolean hasOrRequestPermission(String permission, String explanation, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (requireActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            return true;
        }
        if (explanation != null && shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.PermissionRequired))
                    .setMessage(getString(R.string.Permissionisrequiredtodownload))
                    .setPositiveButton(Html.fromHtml("<font color='#006AFC'>ok</font>"), (dialog, which) ->
                            ActivityCompat.requestPermissions(requireActivity(),new String[]{permission}, requestCode))
                    .show();
            return false;
        }
        ActivityCompat.requestPermissions(requireActivity(),new String[]{permission}, requestCode);
        return false;

    }*/



}

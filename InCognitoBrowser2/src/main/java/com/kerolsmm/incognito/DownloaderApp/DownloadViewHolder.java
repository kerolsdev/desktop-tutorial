package com.kerolsmm.incognito.DownloaderApp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kerolsmm.incognito.R;

public class DownloadViewHolder extends RecyclerView.ViewHolder {

    TextView downloadName;
    MaterialButton cancel;

    public DownloadViewHolder(View itemView) {
        super(itemView);
        downloadName = itemView.findViewById(R.id.textDownload);
        cancel = itemView.findViewById(R.id.cancelDownloaditem);
    }
}

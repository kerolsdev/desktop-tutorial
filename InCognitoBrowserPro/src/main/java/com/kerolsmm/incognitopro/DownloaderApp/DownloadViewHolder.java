package com.kerolsmm.incognitopro.DownloaderApp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kerolsmm.incognitopro.R;

public class DownloadViewHolder extends RecyclerView.ViewHolder {

    TextView downloadName;
    ImageView cancel;

    public DownloadViewHolder(View itemView) {
        super(itemView);
        downloadName = itemView.findViewById(R.id.textDownload);
        cancel = itemView.findViewById(R.id.cancelDownloaditem);
    }
}

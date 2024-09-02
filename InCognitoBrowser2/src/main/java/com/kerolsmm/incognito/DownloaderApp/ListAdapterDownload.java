package com.kerolsmm.incognito.DownloaderApp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kerolsmm.incognito.R;

import java.util.ArrayList;
import java.util.Objects;

public class ListAdapterDownload extends RecyclerView.Adapter<DownloadViewHolder> {

    ArrayList<DownloadItem> downloadItems;
    OnClickVoid onClickVoid;
    public ListAdapterDownload(ArrayList<DownloadItem> downloadItems, OnClickVoid onClickVoid) {
        this.downloadItems = downloadItems;
        this.onClickVoid = onClickVoid;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_layout_download,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, @SuppressLint("RecyclerView") int position) {
       holder.downloadName.setText(downloadItems.get(position).name);
       holder.cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onClickVoid.onClickPosition(position);
           }
       });
    }

    @Override
    public int getItemCount() {
        return downloadItems.size();
    }

    public void setDownloadItems(ArrayList<DownloadItem> downloadItems) {
        this.downloadItems = downloadItems;
        notifyDataSetChanged();
    }

    static class CustomDiffCallback extends DiffUtil.ItemCallback<DownloadItem> {

        @Override
        public boolean areItemsTheSame(@NonNull DownloadItem oldItem, @NonNull DownloadItem newItem) {
            return Objects.equals(oldItem.name, newItem.name);
        }

        @Override
        public boolean areContentsTheSame(@NonNull DownloadItem oldItem, @NonNull DownloadItem newItem) {
            return Objects.equals(oldItem, newItem);
        }
    }

   public interface OnClickVoid {
        void onClickPosition (int position) ;
    }

}

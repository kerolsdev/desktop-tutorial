package com.kerolsmm.incognitopro.Utilts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kerolsmm.incognitopro.Data.FileModel;
import com.kerolsmm.incognitopro.R;

import java.util.ArrayList;

public class FileAdapter extends ListAdapter<FileModel, FileAdapter.FileViewHolder> {

   /* private ArrayList<FileModel> arrayList;*/
    private Context context;
    private onClickDownload onclickdownload;



    public FileAdapter () {
        super(new SleepNightDiffCallback());

    }


    public FileAdapter(/*ArrayList<FileModel> arrayList,*/
                       Context context, onClickDownload onclickdownload) {

        super(new SleepNightDiffCallback());
     /*   this.arrayList = arrayList;*/
        this.context = context;
        this.onclickdownload = onclickdownload;
    }




    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_model,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
         FileModel model = getItem(position);
         holder.File_name.setText(model.getName());
         holder.File_size.setText(model.getUrl());
         holder.more.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Initializing the popup menu and giving the reference as current context
                 Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE_PopupMenu);
                 PopupMenu popupMenu = new PopupMenu(wrapper, view);

                 // Inflating popup menu from popup_menu.xml file
                 popupMenu.getMenuInflater().inflate(R.menu.more_download, popupMenu.getMenu());
                 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem menuItem) {
                         // Toast message on menu item clicked
                           onclickdownload.onPositionDownload(menuItem,model);

                         return true;
                     }
                 });
                 // Showing the popup menu
                 popupMenu.show();
             }
         });


    }

   /* @Override
    public int getItemCount() {
        return arrayList.size();
    }*/

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView File_name;
        private TextView File_size;
        private ImageView more;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            File_name = itemView.findViewById(R.id.FileName);
            File_size = itemView.findViewById(R.id.File_url);
            more = itemView.findViewById(R.id.more_download);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickdownload.onClickDownloadFile(getItem(getAdapterPosition()));
                }
            });
        }
    }
    public interface onClickDownload {
        void onPositionDownload (MenuItem Position,FileModel fileModel) ;
        void onClickDownloadFile (FileModel fileModel);
    }

    /*@SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<FileModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }*/

    static class SleepNightDiffCallback extends DiffUtil.ItemCallback<FileModel> {

        @Override
        public boolean areItemsTheSame(@NonNull FileModel oldItem, @NonNull FileModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull FileModel oldItem, @NonNull FileModel newItem) {
            return oldItem == newItem;
        }
    }

}

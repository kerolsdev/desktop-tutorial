package com.kerolsmm.incognito.Utilts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.kerolsmm.incognito.Data.Tab;
import com.kerolsmm.incognito.R;

import java.util.ArrayList;

public class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.TabsViewHolder> {

    private ArrayList<Tab> arrayList;
    private DeleteTabs deleteTabs;
    private Context context;
    private Bitmap bitmap;

    public TabsAdapter(ArrayList<Tab> arrayList, DeleteTabs deleteTabs, Context context, Bitmap bitmap) {
        this.arrayList = arrayList;
        this.deleteTabs = deleteTabs;
        this.context = context;
        this.bitmap = bitmap;
    }


    @NonNull
    @Override
    public TabsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TabsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tabs,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TabsViewHolder holder, @SuppressLint("RecyclerView") int position) {

// TEXTVIEW
      /*  if(holder.frameLayout.getParent() != null) {
            ((ViewGroup)holder.frameLayout.getParent()).removeAllViews(); // <- fix
        }*/

        try {

            if (arrayList.get(holder.getAdapterPosition()).getWebView().getUrl().equals("about:blank")){
                try{
                    Glide.with(context).load(bitmap).into(holder.frameLayout);
                }catch (RuntimeException runtimeException) {
                    Log.e("TAG", "onBindViewHolder: ", runtimeException);
                }

            }else {
                Glide.with(context).load(arrayList.get(holder.getAdapterPosition()).getWebView().getDrawingCache()).override(280,280).into(holder.frameLayout);

            }




        if (arrayList.get(holder.getAdapterPosition()).getWebView().getOriginalUrl().equals("about:blank")){
            holder.textView.setText(context.getString(R.string.HomePage));
        }else {
            holder.textView.setText(arrayList.get(holder.getAdapterPosition()).getWebView().getTitle());
        }

        if (arrayList.get(holder.getAdapterPosition()).isSelected()){
            holder.cardView.setStrokeColor(ContextCompat.getColor(context,R.color.ColorAct));
        }else {
            holder.cardView.setStrokeColor(ContextCompat.getColor(context,R.color.NoSelect));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    for (Tab item : arrayList){
                        item.setSelected(false);
                    }
                }finally {
                    deleteTabs.ClickChangePosition(holder.getAdapterPosition());

                }


            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* holder.frameLayout.removeView(arrayList.get(position).getWebView());*/
                deleteTabs.PositionTabs(holder.getAdapterPosition());
            }
        });
        }catch (RuntimeException runtimeException) {

        }
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }else  {
            return  0;
        }
    }

    public class TabsViewHolder extends RecyclerView.ViewHolder {
        ImageView cancel;
        TextView textView;
        ImageView frameLayout;
        MaterialCardView cardView;
        public TabsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Text_Url_search_option);
            cancel = itemView.findViewById(R.id.cancel_tabs_);
            frameLayout = itemView.findViewById(R.id.webview_tabs_dialog);
            cardView = itemView.findViewById(R.id.kerols);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<Tab> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }


   public interface DeleteTabs  {
        void PositionTabs (int Position);
        void ClickChangePosition (int Position);
    }
    /*public static Bitmap viewToImage(Context context,
                                     WebView viewToBeConverted) {
        int extraSpace = 2000; //because getContentHeight doesn't always return the full screen height.
        int height = viewToBeConverted.getContentHeight() + extraSpace;

        Bitmap viewBitmap = Bitmap.createBitmap(
                viewToBeConverted.getWidth(), height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(viewBitmap);
        viewToBeConverted.draw(canvas);//w ww . ja  va 2 s. c  o m

        //If the view is scrolled, cut off the top part that is off the screen.
        try {
            int scrollY = viewToBeConverted.getScrollY();

            if (scrollY > 0) {
                viewBitmap = Bitmap.createBitmap(viewBitmap, 0, scrollY,
                        viewToBeConverted.getWidth(), height - scrollY);
            }
        } catch (Exception ex) {

            Log.d("d","Could not remove top part of the webview image. ex: " + ex);
        }

        return viewBitmap;
    }*/



}


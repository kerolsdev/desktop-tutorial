package com.kerolsmm.incognitopro.Utilts;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kerolsmm.incognitopro.Data.BookMark;
import com.kerolsmm.incognitopro.R;

import java.util.ArrayList;

public class BookmarkAdapter  extends RecyclerView.Adapter<BookmarkAdapter.BookViewHolder> {

    ArrayList<BookMark> arrayList;
    Context context;
    onMoreBookmark onmorebookmark;
    public BookmarkAdapter (ArrayList<BookMark> arrayList , Context context , onMoreBookmark onmorebookmark) {
        this.arrayList = arrayList;
        this.context = context;
        this.onmorebookmark = onmorebookmark;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.site_model,
                  parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        BookMark bookMark = arrayList.get(position);
        holder.Title_site_url.setText(bookMark.getUrl());
        holder.Title_site_name.setText(bookMark.getTitle());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE_PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, view);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.more_bookmark, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        onmorebookmark.onMore(menuItem,bookMark);
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onmorebookmark.onClickBookmark(bookMark);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView Title_site_name ;
        TextView Title_site_url;
        ImageView imageView;
        ImageView more;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            Title_site_name = itemView.findViewById(R.id.Title_site_text);
            Title_site_url = itemView.findViewById(R.id.Title_site_url);
            imageView = itemView.findViewById(R.id.SiteIcon);
            more =  itemView.findViewById(R.id.more_site);

        }
    }

    public void setArrayList(ArrayList<BookMark> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

  public   interface onMoreBookmark {
        void onMore (MenuItem Position, BookMark bookMark);
        void onClickBookmark (BookMark bookMark);
    }


}

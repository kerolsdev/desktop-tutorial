package com.kerolsmm.incognitopro.Utilts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kerolsmm.incognitopro.R;

import java.util.ArrayList;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {


    private ArrayList<String>arrayList;
    private onClickSearch onclickSearch;

    public SearchAdapter (ArrayList<String> arrayList , onClickSearch onclickSearch ) {
        this.arrayList = arrayList;
        this.onclickSearch = onclickSearch;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.searchlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.TextSuggiestion.setText(arrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onclickSearch.onPosition(arrayList.get(position));
                }catch (RuntimeException runtimeException){

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView TextSuggiestion;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            TextSuggiestion = itemView.findViewById(R.id.TextSuggiestion);
        }
    }
   public interface onClickSearch {
        void onPosition (String search);
    }
}

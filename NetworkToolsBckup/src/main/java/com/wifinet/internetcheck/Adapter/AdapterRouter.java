package com.wifinet.internetcheck.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wifinet.internetcheck.R;

import java.util.ArrayList;

public class AdapterRouter  extends RecyclerView.Adapter<AdapterRouter.ViewHolderCustom> {

    ArrayList<Router> arrayList;

    public AdapterRouter(ArrayList<Router> arrayList) {

        this.arrayList = arrayList;
        
    }


    public AdapterRouter () {


    }


    @NonNull
    @Override
    public ViewHolderCustom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCustom(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCustom holder, int position) {
        Router router = arrayList.get(position);
        holder.password.setText(router.password);
        holder.username.setText(router.username);
        holder.brand.setText(router.brand);
        holder.model.setText(router.model);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderCustom extends RecyclerView.ViewHolder {
        TextView model ;
        TextView brand ;
        TextView username ;
        TextView password ;
        public ViewHolderCustom(@NonNull View itemView) {
            super(itemView);
            model = itemView.findViewById(R.id.model);
            brand = itemView.findViewById(R.id.brand);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);

        }
    }

    public void setArrayList(ArrayList<Router> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

}

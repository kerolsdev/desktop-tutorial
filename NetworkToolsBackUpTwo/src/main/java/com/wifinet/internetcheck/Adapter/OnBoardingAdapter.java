package com.wifinet.internetcheck.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wifinet.internetcheck.Model.ModelAdapterOnBoarding;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {
    ArrayList<ModelAdapterOnBoarding> arrayList ;
    public OnBoardingAdapter (ArrayList<ModelAdapterOnBoarding>arrayList){

        this.arrayList= arrayList;

    }


    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_screen,parent,false);
        return new OnBoardingViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {

        holder.method(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class OnBoardingViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
       private void method(ModelAdapterOnBoarding modelAdapterOnBoarding ){
           imageView.setImageResource(modelAdapterOnBoarding.image);
           textView.setText(modelAdapterOnBoarding.text);

        }
        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_onBoarding);
            textView = itemView.findViewById(R.id.text_onBoarding);


        }
    }
}

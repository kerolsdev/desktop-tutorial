package com.wifinet.internetcheck.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wifinet.internetcheck.BottomAlterDialog;
import com.wifinet.internetcheck.Model.ModelAdapterDevice;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;

public class DeviceWifiAdapter extends RecyclerView.Adapter<DeviceWifiAdapter.DeviceViewHolder> {
   private final ArrayList <ModelAdapterDevice> arrayList ;
   private final FragmentManager fragmentManager;
    public  DeviceWifiAdapter (ArrayList<ModelAdapterDevice> arrayList, FragmentManager fragmentManager) {

        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;


    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_device,parent,false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {

        ModelAdapterDevice modelAdapterDevice = arrayList.get(position);
        holder.ipAddress.setText(modelAdapterDevice.getDevice_Ip());
        holder.VendorDevice.setText(modelAdapterDevice.getDevice_vendor());
        holder.macAddress.setText(modelAdapterDevice.getDevice_mac());
        if(position == arrayList.size()-1){
            holder.relativeParams.setMargins(0,0,0,160);

        }else {
            holder.relativeParams.setMargins(0, 0, 0, 0);
        }
        if (modelAdapterDevice.getDevice_mac() == null || modelAdapterDevice.getDevice_mac().length() <= 1){
            holder.macAddress.setText("N/A");
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomAlterDialog bottomAlterDialog = new BottomAlterDialog(modelAdapterDevice);
                bottomAlterDialog.show(fragmentManager,bottomAlterDialog.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
       TextView ipAddress;
       TextView macAddress;
       TextView VendorDevice;
       ImageView imageView;
        RecyclerView.LayoutParams relativeParams ;
        SwipeRefreshLayout swipeRefreshLayout;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
           ipAddress = itemView.findViewById(R.id.ipAddress);
           macAddress = itemView.findViewById(R.id.macAddress);
           VendorDevice = itemView.findViewById(R.id.VendorDevice);
           imageView = itemView.findViewById(R.id.more);
           swipeRefreshLayout = itemView.findViewById(R.id.swiperefresh);

            relativeParams = (RecyclerView.LayoutParams)itemView.getLayoutParams();



        }
    }
}

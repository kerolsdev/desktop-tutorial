/*
 *  Copyright (C) 2014 Benjamin W. (bitbatzen@gmail.com)
 *
 *  This file is part of WLANScanner.
 *
 *  WLANScanner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WLANScanner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WLANScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wifinet.internetcheck.wifiana;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wifinet.internetcheck.BottomAna;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;


public class ArrayAdapterWLAN extends RecyclerView.Adapter<ArrayAdapterWLAN.ViewModelCustom> {

    Context context;
    
    ArrayList<ScanResult> data;

    private static LayoutInflater inflater = null;

    onClickAdapter onClAdapter;
    
    public ArrayAdapterWLAN(Context context, ArrayList<ScanResult> data , onClickAdapter onClAdapter) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClAdapter = onClAdapter;
    }

    @NonNull
    @Override
    public ViewModelCustom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewModelCustom(inflater.inflate(R.layout.row_item_wlan, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModelCustom holder, int position) {

        ScanResult itemData = data.get(position);

        if (TextUtils.isEmpty(itemData.SSID)){
            holder.ssidItem.setTextColor(ContextCompat.getColor(context, R.color.level));
            holder.ssidItem.setText("(Hidden Network)");
        }else {
            holder.ssidItem.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.ssidItem.setText(itemData.SSID);
        }

        holder.levelItem.setText(Integer.toString(itemData.level) + " dBm");
        holder.capabilitiesItem.setText(Util.getCapabilitiesString(itemData.capabilities));

        if (itemData.level >= -65) {
            holder.levelItem.setBackgroundResource(R.drawable.list_item_level_bg_green);
        }
        else if (itemData.level >= -85) {
            holder.levelItem.setBackgroundResource(R.drawable.list_item_level_bg_yellow);
        }
        else {
            holder.levelItem.setBackgroundResource(R.drawable.list_item_level_bg_red);
        }
        Util.FrequencyBand fBand = Util.getFrequencyBand(itemData);
        String text = "";
        if (fBand == Util.FrequencyBand.TWO_FOUR_GHZ) {
            text = "2.4 GHz";
        }
        else if (fBand == Util.FrequencyBand.FIVE_GHZ) {
            text = "5 GHz";
        }
        else if (fBand == Util.FrequencyBand.SIX_GHZ) {
            text = "6 GHz";
        }
        holder.bandItem.setText(text);
        String sWlanStandard = Util.getWLANStandard(itemData);
        if (sWlanStandard == "") {
            holder.wlanStandard.setVisibility(View.INVISIBLE);
        }
        else {
            holder.wlanStandard.setText(sWlanStandard);
        }
        holder.wlanStandard.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics()));

        // channel width
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            holder.channelWidthItem.setText(Util.getChannelWidth(itemData) + " MHz");
            holder.channelWidthItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics()));
        }

        // channel
        String channel = "";
        int[] frequencies = Util.getFrequencies(itemData);
        if (frequencies.length == 1) {
            channel = String.valueOf(Util.getChannel(frequencies[0]));
        }
        else if (frequencies.length == 2) {
            channel = Util.getChannel(frequencies[0]) + "+" + Util.getChannel(frequencies[1]);
        }
        holder.channelItem.setText(channel);
        holder.channelItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, context.getResources().getDisplayMetrics()));
        holder.bssidItem.setText(itemData.BSSID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClAdapter.itemViewClick(itemData.BSSID , itemData.SSID);
            }
        });


        // bssid (mac)
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewModelCustom extends RecyclerView.ViewHolder {
        TextView ssidItem ,capabilitiesItem , levelItem , bandItem , wlanStandard , channelItem , bssidItem , channelWidthItem;


        public ViewModelCustom(@NonNull View view) {
            super(view);

             channelWidthItem = (TextView) view.findViewById(R.id.rowItemChannelWidth);
            // ssid
             ssidItem = (TextView) view.findViewById(R.id.rowItemSSID);


            // capabilities
             capabilitiesItem = (TextView) view.findViewById(R.id.rowItemCapabilities);

            // level
             levelItem = (TextView) view.findViewById(R.id.rowItemLevel);


            // frequency band
             bandItem = (TextView) view.findViewById(R.id.rowItemFrequencyBand);

            // wlan standard
             wlanStandard = (TextView) view.findViewById(R.id.rowItemWLANStandard);

             channelItem = (TextView) view.findViewById(R.id.rowItemChannel);

             bssidItem = (TextView) view.findViewById(R.id.rowItemBSSID);

        }
    }

 public  interface onClickAdapter {
        void itemViewClick (String mac , String ssid);
    }
}
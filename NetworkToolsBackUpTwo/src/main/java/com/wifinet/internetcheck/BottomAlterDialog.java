package com.wifinet.internetcheck;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.wifinet.internetcheck.Model.ModelAdapterDevice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.MY_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.OTHER_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.ROUTER;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.WIFI;
import static com.wifinet.internetcheck.My_DeviceInfo.getIpRouter;

public class BottomAlterDialog  extends BottomSheetDialogFragment implements View.OnClickListener {

  ModelAdapterDevice modelAdapterDevice;
  TextView text_ip,text_mac,text_vendor,text_Device_owner;
  MaterialButton btn_cancel,btn_block ;
  CardView ip_copy,mac_copy,vendor_copy;
  private ClipboardManager clipboard;
  private  ClipData clipData;
  String url;

  public  BottomAlterDialog () {


  }


  public BottomAlterDialog (ModelAdapterDevice modelAdapterDevice) {
      this.modelAdapterDevice = modelAdapterDevice; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog,container,false);
        String term = getIpRouter(requireActivity());
        url = term;
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (URLIsReachable("http://" + term)){

                   url = "http://" + term;
                    Log.e("tag", "run: " + url );

                }else {

                    url = "https://" + term;
                    Log.e("tag", "run: " + url );

                }
            }
        }).start();

       text_ip = view.findViewById(R.id.text_ip);
       text_mac =view.findViewById(R.id.text_mac);
       text_vendor = view.findViewById(R.id.text_vendor);
       if (modelAdapterDevice.getDevice_mac() != null) {
           if (!modelAdapterDevice.getDevice_mac().isEmpty()) {
               text_mac.setText(modelAdapterDevice.getDevice_mac());
           }else {
               text_mac.setText("N/A");
           }
       }  else  {
           text_mac.setText("N/A");
       }

        if (modelAdapterDevice.getDevice_vendor() != null ){
            if (!modelAdapterDevice.getDevice_vendor().isEmpty()){
                text_vendor.setText(modelAdapterDevice.getDevice_vendor());

            }else {
                text_vendor.setText("N/A");

            }
        }else  {
            text_vendor.setText("N/A");

        }
       text_ip.setText(modelAdapterDevice.getDevice_Ip());
       btn_block = view.findViewById(R.id.btn_block);
       btn_cancel = view.findViewById(R.id.btn_cancel);
       btn_cancel.setOnClickListener(this);
       btn_block.setOnClickListener(this);
       ip_copy = view.findViewById(R.id.ip_copy);
       mac_copy = view.findViewById(R.id.mac_copy);
       vendor_copy = view.findViewById(R.id.vendor_copy);
       vendor_copy.setOnClickListener(this);
       mac_copy.setOnClickListener(this);
       ip_copy.setOnClickListener(this);
       clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
       text_Device_owner = view.findViewById(R.id.device_owner);

       switch (modelAdapterDevice.getDevice_owner()){
           case MY_DEVICE :
         text_Device_owner.setText(getString(R.string.Your_device));
           btn_block.setVisibility(View.GONE);
         break;
           case ROUTER :
               text_Device_owner.setText(getString(R.string.The_router));
               btn_block.setVisibility(View.GONE);
               break;
           case OTHER_DEVICE :
               text_Device_owner.setText(getString(R.string.Unknown));
               btn_block.setVisibility(View.VISIBLE);
               break;
           default:
               throw new NullPointerException("return value is null at method Device_Owner");



       }

       return view;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
            /*Block Device by Enter on The Site Router*/
        } else if (id == R.id.btn_block) {
            if (modelAdapterDevice.getWifi().equals(WIFI)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    String term = getIpRouter(requireActivity());   // term which you want to search for
                    intent.putExtra(SearchManager.QUERY, url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, getString(R.string.to_bloc)));
                    dismiss();
                } catch (ActivityNotFoundException activityNotFoundException) {

                }
            } else {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dismiss();
            }

            /*Copy ip*/
        } else if (id == R.id.ip_copy) {
            clipData = ClipData.newPlainText("text", modelAdapterDevice.getDevice_Ip());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getActivity(), "Copied : " + modelAdapterDevice.getDevice_Ip(), Toast.LENGTH_SHORT).show();
            /*Copy mac*/
        } else if (id == R.id.mac_copy) {
            clipData = ClipData.newPlainText("text", modelAdapterDevice.getDevice_mac());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getActivity(), "Copied : " + modelAdapterDevice.getDevice_mac(), Toast.LENGTH_SHORT).show();

            /*Copy Vendor*/
        } else if (id == R.id.vendor_copy) {
            clipData = ClipData.newPlainText("text", modelAdapterDevice.getDevice_vendor());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            Toast.makeText(getActivity(), "Copied : " + modelAdapterDevice.getDevice_vendor(), Toast.LENGTH_SHORT).show();
        } else {
            throw new NullPointerException("return value is null at method BottomDialog");
        }
    }

    public boolean URLIsReachable(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
           int responseCode = urlConnection.getResponseCode();
            urlConnection.disconnect();
            return responseCode != 200;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

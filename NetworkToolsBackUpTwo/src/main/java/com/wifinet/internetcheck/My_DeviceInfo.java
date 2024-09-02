package com.wifinet.internetcheck;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.Formatter;

import androidx.annotation.RequiresApi;

import com.wifinet.internetcheck.Model.ModelAdapterDevice;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.MY_DEVICE;
import static com.wifinet.internetcheck.Model.ModelAdapterDevice.WIFI;

public class My_DeviceInfo {


    public My_DeviceInfo (){

    }

    public static ModelAdapterDevice modelAdapterDevice (String ip) {
        StringBuilder res1 = new StringBuilder();
        return new ModelAdapterDevice(macAddress(res1),getDeviceName(),ip,MY_DEVICE,WIFI);
    }

    public static String macAddress (StringBuilder res1){


        try {
            // get all the interfaces
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            //find network interface wlan0
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
                //get the hardware address (MAC) of the interface
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes == null) {
                    return "00:00:00:00";
                }


                res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //gets the last byte of b
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res1.toString();
    }
    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    public static String  getIpRouter(Context context){

        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final DhcpInfo dhcp = wm.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);
        return address;

    }
}

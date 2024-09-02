package com.wifinet.internetcheck.Model;

import android.bluetooth.BluetoothClass;

public class ModelAdapterDevice {

     public static final int   MY_DEVICE = 1;
     public static final String HOTSPOT = "HOTSPOT";
     public static  final  String WIFI = "WIFI";
     public static  final int  ROUTER = 2 ;
     public  static final   int OTHER_DEVICE = 3;
    private  String Device_mac;
    private  String Device_vendor;
    private  String Device_Ip;
    private  int Device_owner;
    private String Wifi ;

    public ModelAdapterDevice (String Device_mac , String Device_vendor, String Device_Ip,int Device_owner,String Wifi){
        this.Wifi = Wifi;
        this.Device_Ip = Device_Ip;
        this.Device_mac = Device_mac;
        this.Device_vendor = Device_vendor;
        this.Device_owner = Device_owner;

    }

    public String getDevice_Ip() {
        return Device_Ip;
    }

    public String getDevice_mac() {
        return Device_mac;
    }

    public int getDevice_owner() {
        return Device_owner;
    }

    public void setDevice_owner(int device_owner) {
        Device_owner = device_owner;
    }

    public String getDevice_vendor() {
        return Device_vendor;
    }

    public void setDevice_Ip(String device_Ip) {
        Device_Ip = device_Ip;
    }

    public void setDevice_mac(String device_mac) {
        Device_mac = device_mac;
    }

    public String getWifi() {
        return Wifi;
    }

    public void setDevice_vendor(String device_vendor) {
        Device_vendor = device_vendor;
    }
}

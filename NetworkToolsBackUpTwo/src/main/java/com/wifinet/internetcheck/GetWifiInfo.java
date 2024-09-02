package com.wifinet.internetcheck;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.stealthcopter.networktools.ARPInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Looper.getMainLooper;

import org.json.JSONException;
import org.json.JSONObject;

public class GetWifiInfo {

    WifiManager mWifiManager;
    WifiInfo mWifiInfo ;
    DhcpInfo mDhcpInfo;
    Handler mainHandler = new Handler(getMainLooper());
    String ip;


    public GetWifiInfo (Context context) {

        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        mDhcpInfo = mWifiManager.getDhcpInfo();

    }

    public String getDns1 () {

        return  intToIp(mDhcpInfo.dns1);

    }

    public String getDns2 () {

        return intToIp(mDhcpInfo.dns2);


    }

    public String getIpAddressWifi () {


        return intToIp(mDhcpInfo.gateway);


    }

    public String getMacAddressWifi () {

        if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            return ARPInfo.getMACFromIPAddress(getIpAddressWifi());
        }else {
            return mWifiManager.getConnectionInfo().getBSSID();
        }

    }

    public void publicIpAddress (TextView textView) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder result = new StringBuilder();
                    URL url = new URL("https://api.ipify.org/");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
                    conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();
                    String finalLine = result.toString();

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(finalLine);
                            ip = finalLine;

                        }
                    });

                } catch (ProtocolException e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("0.0.0.0");
                        }
                    });
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("0.0.0.0");
                        }
                    });
                    e.printStackTrace();
                } catch (IOException ioException) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("0.0.0.0");
                        }
                    });
                    ioException.printStackTrace();
                } catch (RuntimeException exception){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("0.0.0.0");
                        }
                    });
                }

            }

        }).start();
            }
        },50);
    }

    public String getServerAddress () {

         return intToIp(mDhcpInfo.serverAddress);

    }

    public String getNetMask () {

        return intToIp(mDhcpInfo.netmask);
    }


    public String intToIp(int i) {

        return ( i & 0xFF) +"." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "."+
                ((i >> 24 ) & 0xFF );

    }
//    public  String intToIp(int ipAddress) {
//
//        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
//            ipAddress = Integer.reverseBytes(ipAddress);
//        }//from w ww  .  j a  v  a  2s  .c om
//
//        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
//
//        String ipAddressString;
//        try {
//            ipAddressString = InetAddress.getByAddress(ipByteArray)
//                    .getHostAddress();
//        } catch (UnknownHostException ex) {
//            Log.e("WIFI_IP", "Unable to get host address.");
//            ipAddressString = "NaN";
//        }
//
//        return ipAddressString;
//    }



    public void getLocation (TextView textView) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder result = new StringBuilder();
                            URL string  = new URL("http://ip-api.com/json/");
                            HttpURLConnection conn = (HttpURLConnection) string.openConnection();
                            conn.setRequestMethod("GET");
                            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line = "";
                            while ((line = rd.readLine()) != null) {
                                result.append(line);
                            }
                            rd.close();
                            String finalLine = result.toString();

                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        JSONObject jObject = new JSONObject(finalLine);
                                        textView.setText(jObject.getString("country"));

                                    } catch (JSONException | RuntimeException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                        } catch (ProtocolException e) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: " + e );
                                }
                            });
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: " + e );

                                }
                            });
                            e.printStackTrace();
                        } catch (IOException ioException) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: " + ioException );
                                }
                            });
                            ioException.printStackTrace();
                        } catch (RuntimeException exception){
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: " + exception );
                                }
                            });
                        }

                    }

                }).start();
            }
        },50);






    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "02:00:00:00";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public static String macAddress (StringBuilder res1){


        try {
            // get all the interfaces
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            //find network interface wlan0
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("eth0")) continue;
                //get the hardware address (MAC) of the interface
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes == null) {
                    return "";
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
}

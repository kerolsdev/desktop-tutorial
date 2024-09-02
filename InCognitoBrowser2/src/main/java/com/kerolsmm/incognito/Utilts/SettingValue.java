package com.kerolsmm.incognito.Utilts;

import static com.kerolsmm.incognito.Utilts.StaticValue.DesktopMode;
import static com.kerolsmm.incognito.Utilts.StaticValue.DownloadRequest;
import static com.kerolsmm.incognito.Utilts.StaticValue.ISRate;
import static com.kerolsmm.incognito.Utilts.StaticValue.IS_PRIVATE;
import static com.kerolsmm.incognito.Utilts.StaticValue.Password;
import static com.kerolsmm.incognito.Utilts.StaticValue.Popups;
import static com.kerolsmm.incognito.Utilts.StaticValue.SeekbarTextFont;
import static com.kerolsmm.incognito.Utilts.StaticValue.Third_party;
import static com.kerolsmm.incognito.Utilts.StaticValue.Javascript;
import static com.kerolsmm.incognito.Utilts.StaticValue.Cookies;
import static com.kerolsmm.incognito.Utilts.StaticValue.Ads;
import static com.kerolsmm.incognito.Utilts.StaticValue.rate;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingValue {


    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingValue (Context context) {
        sharedPreferences = context.getSharedPreferences("value",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.context = context;
    }

    public void setSavePassword (boolean isCheck) {
        editor.putBoolean(Password,isCheck);
    }
  /*  public void setAdsValue (boolean isCheck) {
        editor.putBoolean(Ads,isCheck);

    }*/
    public void setPopups (boolean isCheck) {
        editor.putBoolean(Popups,isCheck);

    }
    public void setJavaScript (boolean isCheck) {
        editor.putBoolean(Javascript,isCheck);

    }

    public void setRate (boolean isChecked) {
        editor.putBoolean(ISRate,isChecked);
    }
    public void setFinish (boolean b) {
        editor.putBoolean("finish" , b);

    }

    public void setSearch(int position){
        editor.putInt(rate,position);
    }
    public int getSearch() {
        return sharedPreferences.getInt(rate,0);
    }

    public boolean isFinish () {
        return sharedPreferences.getBoolean("finish",false);
    }
    public boolean isRate () {
        return sharedPreferences.getBoolean(ISRate,true);
    }

    public void setAllowCookies (boolean isCheck) {
        editor.putBoolean(Cookies,isCheck);
    }

    public void setDesktopMode (boolean isCheck) {
        editor.putBoolean(DesktopMode,isCheck);
    }

    public void setDownloadRequest (boolean isCheck) {
        editor.putBoolean(DownloadRequest,isCheck);
    }
    public void setThird_party (boolean isCheck) {
        editor.putBoolean(Third_party,isCheck);
    }

    public boolean isSavePassword () {
        return sharedPreferences.getBoolean(Password,true);
    }
/*
    public boolean isAdsBlock() {
        return sharedPreferences.getBoolean(Ads,true);
    }
*/
    public boolean isPopups() {
        return sharedPreferences.getBoolean(Popups,false);
    }
    public boolean isJavascript () {
        return sharedPreferences.getBoolean(Javascript,true);
    }
    public void setHistory (boolean isChecked){
        editor.putBoolean(IS_PRIVATE,isChecked);
    }

    public boolean isHistory () {
        return sharedPreferences.getBoolean(IS_PRIVATE,true);
    }

    public boolean isCookies () {
        return sharedPreferences.getBoolean(Cookies,true);
    }
    public boolean isDownloadRequest () {
        return sharedPreferences.getBoolean(DownloadRequest,true);
    }
    public boolean isDesktopMode () {
        return sharedPreferences.getBoolean(DesktopMode,false);
    }
    public boolean isThird_party () {
        return sharedPreferences.getBoolean(Third_party,true);
    }

    public void setValuePublic (String keyValue , boolean isCheck) {
        editor.putBoolean(keyValue,isCheck);
    }

    public int getSeekbarFont () {
        return sharedPreferences.getInt(SeekbarTextFont,100);
    }

    public void setSeekbar (int progress) {
        editor.putInt(SeekbarTextFont,progress);
    }

    public boolean getValuePublic (String keyValue) {
        if (keyValue.equals(Password) || keyValue.equals(Third_party) ||
                keyValue.equals(Javascript) || keyValue.equals(Cookies) ||  keyValue.equals(Ads)
                || keyValue.equals(IS_PRIVATE) ) {
            return sharedPreferences.getBoolean(keyValue, true);
        }else {
            return sharedPreferences.getBoolean(keyValue, false);
        }
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

}

package com.wifinet.internetcheck;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {


    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;

    public Utils (AppCompatActivity activity)
    {
        sharedPreferences =  activity.getSharedPreferences("GO", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public void SaveValue () {

        editor.putBoolean("START",true);
        editor.apply();

    }

    public Boolean Result () {

        return sharedPreferences.getBoolean("START",false);
    }

    public void  setFinish () {
        editor.putBoolean("END",true);
        editor.apply();
    }
  public boolean  getFinish () {

        return sharedPreferences.getBoolean("END",false);
  }
    public void SaveValueRequest() {

        editor.putBoolean("ORDER",true);
        editor.apply();

    }

    public boolean getValueRequest () {
        return sharedPreferences.getBoolean("ORDER",false);
    }




}

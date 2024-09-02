package com.wifinet.internetcheck.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.wifinet.internetcheck.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Privacy_Policy extends AppCompatActivity {
   String text_Privacy;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy__policy);

        getSupportActionBar().setElevation(0);
                /*Read file text for Privacy Page*/
              InputStream is = getResources().openRawResource(R.raw.privacy_generator);
              TextView textView = (TextView)findViewById(R.id.text_Privacy_policy);
              BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
             text_Privacy = sb.toString();
        } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
          textView.setText(text_Privacy);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
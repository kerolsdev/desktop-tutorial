package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wifinet.internetcheck.MyWebViewClient;
import com.wifinet.internetcheck.R;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RouterLogin extends AppCompatActivity {

    WebView webView;
    String address;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        if (getIntent().hasExtra("ip")) {

            address = getIntent().getStringExtra("ip");

            Log.e("TAG", "onCreate: " );

        } else {

            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            final DhcpInfo dhcp = wm.getDhcpInfo();
            address = Formatter.formatIpAddress(dhcp.gateway);
        }

        webView = (WebView) findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.load);


        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        loadUrl(address,webView);
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);


       /* if (IronSource.isInterstitialReady()) {
            //show the interstitial
            IronSource.showInterstitial();
        }else {
            IronSource.loadInterstitial();

        }*/




    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    public boolean URLIsReachable(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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

    private void loadUrl(String url, WebView webview) {
        try {
            url = url.trim();
            if (url.isEmpty()) {
                url = "about:blank";
            }
            if (url.startsWith("about:") || url.startsWith("javascript:") || url.startsWith("file:") || url.startsWith("data:") ||
                    (url.indexOf(' ') == -1 && Patterns.WEB_URL.matcher(url).matches())) {
                int indexOfHash = url.indexOf('#');
                String guess = URLUtil.guessUrl(url);
                if (indexOfHash != -1 && guess.indexOf('#') == -1) {
                    // Hash exists in original URL but no hash in guessed URL
                    url = guess + url.substring(indexOfHash);
                } else {
                    url = guess;
                }
            } /*else {
                url = URLUtil.composeSearchUrl(url, searchUrl, "%s");
            }*/

            if (url.contains("http://")) {
                webview.loadUrl(url);
            } else {
                webview.loadUrl(url);
            }
            Log.e("TAG", "loadUrl: " + url );
        }catch (RuntimeException runtimeException) {
            Log.e("TAG", "loadUrl: ", runtimeException );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


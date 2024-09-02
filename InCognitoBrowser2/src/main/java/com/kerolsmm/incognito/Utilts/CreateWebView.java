package com.kerolsmm.incognito.Utilts;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.kerolsmm.incognito.Activitys.Home;
import com.kerolsmm.incognito.BuildConfig;
import com.kerolsmm.incognito.R;

public class CreateWebView {

    private ValueCallback<Uri[]> fileUploadCallback;
    private boolean fileUploadCallbackShouldReset;
    static final int FORM_FILE_CHOOSER = 1;
    Context context;
    FrameLayout frameLayout;



    public CreateWebView(Context context ,FrameLayout frameLayout) {
        this.context =  context;
        this.frameLayout = frameLayout;
    }

    public WebView getWebView() {
        SettingValue settingValue = new SettingValue(context);
        WebView webview = new WebView(context);
        CookieManager cookieManager = CookieManager.getInstance();
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(settingValue.isJavascript());
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setTextZoom(settingValue.getSeekbarFont());
        settings.setDatabaseEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webview,false);
        setDesktopMode(settingValue.isDesktopMode(),settings , context);
        webview.setVisibility(View.VISIBLE);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webview.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false);
        }*/
       // frameLayout.addView(webview);
        return webview;
    }




    public static void setDesktopMode(final boolean enabled , WebSettings webSettings , Context context) {
try
{
        webSettings.setUserAgentString(getUserAgent(enabled,context));
        webSettings.setUseWideViewPort(enabled);
       // webSettings.setLoadWithOverviewMode(enabled);
}catch (RuntimeException runtimeException) {

}
    }
    private static boolean isNetwork (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
    public static void Webshare(WebView webView, Home home) {
        try {

            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, webView.getOriginalUrl().toString());
            intent.setType("text/plain");
            home.startActivity(intent);

        } catch (ActivityNotFoundException a) {

            Toast.makeText(home, "You don't have apps to run", Toast.LENGTH_SHORT).show();

        }
    }
    public  static  boolean check_permission(int permission , Home home){
        if (permission == 1) {
            return ContextCompat.checkSelfPermission(home, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else if (permission == 2) {
            return ContextCompat.checkSelfPermission(home, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else if (permission == 3) {
            return ContextCompat.checkSelfPermission(home, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else if (permission == 4) {
            return ContextCompat.checkSelfPermission(home, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public static String getUserAgent(boolean desktopMode, Context context) {
        String mobilePrefix = "Mozilla/5.0 (Linux; Android "+Build.VERSION.RELEASE+") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36";

        if (context.getResources().getBoolean(R.bool.large_layout)){
            mobilePrefix = "Mozilla/5.0 (Linux; Android "+Build.VERSION.RELEASE+") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";
        }else  {
            mobilePrefix = "Mozilla/5.0 (Linux; Android "+Build.VERSION.RELEASE+") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36";
        }

        String desktopPrefix = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:70.0) Gecko/20100101 Firefox/70.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";
         // String desktopPrefix = "Mozilla/5.0 (X11; Linux "+ System.getProperty("os.arch") +")";
        // Log.e("TAG", WebSettings.getDefaultUserAgent(context) + "  "  +System.getProperty("os.arch") );
       // Log.e("TAG", "getUserAgent: " + Build.VERSION.RELEASE );

        String newUserAgent = WebSettings.getDefaultUserAgent(context);
        String prefix = newUserAgent.substring(0, newUserAgent.indexOf(")") + 1);

        if (desktopMode) {
            try {
                // newUserAgent = newUserAgent.replace(prefix, desktopPrefix);
                   newUserAgent = desktopPrefix;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                newUserAgent = mobilePrefix;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*        if (desktopMode) {
            return desktopPrefix;
        }else  {
            return mobilePrefix;
        }*/

        return newUserAgent;
    }

}

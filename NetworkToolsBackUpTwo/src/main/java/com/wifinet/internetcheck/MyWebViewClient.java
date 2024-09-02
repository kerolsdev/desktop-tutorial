package com.wifinet.internetcheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.notification_error_ssl_cert_invalid);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    //override
    //override
 public boolean shouldOverrideUrlLoading(WebView view, String url) {

//Use your own WebView component to respond to Url loading events, instead of using the default browser to load the page
 view.loadUrl(url);
        Log. d ( "MyWebViewClient" , "shouldOverrideUrlLoading" );
 //Remember to consume this event. Explain to friends who don’t know. The meaning of returning True in Android is to stop here, and the event will not be bubbled and delivered. We call it consuming
 return true ;

    }
}
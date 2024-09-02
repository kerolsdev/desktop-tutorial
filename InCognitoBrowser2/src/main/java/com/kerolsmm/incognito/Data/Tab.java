package com.kerolsmm.incognito.Data;


import android.webkit.WebView;

public class Tab {

    private WebView webview;
    private boolean selected;

    private int id;

    public Tab() {}

    public Tab(WebView webView , boolean selected) {
        this.webview = webView;
        this.selected = selected;
    }

    public Tab(int id,WebView webView , boolean selected) {
        this.id = id;
        this.webview = webView;
        this.selected = selected;
    }

    public WebView getWebView() {
        return webview;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setWebview(WebView webview) {
        this.webview = webview;
    }
}

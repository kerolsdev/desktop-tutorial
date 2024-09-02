package com.kerolsmm.incognitopro.Activitys;

import static com.kerolsmm.incognitopro.DownloaderApp.FileDownloader.getMimeTypeFromUrl;
import static com.kerolsmm.incognitopro.Utilts.CreateWebView.Webshare;
import static com.kerolsmm.incognitopro.Utilts.CreateWebView.check_permission;
import static com.kerolsmm.incognitopro.Utilts.CreateWebView.setDesktopMode;
import static com.kerolsmm.incognitopro.Utilts.Search.getCompletions;
import static com.kerolsmm.incognitopro.Utilts.StaticValue.ChangeValue;
import static com.kerolsmm.incognitopro.Utilts.StaticValue.isSearch;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableKt;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
/*import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;*/


import com.kerolsmm.incognitopro.Data.Data_name;
import com.kerolsmm.incognitopro.Data.FileModel;
import com.kerolsmm.incognitopro.Data.MvvmTab;
import com.kerolsmm.incognitopro.Data.Tab;
import com.kerolsmm.incognitopro.Data.TabMvvmModel;
import com.kerolsmm.incognitopro.Data.UpdatePosition;
import com.kerolsmm.incognitopro.DownloadUtilt;
import com.kerolsmm.incognitopro.DownloaderApp.DownloadModel;
import com.kerolsmm.incognitopro.DownloaderApp.DownloaderService;
import com.kerolsmm.incognitopro.Fragments.DownloadRequstAfter;
import com.kerolsmm.incognitopro.Fragments.InfoDialog;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.SafeClickListener;
import com.kerolsmm.incognitopro.Utilts.AdsUnity;
import com.kerolsmm.incognitopro.Utilts.BroadcastCustom;
import com.kerolsmm.incognitopro.Utilts.CreateWebView;
import com.kerolsmm.incognitopro.Utilts.More;
import com.kerolsmm.incognitopro.Utilts.SearchAdapter;
import com.kerolsmm.incognitopro.Utilts.SettingValue;
import com.kerolsmm.incognitopro.Fragments.TabsDialog;
import com.kerolsmm.incognitopro.adblockads.util.AdBlocker;
import com.kerolsmm.incognitopro.databinding.ActivityHomeBinding;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends AppCompatActivity implements SearchAdapter.onClickSearch, More.onFindPage {

    static String searchUrl = "https://www.google.com/search?q=%s";
    private ActivityHomeBinding binding;
    Data_name data_name;
    private ArrayList<Tab> arrayListTabs = new ArrayList<>();
    private int positionTabs;
    private WebView webViewCurrent;
    private ValueCallback<Uri[]> fileUploadCallback;
    private boolean fileUploadCallbackShouldReset;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private MvvmTab mvvmTab;
    private More mMore;
    private StringBuilder adservers;
    private SettingValue settingValue;
    static final int PERMISSION_REQUEST_DOWNLOAD = 3;
    static final int PERMISSION_REQUEST_DOWNLOAD_MORE = 4;
    private BroadcastCustom broadcastCustom;
    private final View[] fullScreenView = new View[1];
    private final WebChromeClient.CustomViewCallback[] fullScreenCallback = new WebChromeClient.CustomViewCallback[1];
    private boolean mErrorCodeRequest = false;
    private PermissionRequest permissionRequestNew;
    private boolean isHistory = false;
    private boolean isHideMore = false;
    private boolean isFouce = false;
    private GeolocationPermissions.Callback GPScallback;
    private String OrginalResource;
  /*  private AdView mAdView;*/
    private boolean isSearchFindPage = false;
    private DownloadUtilt downloadUtilt;
    Bitmap bitmapHome;

    private DownloadModel downloadModel;

    private boolean isHiddenSplashScreen = true;
    private String UrlDownload = "";

    private boolean isShowTabs = false;

    private boolean isHiddenKeyBoard = true;

  /*  private IronSourceBannerLayout mIronSourceBannerLayout;*/
    private boolean isFinishSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*SplashScreen splashScreen =*/
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
      /*  splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return isHiddenSplashScreen;
            }
        });*/



        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*AdsShow();*/

        mvvmTab = new ViewModelProvider(this).get(MvvmTab.class);
        positionTabs = 0;
        broadcastCustom = new BroadcastCustom();
        data_name = Data_name.getInstance(Home.this);
        settingValue = new SettingValue(getApplicationContext());
        mMore = new More(Home.this, mvvmTab , this);
        downloadUtilt = new DownloadUtilt(this);
        InitView();
        init_Toolbar();
        ResultFileChooser();

        String initulr = getUrlFromIntent(getIntent());
        try {
            if (initulr != null && !initulr.isEmpty()) {
                NewAddTab(initulr);
            }else {
                NewAddTab("about:blank");
            }
        }catch (RuntimeException runtimeException){
                NewAddTab("about:blank");
        }
        ChangeValue = false;
        if (!settingValue.isRate() && !settingValue.isFinish()){
            try {
                RateApp();
            }catch (RuntimeException runtimeException) {

            }
        }
        try {
            settingValue.setRate(false);
            settingValue.getEditor().apply();
        }catch (RuntimeException runtimeException) {}

        if(settingValue.getSearch() == 0) {
            searchUrl = "https://www.google.com/search?q=%s";
        } else if(settingValue.getSearch() == 1) {
            searchUrl = "https://www.bing.com/search?q=%s";
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                try {
                    if (isSearchFindPage) {
                        CloseSearch();
                    }else if (binding.Searchview.getVisibility() == View.VISIBLE) {
                        setClickSearch();
                    } else {
                        if (getWebView() != null) {
                            if (getWebView().canGoBack()) {
                                getWebView().goBack();
                            } else {
                                moveTaskToBack(true);
                            }

                        } else {
                            moveTaskToBack(true);
                        }
                    }
                } catch (RuntimeException runtimeException) {
                    Log.e("TAG", "handleOnBackPressed: ", runtimeException );
                }

            }
        });

     /*   try {
            registerReceiver(broadcastCustom, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (RuntimeException runtimeException) {
            Log.e("TAG", "registerReceiver: ", runtimeException );
        }*/

        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
            WebStorage.getInstance().deleteAllData();
            getWebView().clearCache(true);
            getWebView().clearFormData();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Glide.get(Home.this).clearDiskCache();
                       // getCacheDir().delete();
                    }catch (RuntimeException r) {
                        Log.e("TAG", "run: ", r);
                    }

                }
            }).start();
        }catch (RuntimeException runtimeException){
            Log.e("TAG", "onCreate: ", runtimeException );
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ChangeValue = false;
                readAdServers();
            }
        }).start();

    }


    public void init_Toolbar() {

        try {


        binding.WebViewnowsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getWebView() != null) {
                    getWebView().reload();
                    binding.searchEdit.clearFocus();
                    try {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(Home.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (RuntimeException runtimeException) {

                    }
                }
            }
        });


        binding.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchEdit.setText("");
            }
        });


        SearchAdapter searchAdapter = new SearchAdapter(new ArrayList<String>(), this);
        binding.ListSearch.setAdapter(searchAdapter);
        binding.ListSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                  /*  if (dy > 0) {
                        // Scrolling up
                        isHiddenKeyboard = true;
                        Log.e("TAG", "onScrolled:kerolsone Up " );
                        UIUtil.showKeyboard(Home.this,binding.searchEdit);
                    } else {
                        // Scrolling down
                        Log.e("TAG", "onScrolled:kerolsone down " );
                        isHiddenKeyboard = false;
                        UIUtil.hideKeyboard(Home.this,recyclerView);
                    }*/
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                       // Log.e("TAG", "onScrolled:kerolsone SCROLL_STATE_IDLE " );
                        isHiddenKeyBoard = true;
                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                       // Log.e("TAG", "onScrolled:kerolsone SCROLL_STATE_DRAGGING " );
                        try {
                            if (isFinishSearch) {
                                isHiddenKeyBoard = false;
                                UIUtil.hideKeyboard(Home.this);
                            }
                        }catch (RuntimeException runtimeException) {

                        }
                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                       // Log.e("TAG", "onScrolled:kerolsone SCROLL_STATE_SETTLING " );
                    }

                }
            });

        binding.searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                if (b) {
                    searchAdapter.setArrayList(new ArrayList<String>());
                    binding.searchEdit.setText("");
                    setImage(settingValue.getSearch());
                    binding.searchEdit.setCursorVisible(true);
                    binding.Searchview.setVisibility(View.VISIBLE);
                    binding.Homeicon.setVisibility(View.GONE);
                    binding.tabs.setVisibility(View.GONE);
                    binding.more.setVisibility(View.GONE);
                    isFouce = true;
                    if (getWebView() != null) {
                        if (getWebView().getOriginalUrl().equals("about:blank")) {
                            binding.WebViewnowsearch.setVisibility(View.GONE);
                        }
                        binding.TextTitleSearch.setText(getWebView().getTitle());
                        binding.TextUrlSearch.setText(getWebView().getUrl());
                        binding.searchEdit.setText("");
                        setChange(getWebView());

                    } else {
                        binding.WebViewnowsearch.setVisibility(View.GONE);
                    }
                } else {
                    isFouce = false;
                    binding.Cancel.setVisibility(View.GONE);
                    setClickSearch();
                }
                }catch (RuntimeException runtimeException) {

                }


            }
        });

        binding.searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    try {
                        if (!v.getText().toString().isEmpty()) {
                            loadUrl(v.getText().toString(), getWebView());
                        }
                        //binding.homeview.setVisibility(View.GONE);
                        binding.searchEdit.clearFocus();
                        try {
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(Home.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } catch (RuntimeException runtimeException) {

                        }
                       // searchAdapter.setArrayList(new ArrayList<String>());
                    } catch (RuntimeException r) {

                    }

                    return true;
                }
                return false;
            }
        });


        TextChanger(searchAdapter);

/*        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        int finalActionBarHeight = actionBarHeight;*/
        binding.more.setOnClickListener(new SafeClickListener(new SafeClickListener.OnSafeClick() {
            @Override
            public void onSafeClick(View view) {
                try {
                    mMore.setWebView(getWebView(), arrayListTabs, positionTabs);
                    mMore.setPopUpWindow(getLayoutInflater(), data_name, isHideMore,binding);
                    mMore.getMyPop().showAsDropDown(view, -(int) getResources().getDimension(R.dimen.widthx),-(int) getResources().getDimension(R.dimen.toolbar2) , Gravity.NO_GRAVITY);

                } catch (RuntimeException runtimeException) {

                }
            }
        })); /*{
            @Override
            public void onClick(View view) {
                // Calculate ActionBar height


            }
        });*/

        binding.siteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getWebView() != null) {
                    info_Dialog(getWebView());
                }
            }
        });


        binding.Homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebView().loadUrl("about:blank");
                binding.Homeicon.setVisibility(View.GONE);
                settextEdit("about:blank");
            }
        });

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (!isOpen && !isSearchFindPage && isHiddenKeyBoard) {
                            binding.searchEdit.clearFocus();
                            // binding.WebViewFrameLayout.requestFocus();
                            // setClickSearch();
                        } else {
                            // binding.siteicon.setEnabled(false);
                        }
                    }
                });
        }catch (RuntimeException runtimeException) {

        }
    }

    private void ResultFileChooser() {
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Here, no request code
                            Intent data = result.getData();
                            if (fileUploadCallbackShouldReset) {
                                if (data.getClipData()!= null){
                                    try {
                                    Uri[] uris = new Uri[data.getClipData().getItemCount()];
                                    for (int i = 0 ; i < uris.length ; i++) {
                                       uris[i] = data.getClipData().getItemAt(i).getUri();
                                    }
                                    fileUploadCallback.onReceiveValue(uris);
                                    fileUploadCallback = null;

                                    }catch (RuntimeException runtimeException) {
                                        Log.e("TAG", "onActivityResult: " , runtimeException );

                                    }
                                }else {
                                    fileUploadCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result.getResultCode(), data));
                                    fileUploadCallback = null;
                                }

                            } else {
                                fileUploadCallbackShouldReset = true;
                            }
                        }else {
                            fileUploadCallback.onReceiveValue(null);
                            fileUploadCallback = null;
                        }
                        }catch (RuntimeException re) {

                        }
                    }
                });

    }

    private void settextEdit(String url) {
        try {
            if (url.isEmpty() || url.equals("about:blank") || url.equals("null")) {
                //
                binding.searchEdit.setText("");
            } else {
                binding.searchEdit.setText(url);
            }
        } catch (RuntimeException ee) {

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        binding.tabs.setOnClickListener(new SafeClickListener(new SafeClickListener.OnSafeClick() {
            @Override
            public void onSafeClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            getWebView().destroyDrawingCache();
                            getWebView().setDrawingCacheEnabled(true);
                            getWebView().buildDrawingCache(true);
                        } catch (OutOfMemoryError | RuntimeException runtimeException) {
                            Log.e("TAG", "onClick: `", runtimeException);
                        }


                        try {
                            bitmapHome = createBitmapFromView(binding.homeview);
                        } catch (RuntimeException ignored) {

                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                try {
                                    TabsDialog tabsDialog = new TabsDialog(positionTabs, arrayListTabs, bitmapHome);
                                    tabsDialog.show(getSupportFragmentManager(), tabsDialog.getTag());
                                } catch (RuntimeException runtimeException) {

                                }
                            }
                        });
                    }
                }).start();
            }
        }));


    }

    //  arrayListTabs.get(positionTabs).setWebview(getWebView());
    private void InitView() {
        mvvmTab.getArrayListMutableLiveData().observe(this, new Observer<TabMvvmModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(TabMvvmModel tabs) {
                try {
                    binding.WebViewFrameLayout.removeAllViews();
                    arrayListTabs = new ArrayList<>(tabs.getTab());
                    if (!arrayListTabs.isEmpty()) {
                        try {
                            positionTabs = tabs.getmPosition();
                            if (getWebView() != null) {
                                getWebView().setVisibility(View.GONE);
                                getWebView().stopLoading();
                                getWebView().onPause();
                            }

                        } finally {
                            setcurrentWbview(arrayListTabs.get(tabs.getmPosition()).getWebView());
                            getWebView().setVisibility(View.VISIBLE);
                            getWebView().requestFocus();
                            getWebView().onResume();

                            binding.tabnum.setText("" + arrayListTabs.size());
                            if (arrayListTabs.get(positionTabs).getWebView().getUrl().equals("about:blank")) {
                                binding.Homeicon.setVisibility(View.GONE);
                                mVIEWHOME();
                                setImage(settingValue.getSearch());
                                binding.siteicon.setEnabled(false);
                                isHideMore = true;
                            } else {
                                binding.Homeicon.setVisibility(View.VISIBLE);
                                binding.homeview.setVisibility(View.GONE);
                                binding.siteicon.setImageResource(R.drawable.lock_svgrepo_com);
                                binding.siteicon.setEnabled(true);
                                isHideMore = false;
                            }
                            settextEdit("" + getWebView().getUrl());
                            try {
                                setDesktopMode(settingValue.isDesktopMode(), getWebView().getSettings(),getApplicationContext());
                            } catch (RuntimeException r) {

                            }
                            binding.WebViewFrameLayout.addView(getWebView());
                        }
                    } else {
                        NewAddTab("about:blank");
                    }
                }catch (RuntimeException runtimeException) {

                }
            }

        });

        mvvmTab.getDoAnyThing().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    NewAddTab(s);
                }catch (RuntimeException runtimeException){
                }
            }
        });


        mvvmTab.getMutableLiveData().observe(Home.this, new Observer<UpdatePosition>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UpdatePosition integer) {
                positionTabs = integer.getmPosition();
                binding.tabnum.setText("" + integer.getTab());
            }
        });


    }


    private void NewAddTab(String url) {
        // binding.WebViewFrameLayout.removeAllViews();
        try {

        try {
            if (getWebView() != null) {
                getWebView().setVisibility(View.GONE);
                getWebView().stopLoading();
                getWebView().onPause();
            }
            try {
                for (Tab item : arrayListTabs) {
                    item.setSelected(false);
                }
            } catch (RuntimeException ignored) {

            }
        } finally {
            CreateWebView createWebView = new CreateWebView(this, binding.WebViewFrameLayout);
            WebView mWebView = createWebView.getWebView();
            StartWebView(mWebView);
            try {
                if (URLUtil.isValidUrl(url)) {
                    mWebView.loadUrl(url);
                } else {
                    loadUrl(url, mWebView);
                }
            }catch (RuntimeException runtimeException) {

            }
            setcurrentWbview(mWebView);
            arrayListTabs.add(0, new Tab(mWebView, true));
            positionTabs = 0;
            getWebView().setVisibility(View.VISIBLE);
            if (arrayListTabs.get(positionTabs).getWebView().getUrl().equals("about:blank")) {
                binding.Homeicon.setVisibility(View.GONE);
                mVIEWHOME();
                setImage(settingValue.getSearch());
                binding.siteicon.setEnabled(false);
            } else {
                binding.Homeicon.setVisibility(View.VISIBLE);
                binding.homeview.setVisibility(View.GONE);
                binding.siteicon.setImageResource(R.drawable.lock_svgrepo_com);
                binding.siteicon.setEnabled(true);
            }
            getWebView().requestFocus();
            getWebView().onResume();
            settextEdit("" + arrayListTabs.get(0).getWebView().getUrl());
            binding.tabnum.setText("" + arrayListTabs.size());
            binding.WebViewFrameLayout.addView(getWebView());
        }
        }catch (RuntimeException ignored) {

        }
    }


    public void setcurrentWbview(WebView webView) {
        this.webViewCurrent = webView;
    }

    public WebView getWebView() {
        return webViewCurrent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String url = getUrlFromIntent(intent);
        if (intent.getAction().equals("addnewtab") ) {
            NewAddTab(intent.getStringExtra("url_site"));
        }else if ( url != null && !url.isEmpty()){
            try {
                NewAddTab(url);
            }catch (RuntimeException runtimeException){
                NewAddTab("about:blank");
            }
        }
        super.onNewIntent(intent);
    }


    private void StartWebView(WebView webview) {

        webview.setOnLongClickListener(v -> {
            String url = null, imageUrl = null;
            WebView.HitTestResult r = ((WebView) v).getHitTestResult();
            switch (r.getType()) {
                case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                    url = r.getExtra();
                    break;
                case WebView.HitTestResult.IMAGE_TYPE:
                    imageUrl = r.getExtra();
                    break;
                case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                case WebView.HitTestResult.EMAIL_TYPE:
                case WebView.HitTestResult.UNKNOWN_TYPE:
                    Handler handler = new Handler();
                    Message message = handler.obtainMessage();
                    ((WebView) v).requestFocusNodeHref(message);
                    url = message.getData().getString("url");
                    if ("".equals(url)) {
                        url = null;
                    }
                    imageUrl = message.getData().getString("src");
                    if ("".equals(imageUrl)) {
                        imageUrl = null;
                    }
                    if (url == null && imageUrl == null) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }
            showLongPressMenu(url, imageUrl);
            return true;

        });

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    binding.progressId.setVisibility(View.INVISIBLE);
                } else {
                    binding.progressId.setVisibility(View.VISIBLE);
                    binding.progressId.setProgress(newProgress);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                try {

                binding.toolbarApp.setVisibility(View.GONE);
                fullScreenView[0] = view;
                fullScreenCallback[0] = callback;
                Home.this.findViewById(R.id.WebViewFrameLayout).setVisibility(View.INVISIBLE);
                ViewGroup fullscreenLayout = Home.this.findViewById(R.id.fullScreenVideo);
                fullscreenLayout.addView(view);
                updateFullScreen(true);
                fullscreenLayout.setVisibility(View.VISIBLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
               // IronSource.onPause(Home.this);

                }catch (RuntimeException runtimeException) {

                }
            }

            @Override
            public void onHideCustomView() {
                try {


                if (fullScreenView[0] == null) return;
                ViewGroup fullscreenLayout = Home.this.findViewById(R.id.fullScreenVideo);
                fullscreenLayout.removeView(fullScreenView[0]);
                fullscreenLayout.setVisibility(View.GONE);
                binding.toolbarApp.setVisibility(View.VISIBLE);
                fullScreenView[0] = null;
                fullScreenCallback[0] = null;
                updateFullScreen(false);
                Home.this.findViewById(R.id.WebViewFrameLayout).setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

               // IronSource.onResume(Home.this);

                }catch (RuntimeException r) {

                }
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                StringBuilder text_permission = new StringBuilder();
                ArrayList<String> arrayListStrings = new ArrayList<>();
                for (String item : request.getResources()) {
                    if (item.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        arrayListStrings.add(Manifest.permission.CAMERA);
                        text_permission.append("\n" + getString(R.string.Camera) );
                    } else if (item.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                        arrayListStrings.add(Manifest.permission.RECORD_AUDIO);
                        text_permission.append("\n" +getString(R.string.Audio));
                    }
                }
                if (!arrayListStrings.isEmpty()) {
                    permissionRequestNew = request;
                    String[] strings = new String[arrayListStrings.size()];
                    strings = arrayListStrings.toArray(strings);
                    alterPermission(strings, request, text_permission);
                }

            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                GPScallback = callback;
                OrginalResource = origin;
                new MaterialAlertDialogBuilder(Home.this)
                        .setTitle(getString(R.string.PermissionRequired))
                        .setMessage(getString(R.string.ThesiteneedspermissiontoStart) + "\n" + getString(R.string.geo))
                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (Build.VERSION.SDK_INT < 23 || check_permission(1, Home.this)) {
                                    // location permissions were granted previously so auto-approve
                                    callback.invoke(origin, true, false);
                                } else {
                                    // location permissions not granted so request them
                                    ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                                }


                            }
                        }).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(false).show();

            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (fileUploadCallback != null) {
                    fileUploadCallback.onReceiveValue(null);
                }
                fileUploadCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    fileUploadCallbackShouldReset = true;
                    someActivityResultLauncher.launch(intent);
                    return true;
                } catch (RuntimeException e) {
                    // Continue below
                }

                // FileChooserParams.createIntent() copies the <input type=file> "accept" attribute to the intent's getType(),
                // which can be e.g. ".png,.jpg" in addition to mime-type-style "image/*", however startActivityForResult()
                // only accepts mime-type-style. Try with just */* instead.
                try {

                 intent.setType("*/*");

                    fileUploadCallbackShouldReset = false;
                    someActivityResultLauncher.launch(intent);
                    return true;
                } catch (RuntimeException e) {
                }

                Toast.makeText(getApplicationContext(), "Can't open file chooser", Toast.LENGTH_SHORT).show();
                fileUploadCallback = null;
                return false;
            }

        });

        webview.setWebViewClient(webViewClient());
        webview.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {

         /*   String headerFileName = getFileNameFromHeader(contentDisposition);
            String filename = URLUtil.guessFileName(url, contentDisposition , mimetype);
            int lastDotIndex2 = filename.lastIndexOf(".");

            if (headerFileName != null && filename.substring(lastDotIndex2 + 1).equals("bin")) {
                filename  = headerFileName;
            }*/

                    new Thread(new Runnable() {
                        @Override
                        public void run() {



            String filename =  getFileNameWithU(url,mimetype,contentDisposition);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED )  {
                downloadModel = new DownloadModel(mimetype,url,contentDisposition,filename);

                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},666);
                    return;
                }
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&  Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q ) {
                downloadModel = new DownloadModel(mimetype,url,contentDisposition,filename);

                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},777);
                return;
            }

            if (settingValue.isDownloadRequest()) {
                DownloadRequstAfter downloadRequstAfter = new DownloadRequstAfter( filename , new DownloadRequstAfter.onDownloadClick() {
                    @Override
                    public void onClickForDownload() {
                        Intent intent = new Intent(Home.this, DownloaderService.class);
                        intent.putExtra("mimeType",mimetype);
                        intent.putExtra("url",url);
                        intent.putExtra("filename",filename);
                        intent.putExtra("attachment",contentDisposition);
                        startService(intent);
                    }
                });
                downloadRequstAfter.show(getSupportFragmentManager(), downloadRequstAfter.getTag());
            } else {
                Intent intent = new Intent(Home.this, DownloaderService.class);
                intent.putExtra("mimeType",mimetype);
                intent.putExtra("url",url);
                intent.putExtra("filename",filename);
                intent.putExtra("attachment",contentDisposition);
                startService(intent);
            }
                        }}).start();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            //  unregisterReceiver(broadcastCustom);
            if (isMyServiceRunning(DownloaderService.class)){
                Intent intent = new Intent(getApplicationContext(),DownloaderService.class);
                stopService(intent);
            }
            /*binding.adView.destroy();*/
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
            WebStorage.getInstance().deleteAllData();
        } catch (RuntimeException runtimeException) {
            Log.e("TAG", "sdasdadadsadsaadsa: ", runtimeException );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
        if (ChangeValue) {
            ChangeSetting(getWebView());
        }
        if (isSearch){

            if(settingValue.getSearch() == 0){
                searchUrl = "https://www.google.com/search?q=%s";
            }else if(settingValue.getSearch() == 1) {
                searchUrl = "https://www.bing.com/search?q=%s";
            }
            if(arrayListTabs.get(positionTabs).getWebView().getOriginalUrl().equals("about:blank")){
                setImage(settingValue.getSearch());}
            isSearch = false;
        }

        } catch (RuntimeException runtimeException) {
          //  Log.e("TAG", "onResume: ", runtimeException );
        }

       /* try {
            registerReceiver(broadcastCustom, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (RuntimeException runtimeException) {
            Log.e("TAG", "registerReceiver: ", runtimeException );
        }*/

        try {
            getWebView().onResume();
           // IronSource.onResume(this);
        } catch (RuntimeException runtimeException ) {
           // Log.e("TAG", "onResume: ", runtimeException );
        }

    }


    /*@Override
    public void onBackPressed() {
        if (isSearchFindPage) {
            CloseSearch();
        }else if (binding.Searchview.getVisibility() == View.VISIBLE) {
            setClickSearch();
        } else {
            if (getWebView() != null) {

                if (getWebView().canGoBack()) {
                    getWebView().goBack();
                } else {
                    moveTaskToBack(true);
                }

            } else {
                super.onBackPressed();
            }
        }
    }*/

    @SuppressLint("SetTextI18n")
    @Override
    public void onPosition(String StringSearch) {
        loadUrl(StringSearch, getWebView());
        settextEdit(StringSearch);
        binding.searchEdit.clearFocus();
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (RuntimeException runtimeException) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            getWebView().onPause();
          //  IronSource.onPause(this);
        }catch (RuntimeException runtimeException ) {
            Log.e("TAG", "onPause: ", runtimeException );
        }

    }

    private WebViewClient webViewClient() {

        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mErrorCodeRequest = false;

                if (url.equals("about:blank") || url.equals("null")) {
                    mVIEWHOME();
                    setImage(settingValue.getSearch());
                    binding.siteicon.setEnabled(false);
                    binding.Homeicon.setVisibility(View.GONE);
                    binding.progressId.setVisibility(View.INVISIBLE);
                    isHideMore = true;
                } else {
                    binding.progressId.setVisibility(View.VISIBLE);
                    binding.SSLView.setVisibility(View.GONE);
                    binding.Searchview.setVisibility(View.GONE);
                    binding.homeview.setVisibility(View.GONE);
                    binding.Homeicon.setVisibility(View.VISIBLE);
                    binding.siteicon.setImageResource(R.drawable.lock_svgrepo_com);
                    binding.siteicon.setEnabled(true);
                    isHideMore = false;
                }
                if (isSearchFindPage) {CloseSearch();}
                 view.setVisibility(View.VISIBLE);
                 isHistory = true;
                 settextEdit(url);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mErrorCodeRequest) {
                    view.setVisibility(View.GONE);
                    settextEdit(url);
                   /* AdRequest adRequest = new AdRequest.Builder().build();*/
                 /*   mAdView.loadAd(adRequest);*/
                } else {
                    binding.SSLView.setVisibility(View.GONE);
                    binding.viewNoConnect.setVisibility(View.GONE);
                    binding.scrollableError.setVisibility(View.GONE);
                }

                if (url.equals("about:blank") || url.equals("null")) {
                    mVIEWHOME();
                    setImage(settingValue.getSearch());
                    binding.siteicon.setEnabled(false);
                    binding.Homeicon.setVisibility(View.GONE);
                    binding.searchEdit.setText("");
                    binding.progressId.setVisibility(View.INVISIBLE);
                    isHideMore = true;
                } else {
                    binding.progressId.setVisibility(View.INVISIBLE);
                    /*binding.progressId.setVisibility(View.INVISIBLE);
                    binding.SSLView.setVisibility(View.GONE);
                    binding.Searchview.setVisibility(View.GONE);
                    binding.homeview.setVisibility(View.GONE);
                    binding.Homeicon.setVisibility(View.VISIBLE);
                    binding.siteicon.setImageResource(R.drawable.lock_svgrepo_com);
                    binding.siteicon.setEnabled(true);
                    isHideMore = false;*/
                }

            }


            @Override
            public void onReceivedHttpAuthRequest(WebView view, final HttpAuthHandler handler, String host, String realm) {
                new AlertDialog.Builder(Home.this)
                        .setTitle(host)
                        .setView(R.layout.login_password)
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            String username = ((EditText) ((Dialog) dialog).findViewById(R.id.username)).getText().toString();
                            String password = ((EditText) ((Dialog) dialog).findViewById(R.id.password)).getText().toString();
                            handler.proceed(username, password);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> handler.cancel()).show();
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (settingValue.isAdsBlock()) {
                    ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
                    String kk5 = String.valueOf(adservers);

                    if (kk5.contains( ":::::" + request.getUrl().getHost())) {
                        return new WebResourceResponse("text/plain", "utf-8", EMPTY);
                    }

                }
                return super.shouldInterceptRequest(view, request);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // For intent:// URLs, redirect to browser_fallback_url if given

                if (url.startsWith("market://details?") || url.startsWith("intent://play.google.com/") || url.startsWith("intent://details?") ) {
                    isGooglePlayStoreInstalled(url);
                    return  true;
                } else if (url.startsWith("intent://")) {
                    int start = url.indexOf(";S.browser_fallback_url=");
                    if (start != -1) {
                        start += ";S.browser_fallback_url=".length();
                        int end = url.indexOf(';', start);
                        if (end != -1 && end != start) {
                            url = url.substring(start, end);
                            url = Uri.decode(url);
                            view.loadUrl(url);
                            return true;
                        }
                    }

                } else if (url.startsWith("imdb://") || url.startsWith("reddit://")  || url.startsWith("spotify://")
                        || url.startsWith("etsy://") || url.startsWith("whatsapp://") || url.startsWith("fb://") || url.startsWith("robloxmobile://")) {
                    return  true;
                }

                return false;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == -6 || errorCode == -2){
                    mErrorCodeRequest = true;
                    mVIEWNoConnect(view);
                    binding.TextError.setText(getString(R.string.no_internet_connectio));
                    binding.imageError.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    binding.scrollableError.setVisibility(View.VISIBLE);
                }else {
                    mErrorCodeRequest = true;
                    binding.scrollableError.setVisibility(View.VISIBLE);
                    binding.TextError.setText(getString(R.string.The_site));
                    binding.imageError.setVisibility(View.GONE);
                    mVIEWNoConnect(view);
                }
               //   Log.e("TAG", "onReceivedError: " + errorCode);
              //  Log.e("TAG", "onPageFinished: " + "p3");

                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            final String[] sslErrors = {"Not yet valid", "Expired", "Hostname mismatch", "Untrusted CA", "Invalid date", "Unknown error"};

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                int primaryError = error.getPrimaryError();
                String errorStr = primaryError >= 0 && primaryError < sslErrors.length ? sslErrors[primaryError] : "Unknown error " + primaryError;
               // Log.e("TAG", "onReceivedSslError: " + errorStr );
                if (!errorStr.equals("Invalid date")) {
                    mErrorCodeRequest = true;
                    binding.scrollableError.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    mVIEWSSlError(handler);
                    binding.viewNoConnect.setVisibility(View.GONE);
                }
            }
        };
    }


    public void ChangeSetting(WebView webView) {
        SettingValue settingValue = new SettingValue(getApplicationContext());
        WebSettings settings = webView.getSettings();
        /*CookieManager.getInstance().setAcceptCookie(settingValue.isCookies());
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, settingValue.isThird_party());*/
        setDesktopMode(settingValue.isDesktopMode(), settings,getApplicationContext());
        //webView.setWebViewClient(webViewClient());
        settings.setTextZoom(settingValue.getSeekbarFont());
        webView.reload();
        ChangeValue = false;
    }


    boolean hasOrRequestPermission(String[] permission, String explanation, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||  Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            return true;
        }

        if (checkSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            return true;
        }
        if (explanation != null && shouldShowRequestPermissionRationale(permission[0])) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.PermissionRequired))
                    .setMessage(getString(R.string.Permissionisrequiredtodownload))
                    .setPositiveButton(Html.fromHtml("<font color='#006AFC'>ok</font>"), (dialog, which) ->
                            requestPermissions(permission, requestCode))
                    .show();
            return false;
        }
        requestPermissions(permission, requestCode);
        return false;

    }

    public void mVIEWNoConnect(WebView webView) {
        binding.viewNoConnect.setVisibility(View.VISIBLE);
        binding.TryAgainViewNoConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
    }

    public void mVIEWSSlError(SslErrorHandler handler) {
        binding.SSLView.setVisibility(View.VISIBLE);
        binding.homeview.setVisibility(View.GONE);
        binding.BackSafeSSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getWebView() != null) {
                   getWebView().reload();
                }
            }
        });
        binding.ContainErrorSsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.proceed();
            }
        });
    }

    private void mVIEWHOME() {
        binding.homeview.setVisibility(View.VISIBLE);
    }



    private void setChange(WebView webView) {
        binding.shareSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Webshare(webView, Home.this);
            }
        });
        binding.EditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchEdit.setText(webView.getUrl());
            }
        });
        binding.CopySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", webView.getOriginalUrl());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
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
        } else {
            url = URLUtil.composeSearchUrl(url, searchUrl, "%s");
        }

        if (url.contains("http://")) {
            String url_ = url.replace("http://", "https://");
            webview.loadUrl(url_);
        } else {
            webview.loadUrl(url);
        }
        }catch (RuntimeException runtimeException) {
            Log.e("TAG", "loadUrl: ", runtimeException );
        }
    }

    private void info_Dialog(WebView w) {
        InfoDialog infoDialog = new InfoDialog(w);
        infoDialog.show(getSupportFragmentManager(), infoDialog.getTag());
    }

    private void showLongPressMenu(String linkUrl, String imageUrl) {
        String url;
        String title;
        String[] options = new String[]{"Open in new tab", "Copy URL", "Show full URL", "Download"};

        if (imageUrl == null) {
            if (linkUrl == null) {
                throw new IllegalArgumentException("Bad null arguments in showLongPressMenu");
            } else {
                // Text link
                url = linkUrl;
                title = linkUrl;
            }
        } else {
            if (linkUrl == null) {
                // Image without link
                url = imageUrl;
                title = "Image: " + imageUrl;
            } else {
                // Image with link
                url = linkUrl;
                title = linkUrl;
                String[] newOptions = new String[options.length + 1];
                System.arraycopy(options, 0, newOptions, 0, options.length);
                newOptions[newOptions.length - 1] = "Image Options";
                options = newOptions;
            }
        }
        new AlertDialog.Builder(Home.this).setTitle(title).setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    NewAddTab(url);
                    break;
                case 1:
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    assert clipboard != null;
                    ClipData clipData = ClipData.newPlainText("URL", url);
                    clipboard.setPrimaryClip(clipData);
                    break;
                case 2:
                    new AlertDialog.Builder(Home.this)
                            .setTitle("Full URL")
                            .setMessage(url)
                            .setPositiveButton(Html.fromHtml("<font color='#006AFC'>ok</font>"), (dialog1, which1) -> {
                            })
                            .show();
                    break;
                case 3:
                    UrlDownload = url;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                    String filename =  getFileNameWithU(url,"","attachment");


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED )  {
                            downloadModel = new DownloadModel("",url,"attachment",filename);
                            ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},666);
                            return;
                        }
                    }

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&  Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q ) {
                        downloadModel = new DownloadModel("",url,"attachment",filename);
                        ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},777);
                        return;
                    }
                    Intent intent = new Intent(Home.this, DownloaderService.class);
                    intent.putExtra("mimeType","");
                    intent.putExtra("url",url);
                    intent.putExtra("filename",filename);
                    intent.putExtra("attachment","attachment");
                    startService(intent);
                        }
                    }).start();
                    break;
                case 4:
                    showLongPressMenu(null, imageUrl);
                    break;
            }
        }).show();
    }


    private void alterPermission(String[] arrayList22, PermissionRequest request, StringBuilder stringBuilder) {
      MaterialAlertDialogBuilder alert =  new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.PermissionRequired))
                .setMessage(getString(R.string.ThesiteneedspermissiontoStart) + stringBuilder.toString())
                .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
try {

                        if (arrayList22.length > 1) {
                            if (check_permission(3, Home.this) && check_permission(4, Home.this)) {
                                request.grant(request.getResources());
                            } else {
                                ActivityCompat.requestPermissions(Home.this, arrayList22, 1);
                            }
                        } else {
                            if (check_permission(3, Home.this) || check_permission(4, Home.this)) {
                                request.grant(request.getResources());

                            } else {
                                ActivityCompat.requestPermissions(Home.this, arrayList22, 1);
                            }
                        }
                    }catch (RuntimeException runtimeException) {

                    }

                    }
                }).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

      try {
          androidx.appcompat.app.AlertDialog dialog = alert.create();
          dialog.setCanceledOnTouchOutside(false);
          dialog.show();
      }catch (RuntimeException runtimeException) {
          alert.show();
      }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 1) {
                if (grantResults.length > 1) {
                    if (check_permission(3, Home.this) && check_permission(4, Home.this)) {
                        permissionRequestNew.grant(permissionRequestNew.getResources());
                    }
                } else {
                    if (check_permission(3, Home.this) || check_permission(4, Home.this)) {
                        permissionRequestNew.grant(permissionRequestNew.getResources());
                    }
                }
            }else if (requestCode == 2){
                GPScallback.invoke(OrginalResource, true, false);
            }else if (PERMISSION_REQUEST_DOWNLOAD == requestCode) {
                try {
                     downloadUtilt.startWithOutContext();
                }catch (RuntimeException ignored){}
            }else if (PERMISSION_REQUEST_DOWNLOAD_MORE == requestCode) {

                try {
                    downloadUtilt.mStart(UrlDownload,"attachment",null,null,0);
                }catch (RuntimeException ignored){}

            }else if (requestCode == 666) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, DownloaderService.class);
                    intent.putExtra("mimeType",downloadModel.mimeType);
                    intent.putExtra("url",downloadModel.url);
                    intent.putExtra("attachment",downloadModel.attachment);
                    intent.putExtra("filename",downloadModel.fileName);
                    startService(intent);
                } else {
                    // Permission is denied, handle this situation, e.g., show a message to the user.
                }
            }else if (requestCode == 777) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, DownloaderService.class);
                    intent.putExtra("mimeType",downloadModel.mimeType);
                    intent.putExtra("url",downloadModel.url);
                    intent.putExtra("attachment",downloadModel.attachment);
                    intent.putExtra("filename",downloadModel.fileName);
                    startService(intent);
                } else {
                    // Permission is denied, handle this situation, e.g., show a message to the user.
                }
            }
        } catch (RuntimeException runtimeException) {

        }
    }

    private void updateFullScreen(boolean isFullscreen) {
        final int flags =  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
      /*  final int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;*/
        int flags2 = 0;   // add LIGHT_STATUS_BAR to flag
      /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            flags2 = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(flags2);

        }*/
        boolean fullscreenNow = (getWindow().getDecorView().getSystemUiVisibility() & flags) == flags;
        if (fullscreenNow != isFullscreen) {
            getWindow().getDecorView().setSystemUiVisibility(isFullscreen ? flags : 0);
        }
    }

    private void setClickSearch() {
        try {
        binding.EditSearch.clearFocus();
        binding.WebViewFrameLayout.requestFocus();
        binding.searchEdit.setCursorVisible(false);
        binding.searchPane.setVisibility(View.GONE);
        settextEdit(getWebView().getUrl());
        binding.Searchview.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.VISIBLE);
        binding.more.setVisibility(View.VISIBLE);
        binding.viewSearch.setVisibility(View.VISIBLE);
        if (!arrayListTabs.get(positionTabs).getWebView().getOriginalUrl().equals("about:blank")) {
            binding.Homeicon.setVisibility(View.VISIBLE);
            binding.siteicon.setEnabled(true);
            binding.siteicon.setImageResource(R.drawable.lock_svgrepo_com);
        } else {
            setImage(settingValue.getSearch());
            binding.siteicon.setEnabled(false);
            binding.Homeicon.setVisibility(View.GONE);
        }
        }catch (RuntimeException runtimeException) {

        }
    }

    private String getUrlFromIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            return intent.getDataString();
        } else if (Intent.ACTION_SEND.equals(intent.getAction()) && "text/plain".equals(intent.getType())) {
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        } else if (Intent.ACTION_WEB_SEARCH.equals(intent.getAction()) && intent.getStringExtra("query") != null) {
            return intent.getStringExtra("query");
        } else {
            return "";
        }
    }

/*    public void AdsShow ()
    {

*//*
         binding.adView.loadAd();
         binding.adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd maxAd) {
                binding.adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdCollapsed(MaxAd maxAd) {

            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {

            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {
                binding.adView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {

            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

            }
        });
*//*

       *//* mAdView = findViewById(R.id.adView);*//*
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w("TAG", String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }
                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w("TAG", String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
       if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }

    }*/


    private void RateApp () {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                // We can get the ReviewInfo object
                 settingValue.setRate(true);
                 settingValue.setFinish(true);
                 settingValue.getEditor().apply();
                ReviewInfo reviewInfo = task2.getResult();
                Task <Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                });
            } else {
                // There was some problem, log or handle the error code.

            }
        });
    }
    public void SearchTools () {
        binding.viewSearch.setVisibility(View.GONE);
        binding.searchPane.setVisibility(View.VISIBLE);
        binding.searchEditPage.requestFocus();
        binding.searchEditPage.setText("");
        binding.searchCount.setText("");
        binding.searchEditPage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getWebView().findAllAsync(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        getWebView().setFindListener((activeMatchOrdinal, numberOfMatches, isDoneCounting) ->
                binding.searchCount.setText(numberOfMatches == 0 ? "Not found" :
                        String.format("%d / %d", activeMatchOrdinal + 1, numberOfMatches)));

        binding.searchFindNext.setOnClickListener(v -> {
            getWebView().findNext(true);
            try {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Home.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (RuntimeException runtimeException) {

            }
        });
        binding.searchFindPrev.setOnClickListener(v -> {
            getWebView().findNext(false);
            try {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Home.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (RuntimeException runtimeException) {

            }
        });
       binding.searchClose.setOnClickListener(v -> {
          CloseSearch();
        });
    }
    @Override
    public void onSearchPage() {
        isSearchFindPage = true;
        SearchTools();
    }

    public  void  CloseSearch () {
        try{
            binding.searchEditPage.clearFocus();
            getWebView().clearMatches();
            getWebView().requestFocus();
            binding.viewSearch.setVisibility(View.VISIBLE);
            binding.searchPane.setVisibility(View.GONE);
            isSearchFindPage = false;
        }catch (RuntimeException ignored) {}
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(Home.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (RuntimeException ignored) {}

    }
    public void setImage (int Change) {
        if (Change == 1) {
            binding.siteicon.setImageResource(R.drawable.bing_logotype_svgrepo_com);
        }else {
            binding.siteicon.setImageResource(R.drawable.ic_google_svgrepo_com);
        }
    }
    public  String getFileName(URL extUrl) {
        //URL: "http://photosaaaaa.net/photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg"
        String filename = "";
        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
        String path = extUrl.getPath();
        //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = path.split("[\\\\/]");
        if(pathContents != null){
            int pathContentsLength = pathContents.length;
            System.out.println("Path Contents Length: " + pathContentsLength);
            for (int i = 0; i < pathContents.length; i++) {
                System.out.println("Path " + i + ": " + pathContents[i]);
            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents != null && lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    System.out.println("Last Part " + i + ": "+ lastPartContents[i]);
                    if(i < (lastPartContents.length -1)){
                        name += lastPartContents[i] ;
                        if(i < (lastPartContentLength -2)){
                            name += ".";
                        }
                    }
                }
                String extension = lastPartContents[lastPartContentLength -1];
                filename = name + "." +extension;
                System.out.println("Name: " + name);
                System.out.println("Extension: " + extension);
                System.out.println("Filename: " + filename);
            }
        }
        return filename;
    }
    public  String getFileNameFromHeader(String header){
        int pos = 0;

        String fileName = null;

        if ((pos = header.toLowerCase().lastIndexOf("filename=")) >= 0) {
            fileName = header.substring(pos + 9);
            pos = fileName.lastIndexOf(";");

            if (pos > 0) {
                fileName = fileName.substring(0, pos - 1);
            }
        }
        if (fileName!=null)
            fileName=fileName.replaceAll("\"", "");

        return fileName;
    }
    private Bitmap createBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);

        view.buildDrawingCache();

        return  view.getDrawingCache();
    }

    private void isGooglePlayStoreInstalled(String url ) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ extractPackageNameFromUrl(url)));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (RuntimeException runtimeException) {

        }

    }

    public static String extractPackageName(String url) {
        // Regular expression patterns to search for the package name
        Pattern subdomainPattern = Pattern.compile("https://([^.]+)\\.reddit\\.app\\.link");
        Pattern pathPattern = Pattern.compile("https://reddit\\.app\\.link/([^/]+)/");

        Matcher subdomainMatcher = subdomainPattern.matcher(url);
        Matcher pathMatcher = pathPattern.matcher(url);

        if (subdomainMatcher.find()) {
            return subdomainMatcher.group(1);
        } else if (pathMatcher.find()) {
            return pathMatcher.group(1);
        }

        // Return null if the package name is not found
        return null;
    }


    public static String extractPackageNameFromUrl(String url) {
        Pattern pattern = Pattern.compile("id=([^&]+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }


   private void TextChanger (SearchAdapter searchAdapter) {
        binding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isFinishSearch = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()) {
                    if (isFouce) {
                        binding.Cancel.setVisibility(View.VISIBLE);
                    }
                    binding.WebViewnowsearch.setVisibility(View.GONE);
                } else {
                    if (isFouce) {
                        binding.Cancel.setVisibility(View.GONE);
                    }
                    try {
                        if (getWebView().getOriginalUrl().equals("about:blank")) {
                            binding.WebViewnowsearch.setVisibility(View.GONE);
                        } else {
                            binding.WebViewnowsearch.setVisibility(View.VISIBLE);
                        }
                    } catch (RuntimeException runtimeException) {

                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            ArrayList<String> arrayList_item = getCompletions(editable.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        searchAdapter.setArrayList(arrayList_item);
                                        isFinishSearch = true;
                                    }catch (RuntimeException ignored){}
                                }
                            });
                        }catch (RuntimeException runtimeException) {

                        }
                    }
                }).start();
            }
        });
    }


    public String getFileNameWithU (String url , String  mime , String contentDisposition) {

        if (url.startsWith("data:")) {  //when url is base64 encoded data
           return foundNameFileData(url);
        }else  {
            if (Objects.equals(mime, "") || mime.equals("application/force-download")) {
                mime = getMimeTypeFromUrl(url);
            }

            String headerFileName = getFileNameFromHeader(contentDisposition);
            String filename = URLUtil.guessFileName(url, contentDisposition , mime);
            int lastDotIndex2 = filename.lastIndexOf(".");

            if (headerFileName != null && filename.substring(lastDotIndex2 + 1).equals("bin")) {
                filename  = headerFileName;
            }

            filename =  filename.replaceAll("[\\|\\-\\s]+", " ");

            File destinationPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            if (destinationPath.exists()) {

                int lastDotIndex = filename.lastIndexOf(".");

                destinationPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        System.currentTimeMillis() + "." + filename.substring(lastDotIndex + 1));

            }
            return destinationPath.getName();
        }
    }


    public String foundNameFileData (String fileUrl) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileType = fileUrl.substring(fileUrl.indexOf("/") + 1, fileUrl.indexOf(";"));
        String filename = System.currentTimeMillis() + "." + fileType;

        return filename;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
  /*  private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Google Mobile Ads SDK.

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                binding.adView.loadAd(adRequest);
                binding.adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdImpression() {
                        // Code to be executed when an impression is recorded
                        // for an ad.
                    }

                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        binding.adView.setVisibility(View.VISIBLE);
                     //   binding.textads.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                    }
                });
            }
        });

    }*/


  /*  public boolean isPrivacyOptionsRequired() {
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        return consentInformation.getPrivacyOptionsRequirementStatus()
                == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;
    }*/

    private void readAdServers() {
        String line = "";
        adservers = new StringBuilder();
        InputStream is = this.getResources().openRawResource(R.raw.adblockserverlist);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        if (is != null) {
            try {
                while ((line = br.readLine()) != null) {
                    adservers.append(line);
                    adservers.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


/*           Uri downloadUri = Uri.parse(url);
            // get file name. if filename exists in contentDisposition, use it. otherwise, use the last part of the url.
/*            String filename = downloadUri.getLastPathSegment();
            String headerFileName = getFileNameFromHeader(contentDisposition);

                            downloadUtilt.mStartContext(url,contentDisposition,mimetype,userAgent,filename);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED )  {
                    return;
                }
                downloadUtilt.onDownloadStartNoStream(url,userAgent,contentDisposition,mimetype,filename,false);


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                return;
              }
                downloadUtilt.onDownloadStartNoStream(url,userAgent,contentDisposition,mimetype, finalFilename,false);

            */

package com.kerolsmm.incognitopro.Utilts;

import static com.kerolsmm.incognitopro.DownloaderApp.FileDownloader.getMimeTypeFromUrl;
import static com.kerolsmm.incognitopro.Utilts.CreateWebView.setDesktopMode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableKt;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.kerolsmm.incognitopro.Activitys.BookmarkActivity;
import com.kerolsmm.incognitopro.Activitys.DownloadActivity;
import com.kerolsmm.incognitopro.Activitys.Home;
import com.kerolsmm.incognitopro.Activitys.SettingActivity;
import com.kerolsmm.incognitopro.Data.AppExecutors;
import com.kerolsmm.incognitopro.Data.BookMark;
import com.kerolsmm.incognitopro.Data.Data_name;
import com.kerolsmm.incognitopro.Data.MvvmTab;
import com.kerolsmm.incognitopro.Data.Tab;
import com.kerolsmm.incognitopro.DownloaderApp.DownloadModel;
import com.kerolsmm.incognitopro.DownloaderApp.DownloaderService;
import com.kerolsmm.incognitopro.Fragments.InfoDialog;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.databinding.ActivityHomeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class More {


    private PopupWindow myPop;
    private WebView webView;
    private ArrayList<Tab> arrayList;
    private int mPosition;
    private Home home;
    private MvvmTab mvvmTab;
    private onFindPage findPage;

    public More(Home home , MvvmTab mvvmTab , onFindPage findPage) {
        this.home = home;
        this.mvvmTab = mvvmTab;
        this.findPage  = findPage;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setPopUpWindow(LayoutInflater layoutInflater, Data_name data_name , boolean isHide , ActivityHomeBinding binding) {

         View view = layoutInflater.inflate(R.layout.popup, binding.getRoot() ,false);

         myPop = new PopupWindow(view,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 true);
        SettingValue settingValue = new SettingValue(home);
        ImageView AddDownloadIcon = view.findViewById(R.id.AddDownloadIcon);
        ImageView Foreword = view.findViewById(R.id.Next);
        ImageView AddBookmarkIcon = view.findViewById(R.id.AddBookMarkIcon);
        ImageView ReloadIcon = view.findViewById(R.id.ReloadIcon);
        ImageView InfoIcon = view.findViewById(R.id.InfoIcon);

        View btn1 = view.findViewById(R.id.btn1);
        View btn4 = view.findViewById(R.id.btn4);
        TextView AddToScreen = view.findViewById(R.id.AddToScreen);

        TextView AddTab = view.findViewById(R.id.AddTabIcon);
        TextView DownloadIcon = view.findViewById(R.id.DownloadIcon);
        TextView mBookmark = view.findViewById(R.id.BookMarkIcon);
        TextView ShareIcon = view.findViewById(R.id.ShareIcon);
        TextView FindPageIcon = view.findViewById(R.id.FindPageIcon);
        MaterialSwitch DesktopMode = view.findViewById(R.id.DesktopModeIcon);
        TextView SettingIcon = view.findViewById(R.id.SettingIcon);
        TextView cancelpop = view.findViewById(R.id.cancelpop);
        ImageView DownloadPage = view.findViewById(R.id.downloadpage);


        if (isHide){
            btn1.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
        }else {
            btn1.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
        }


        try {
            if (getWebView().getProgress() == 100) {
                ReloadIcon.setImageResource(R.drawable.reload_svgrepo_com);
            }else {
                ReloadIcon.setImageResource(R.drawable.ic_multiply_cancel_svgrepo_com);
            }

            String url = getWebView().getOriginalUrl();
            String TitileSite = getWebView().getTitle();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    BookMark bookmark = data_name.kerols().LOLO(url);
                    if ( bookmark == null) {
                      AppExecutors.getInstance().mainThread().execute(new Runnable() {
                          @Override
                          public void run() {
                              AddBookmarkIcon.setImageResource(R.drawable.ic_star_svgrepo_com__1_);
                          }
                      });
                    }else {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                AddBookmarkIcon.setImageResource(R.drawable.ic_star_svgrepo_com);

                            }
                        });
                    }
                }
            });


        }catch (RuntimeException runtimeException) {

        }

        if (settingValue.isDesktopMode()){
            DesktopMode.setChecked(true);
        }else {
            DesktopMode.setChecked(false);
        }


        DownloadPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(home, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED )  {
                        ActivityCompat.requestPermissions(home,new String[]{Manifest.permission.POST_NOTIFICATIONS},1200);
                    }else  {
                        String url = webView.getUrl();
                        String filename = webView.getTitle() + ".html";
                                new Thread(new Runnable() {
                            @Override
                            public void run() {



                        DownloadModel downloadModel = new DownloadModel("",url,"attachment",filename);
                        Intent intent = new Intent(home, DownloaderService.class);
                        intent.putExtra("mimeType",downloadModel.mimeType);
                        intent.putExtra("url",downloadModel.url);
                        intent.putExtra("attachment",downloadModel.attachment);
                        intent.putExtra("filename",downloadModel.fileName);
                        home.startService(intent);
                            }
                        }).start();
                    }
                }else  {
                    String url = webView.getUrl();
                    String filename = webView.getTitle() + ".html";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                    DownloadModel downloadModel = new DownloadModel("",url,"attachment",filename);
                    Intent intent = new Intent(home, DownloaderService.class);
                    intent.putExtra("mimeType",downloadModel.mimeType);
                    intent.putExtra("url",downloadModel.url);
                    intent.putExtra("attachment",downloadModel.attachment);
                    intent.putExtra("filename",downloadModel.fileName);
                    home.startService(intent);
                        }
                    }).start();
                }

                myPop.dismiss();

            }
        });


        DesktopMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                settingValue.setDesktopMode(b);
                setDesktopMode(b,getWebView().getSettings(),home);
                settingValue.getEditor().apply();
                //myPop.dismiss();

                try {
                    webView.reload();
                    myPop.dismiss();
                }catch (RuntimeException runtimeException) {

                }
            }
        });


        AddToScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createShortcut(home, getWebView().getUrl(), getWebView().getTitle(),getWebView().getFavicon());
                myPop.dismiss();

            }
        });


        TextView print = view.findViewById(R.id.PrintWeb);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPrintJob(webView);
            }
        });


        AddTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAddTab();
                myPop.dismiss();
            }
        });

        FindPageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPage.onSearchPage();
                myPop.dismiss();
            }
        });
        mBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent(home, BookmarkActivity.class);
                    home.startActivity(intent);
                    myPop.dismiss();

                }catch (RuntimeException runtimeException) {

                }
            }
        });

        DownloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                Intent intent = new Intent(home, DownloadActivity.class);
                home.startActivity(intent);
                myPop.dismiss();


                }catch (RuntimeException runtimeException) {

                }
            }
        });

        ShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {

                        final Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra( Intent.EXTRA_TEXT, getWebView().getOriginalUrl().toString());
                        intent.setType("text/plain");
                        home.startActivity(intent);
                        myPop.dismiss();

                        }catch (ActivityNotFoundException a){

                            Toast.makeText(home, "You don't have apps to run", Toast.LENGTH_SHORT).show();

                        }
                   }
             });

        Foreword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if (getWebView().canGoForward()){
                       getWebView().goForward();
                       try {
                           myPop.dismiss();
                       }catch (RuntimeException runtimeException) {

                       }
                   }
            }
        });

        ReloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (getWebView().getProgress() == 100) {
                        getWebView().reload();
                    }else {
                        getWebView().stopLoading();
                    }
                    myPop.dismiss();
            }
        });

        SettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home, SettingActivity.class);
                home.startActivity(intent);
                myPop.dismiss();
            }
        });


        cancelpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myPop.dismiss();
                }catch (RuntimeException runtimeException) {

                }
            }
        });


        AddDownloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        InfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog infoDialog = new InfoDialog(getWebView());
                infoDialog.show(home.getSupportFragmentManager(),infoDialog.getTag());
                myPop.dismiss();
            }
        });
        AddBookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                String url = getWebView().getOriginalUrl();
                String TitileSite = getWebView().getTitle();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        BookMark bookmark = data_name.kerols().LOLO(url);
                        if ( bookmark == null) {
                             data_name.kerols().insertBookMark(new BookMark(TitileSite,url));
                              AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(home,home.getString(R.string.AddBookmark),Toast.LENGTH_SHORT).show();
                                  }
                              });
                        }else {
                             data_name.kerols().DeleteFileHistory(url);
                        }
                    }
                });
                myPop.dismiss();
            }catch (RuntimeException runtimeException) {

            }
            }
        });
         myPop.setElevation(10);
         setMyPop(myPop);
    }

  /*  private void setDesktopMode(final boolean enabled , WebView webView) {
        final WebSettings webSettings = webView.getSettings();
        final String desktopUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36";
        // webView.clearCache(true);
        webSettings.setUserAgentString(enabled ? desktopUA : null);
        webSettings.setUseWideViewPort(enabled);
        webView.reload();
        try {
            myPop.dismiss();
        }catch (RuntimeException runtimeException) {

        }

    }*/
    public PopupWindow getMyPop() {
        return myPop;
    }


    private void  setMyPop (PopupWindow myPop){
        this.myPop = myPop;

    }

    public void setWebView(WebView webView , ArrayList<Tab> arrayList ,int mPosition ) {
        this.webView = webView;
        this.arrayList = arrayList;
        this.mPosition = mPosition;
    }

    private WebView getWebView() {
        return webView;
    }

    public ArrayList<Tab> getArrayList() {
        return arrayList;
    }


    private void  NewAddTab () {
        mvvmTab.setDoAnyThing("about:blank");
        myPop.dismiss();
    }

  public   interface  onFindPage {
        void onSearchPage ( ) ;
    }

    public  void createShortcut(@NonNull Activity activity,
                                      @NonNull String url,
                                      @NonNull String unsafeTitle,
                                      Bitmap unsafeFavicon) {
        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.setClassName("com.kerolsmm.incognitopro","com.kerolsmm.incognitopro.Activitys.Home");
        shortcutIntent.setData(Uri.parse(url));

        final String title = TextUtils.isEmpty(unsafeTitle) ? activity.getString(R.string.app_name) : unsafeTitle;
        final Drawable webPageDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_world_svgrepo_com);
        final Bitmap webPageBitmap = DrawableKt.toBitmap(webPageDrawable,
                webPageDrawable.getIntrinsicWidth(),
                webPageDrawable.getIntrinsicHeight(), null);

        final Bitmap favicon = unsafeFavicon != null ? unsafeFavicon : webPageBitmap;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, favicon);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            activity.sendBroadcast(addIntent);
        } else {
            ShortcutManager shortcutManager = activity.getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {
                ShortcutInfo pinShortcutInfo =
                        new ShortcutInfo.Builder(activity, "browser-shortcut-" + url.hashCode())
                                .setIntent(shortcutIntent)
                                .setIcon(Icon.createWithBitmap(favicon))
                                .setShortLabel(title)
                                .build();

                shortcutManager.requestPinShortcut(pinShortcutInfo, null);
            } else {

            }
        }
    }
    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) home
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = home.getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking

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

}


package com.kerolsmm.incognitopro;

import static com.kerolsmm.incognitopro.FileUtils.DEFAULT_DOWNLOAD_PATH;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kerolsmm.incognitopro.Activitys.Home;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtilt {


    String HTTP = "http://";
    String HTTPS = "https://";
    String FILE = "file://";
    String ABOUT = "about:";
    String FOLDER = "folder://";

    String url  = " ";
    String contentDisposition  = " ";
    String mimetype  = " ";
    String userAgent  = " ";
    long contentLengt = 0;
    private long id_ = 0;
    private DownloadManager dm;
    String filenameApp ;
    private static final String COOKIE_REQUEST_HEADER = "Cookie";
    private final Context context;




    public DownloadUtilt (Context context) {
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context;
    }



    public void mStartContext (String url ,String contentDisposition ,
                        String mimetype ,
                        String userAgent ,
                        String filenameApp) {
        this.url = url;
        this.filenameApp = filenameApp;
        this.contentDisposition = contentDisposition;
        this.userAgent = userAgent;
        this.mimetype = mimetype;

    }



    public void mStart (String url ,String contentDisposition ,
                        String mimetype ,
                        String userAgent ,
                        long contentLength) {
try {


        final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);

        WebAddress webAddress;
        try {
            webAddress = new WebAddress(url);
            webAddress.setPath(encodePath(webAddress.getPath()));
        } catch (Exception e) {
            return;
        }

        String addressString = webAddress.toString();
        Uri uri = Uri.parse(addressString);
        final DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(uri);
        } catch (IllegalArgumentException e) {
            // ActivityExtensions.snackbar(context, R.string.cannot_download);
            return;
        }

        // set downloaded file destination to /sdcard/Download.
        // or, should it be set to one of several Environment.DIRECTORY* dirs
        // depending on mimetype?

        String location = DEFAULT_DOWNLOAD_PATH;
        location = FileUtils.addNecessarySlashes(location);
        Uri downloadFolder = Uri.parse(location);

        if (!isWriteAccessAvailable(downloadFolder)) {
            Log.e("TAG", "mStart: ");
            return;
        }

        String newMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(guessFileExtension(filename));


        request.setMimeType(newMimeType);
        request.setDestinationUri(Uri.parse(FILE + location + filename));
        // let this downloaded file be scanned by MediaScanner - so that it can
        // show up in Gallery app, for example.
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setDescription(webAddress.getHost());
        // XXX: Have to use the old url since the cookies were stored using the
        // old percent-encoded url.
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader(COOKIE_REQUEST_HEADER, cookies);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //noinspection VariableNotUsedInsideIf
        if (mimetype == null) {
            //*logger.log(TAG, "Mimetype is null");*//*
            if (TextUtils.isEmpty(addressString)) {
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getStart(addressString,cookies,userAgent,request);
                }
            }).start();
            Toast.makeText(context,"Start Download...",Toast.LENGTH_SHORT).show();

        }else {
            try {
                Toast.makeText(context,"Start Download...",Toast.LENGTH_SHORT).show();
                dm.enqueue(request);
            } catch (IllegalArgumentException e) {

            } catch (SecurityException e) {
                // TODO write a download utility that downloads files rather than rely on the system
                // because the system can only handle Environment.getExternal... as a path
                //*ActivityExtensions.snackbar(context, R.string.problem_location_download);*//*
            }
        }
}catch (RuntimeException runtimeException ) {
    Log.e("TAG", "mStart: ", runtimeException );
}
    }


    @Nullable
    public static String guessFileExtension(@NonNull String filename) {
        int lastIndex = filename.lastIndexOf('.') + 1;
        if (lastIndex > 0 && filename.length() > lastIndex) {
            return filename.substring(lastIndex);
        }
        return null;
    }

    private static boolean isWriteAccessAvailable(@NonNull Uri fileUri) {
        if (fileUri.getPath() == null) {
            return false;
        }
        File file = new File(fileUri.getPath());

        if (!file.isDirectory() && !file.mkdirs()) {
            file.mkdirs();
            return false;
        }

        try {
            if (file.createNewFile()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public void getStart(String mUri, String mCookies,
                                String mUserAgent,
                                DownloadManager.Request mRequest
                                ) {
        String mimeType = null;
        String contentDisposition = null;
        HttpURLConnection connection = null;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        try {
            URL url = new URL(mUri);
            connection = (HttpURLConnection) url.openConnection();
            if (mCookies != null && !mCookies.isEmpty()) {
                connection.addRequestProperty("Cookie", mCookies);
                connection.setRequestProperty("User-Agent", mUserAgent);
            }
            connection.connect();
            // We could get a redirect here, but if we do lets let
            // the download manager take care of it, and thus trust that
            // the server sends the right mimetype
            if (connection.getResponseCode() == 200) {
                String header = connection.getHeaderField("Content-Type");
                if (header != null) {
                    mimeType = header;
                    final int semicolonIndex = mimeType.indexOf(';');
                    if (semicolonIndex != -1) {
                        mimeType = mimeType.substring(0, semicolonIndex);
                    }
                }
                String contentDispositionHeader = connection.getHeaderField("Content-Disposition");
                if (contentDispositionHeader != null) {
                    contentDisposition = contentDispositionHeader;
                }
            }
        } catch (@NonNull IllegalArgumentException | IOException ex) {
            if (connection != null)
                connection.disconnect();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        if (mimeType != null) {
            if (mimeType.equalsIgnoreCase("text/plain")
                    || mimeType.equalsIgnoreCase("application/octet-stream")) {
                String newMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        guessFileExtension(mUri));
                if (newMimeType != null) {
                    mRequest.setMimeType(newMimeType);
                }
            }
            final String filename = URLUtil.guessFileName(mUri, contentDisposition, mimeType);
            mRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        }

        // Start the download
        try {
            downloadManager.enqueue(mRequest);
        } catch (IllegalArgumentException e) {
            // Probably got a bad URL or something
            Log.e("TAG", "Unable to  request", e);

        } catch (SecurityException e) {
            // TODO write a download utility that downloads files rather than rely on the system
            // because the system can only handle Environment.getExternal... as a path

        }
    }



    public  void onDownloadStartNoStream(String url, String userAgent, String contentDisposition,
                                         String mimetype, String fileName, boolean privateBrowsing) {
         DownloadManager.Request request;

        // java.net.URI is a lot stricter than KURL so we have to encode some
        // extra characters. Fix for b 2538060 and b 1634719
        WebAddress webAddress;
        try {
            webAddress = new WebAddress(url);
            webAddress.setPath(encodePath(webAddress.getPath()));
        } catch (Exception e) {
            // This only happens for very bad urls, we want to chatch the
            // exception here
            Log.e("browser", "Exception trying to parse url:" + url);
            return;
        }
        String addressString = webAddress.toString();
        Uri uri = Uri.parse(addressString);
        try {
            request = new DownloadManager.Request(uri);
        } catch (IllegalArgumentException e) {
            return;
        }
        request.setTitle(fileName);

        request.setMimeType(mimetype);
        // set downloaded file destination to /sdcard/Download.
        // or, should it be set to one of several Environment.DIRECTORY* dirs depending on mimetype?
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        // let this downloaded file be scanned by MediaScanner - so that it can
        // show up in Gallery app, for example.
        request.allowScanningByMediaScanner();
        request.setDescription(webAddress.getHost());
        // XXX: Have to use the old url since the cookies were stored using the
        // old percent-encoded url.
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", userAgent);
        //request.addRequestHeader("Referer", referer);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Toast.makeText(context,"Start Download...",Toast.LENGTH_SHORT).show();
        if (mimetype == null) {
            if (TextUtils.isEmpty(addressString)) {
                return;
            }
            // We must have long pressed on a link or image to download it. We
            // are not sure of the mimetype in this case, so do a head request
            mimetype = "";
            new Thread(new Runnable() {
                @Override
                public void run() {
                }
            }).start();
        } else {
            new Thread("Browser download") {
                public void run() {
                    enqueDownload(context,request);

                }
            }.start();
        }
    }



    public void startWithOutContext () {
        DownloadManager.Request request;

        // java.net.URI is a lot stricter than KURL so we have to encode some
        // extra characters. Fix for b 2538060 and b 1634719
        WebAddress webAddress;
        try {
            webAddress = new WebAddress(url);
            webAddress.setPath(encodePath(webAddress.getPath()));
        } catch (Exception e) {
            // This only happens for very bad urls, we want to chatch the
            // exception here
            Log.e("browser", "Exception trying to parse url:" + url);
            return;
        }
        String addressString = webAddress.toString();
        Uri uri = Uri.parse(addressString);
        try {
            request = new DownloadManager.Request(uri);
        } catch (IllegalArgumentException e) {
            return;
        }
        request.setTitle(filenameApp);

        request.setMimeType(mimetype);
        // set downloaded file destination to /sdcard/Download.
        // or, should it be set to one of several Environment.DIRECTORY* dirs depending on mimetype?
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filenameApp);
        // let this downloaded file be scanned by MediaScanner - so that it can
        // show up in Gallery app, for example.
        request.allowScanningByMediaScanner();
        request.setDescription(webAddress.getHost());
        // XXX: Have to use the old url since the cookies were stored using the
        // old percent-encoded url.
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", userAgent);
        //request.addRequestHeader("Referer", referer);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Toast.makeText(context,"Start Download...",Toast.LENGTH_SHORT).show();
        if (mimetype == null) {
            if (TextUtils.isEmpty(addressString)) {
                return;
            }
            // We must have long pressed on a link or image to download it. We
            // are not sure of the mimetype in this case, so do a head request
            mimetype = "";
            new Thread(new Runnable() {
                @Override
                public void run() {

                    getStart(addressString,cookies,userAgent,request);
                }
            }).start();
        } else {
            new Thread("Browser download") {
                public void run() {
                    enqueDownload(context,request);

                }
            }.start();
        }
    }

    public  boolean enqueDownload(Context c , DownloadManager.Request request){
        if (ContextCompat.checkSelfPermission(c,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||  Build.VERSION.SDK_INT > Build.VERSION_CODES.Q ) {

            DownloadManager manager
                    = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);

              if (manager != null) {
                manager.enqueue(request);
            }

            return true;
        }else
            return false;

    }

    // This is to work around the fact that java.net.URI throws Exceptions
    // instead of just encoding URL's properly
    // Helper method for onDownloadStartNoStream
    private  String encodePath(String path) {
        char[] chars = path.toCharArray();
        boolean needed = false;
        for (char c : chars) {
            if (c == '[' || c == ']' || c == '|') {
                needed = true;
                break;
            }
        }
        if (needed == false) {
            return path;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c == '[' || c == ']' || c == '|') {
                sb.append('%');
                sb.append(Integer.toHexString(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}

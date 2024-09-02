package com.kerolsmm.incognito.DownloaderApp;

import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.OptIn;


import com.kerolsmm.incognito.DownloaderApp.FileDownloadListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class FileDownloader {


    public  FileDownloader () {

    }

    boolean isStop = false;


    private void downloadFile(String fileUrl, File destinationPath, FileDownloadListener listener) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set up input stream and output stream
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(destinationPath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;
            int fileSize = urlConnection.getContentLength();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (!isStop) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    // Calculate and notify progress
                    int progress = (int) (totalBytesRead * 100 / fileSize);
                    listener.onProgress(progress);
                } else  {
                    outputStream.close();
                    inputStream.close();
                    urlConnection.disconnect();
                    listener.onStop(destinationPath);
                    throw new Exception("onStop");

                }
            }

            // Close streams and connection
            outputStream.close();
            inputStream.close();
            urlConnection.disconnect();


            // File downloaded successfully
           listener.onComplete(destinationPath);

        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }


    public void startWithMime(String url , String mime , String contentDisposition , String fileName ,FileDownloadListener listener) {
        listener.onStart();
        if (url.startsWith("data:")) {  //when url is base64 encoded data
            downloadBate(url,listener,fileName);
            return;
        }

     /*   if (Objects.equals(mime, "") || mime.equals("application/force-download")) {
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



        }*/
        File destinationPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        downloadFile(url, destinationPath, listener);
    }


    private void downloadBate (String fileUrl , FileDownloadListener listener , String filename){

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, filename);

        if (!path.exists()) {
            path.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String base64EncodedString = fileUrl.substring(fileUrl.indexOf(",") + 1);
        byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
        int bytesWritten = 0;
        int totalBytes = decodedBytes.length;

        try {
            OutputStream os = new FileOutputStream(file);
            for (byte b : decodedBytes) {
                if (!isStop) {
                    os.write(b);
                    bytesWritten++;
                    int progress = (bytesWritten * 100) / totalBytes;
                    listener.onProgress(progress);
                } else {
                    os.write(decodedBytes);
                    os.close();
                    listener.onStop(file);
                    throw new Exception("onStop");
                }
            }

            os.write(decodedBytes);
            os.close();

            listener.onComplete(file);

        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }

    public static boolean hasExtension(String filename) {
        // Check if the filename contains a period (.)
        return filename.contains(".");
    }

    public static String getMimeTypeFromUrl(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            String contentType = connection.getContentType();
            connection.disconnect();
            return contentType;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error gracefully
        }
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


    public void stop ( boolean isStop ) {
        this.isStop = isStop;
    }
 }
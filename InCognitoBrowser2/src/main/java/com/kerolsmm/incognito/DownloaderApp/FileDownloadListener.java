package com.kerolsmm.incognito.DownloaderApp;

import java.io.File;

public interface FileDownloadListener {
    void onProgress(int progress);
    void onComplete(File filePath);
    void onError(String errorMessage);

    void onStart();

    void onStop (File file);



}
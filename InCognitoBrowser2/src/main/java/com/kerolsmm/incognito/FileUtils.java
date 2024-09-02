package com.kerolsmm.incognito;

import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A utility class containing helpful methods
 * pertaining to file storage.
 */
public final class FileUtils {

    private static final String TAG = "FileUtils";

    public static final String DEFAULT_DOWNLOAD_PATH =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    private FileUtils() {}

    @NonNull
    public static String addNecessarySlashes(@Nullable String originalPath) {
        if (originalPath == null || originalPath.length() == 0) {
            return "/";
        }
        if (originalPath.charAt(originalPath.length() - 1) != '/') {
            originalPath = originalPath + '/';
        }
        if (originalPath.charAt(0) != '/') {
            originalPath = '/' + originalPath;
        }
        return originalPath;
    }
}
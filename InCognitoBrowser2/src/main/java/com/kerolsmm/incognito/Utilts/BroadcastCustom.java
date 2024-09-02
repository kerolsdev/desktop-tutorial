package com.kerolsmm.incognito.Utilts;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kerolsmm.incognito.Data.AppExecutors;
import com.kerolsmm.incognito.Data.Data_name;
import com.kerolsmm.incognito.Data.FileModel;
import com.kerolsmm.incognito.R;

import java.io.File;

public class BroadcastCustom extends BroadcastReceiver {

    FileModel fileModel;
    public BroadcastCustom () {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            try {

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = manager.query(query);
                if (cursor.moveToFirst()) {
                    if (cursor.getCount() > 0) {
                        String uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                        String uri_http = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI));
                        int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            try {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Uri downloadUri = Uri.parse(uri);
                                            // get file name. if filename exists in contentDisposition, use it. otherwise, use the last part of the url.
                                            String filename = downloadUri.getLastPathSegment();
                                            Log.e("TAG", "onReceive: " + filename);
                                            Data_name data_name = Data_name.getInstance(context);
                                            File downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
                                            data_name.kerols().insertFile(new FileModel(filename, uri_http, downloadFile.getAbsolutePath()));
                                        } catch (RuntimeException ignored) {
                                        }
                                    }
                                });

                                Toast.makeText(context, R.string.downloadsuccess, Toast.LENGTH_SHORT).show();

                            } catch (RuntimeException runtimeException) {

                            }
                        } else {
                            Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
                            // So something here on failed.
                        }
                    }
                }
            } catch (RuntimeException runtimeException) {

            }
        }
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public FileModel getFileModel() {
        return fileModel;
    }
}

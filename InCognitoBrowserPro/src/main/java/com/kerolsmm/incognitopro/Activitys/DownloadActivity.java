package com.kerolsmm.incognitopro.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kerolsmm.incognitopro.Data.AppExecutors;
import com.kerolsmm.incognitopro.Data.Data_name;
import com.kerolsmm.incognitopro.Data.FileModel;
import com.kerolsmm.incognitopro.Data.RoomLiveDataDownlaod;
import com.kerolsmm.incognitopro.DownloaderApp.DownloadItem;
import com.kerolsmm.incognitopro.DownloaderApp.DownloaderService;
import com.kerolsmm.incognitopro.DownloaderApp.DownloaderTabs;
import com.kerolsmm.incognitopro.DownloaderApp.MimeUtils;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.Utilts.FileAdapter;
import com.kerolsmm.incognitopro.databinding.ActivityDownloadBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DownloadActivity extends AppCompatActivity implements FileAdapter.onClickDownload {

    private  ServiceConnection boundServiceConnection ;

    boolean isBound  = false;

    private ActivityDownloadBinding binding;
    private Data_name data_name;
    private ArrayList<FileModel> mFileModels;
    private FileAdapter fileAdapter;
    private FileModel fileModel;

    private DownloaderService.MyBinder binderBridge ;
    public  boolean isRuing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.ToolbarDownload);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

     /*   DownloaderTabs downloaderTabs = new DownloaderTabs(this);
        binding.viewpager2.setAdapter(downloaderTabs);
        binding.viewpager2.setOffscreenPageLimit(1);*/

       /* TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tablayout, binding.viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Download");
                }else if (position == 1) {
                    tab.setText("Progress");
                }
            }
        });

        tabLayoutMediator.attach();*/


      /*  data_name = Data_name.getInstance(getApplicationContext());
        fileAdapter = new FileAdapter(new ArrayList<>(),this,this);
        binding.listDownload.setAdapter(fileAdapter);
        RoomLiveDataDownlaod roomLiveDataDownlaod = new ViewModelProvider(this).get(RoomLiveDataDownlaod.class);

        roomLiveDataDownlaod.getTask().observe(this, new Observer<List<FileModel>>() {
            @Override
            public void onChanged(List<FileModel> fileModels) {
                mFileModels = new ArrayList<>(fileModels);

                   fileAdapter.setArrayList(mFileModels);
                if (mFileModels.isEmpty()){
                    binding.theListIsEmpty.setVisibility(View.VISIBLE);
                }else {
                    binding.theListIsEmpty.setVisibility(View.GONE);

                }

            }
        });
*/

        binding.progressBarDownloadProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadActivity.this,ProgressDownload.class);
                startActivity(intent);
            }
        });

        boundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                isBound = true;
                binderBridge = (DownloaderService.MyBinder) service ;
                if (binderBridge.getService().isRuing) {
                        binding.progressBarDownloadProgress.setVisibility(View.VISIBLE);
                    } else  {
                        binding.progressBarDownloadProgress.setVisibility(View.GONE);
                    }


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };



        data_name = Data_name.getInstance(this);
        fileAdapter = new FileAdapter(this,this);
        binding.listDownload.setAdapter(fileAdapter);
        RoomLiveDataDownlaod roomLiveDataDownlaod = new ViewModelProvider(this).get(RoomLiveDataDownlaod.class);

        roomLiveDataDownlaod.getTask().observe(this, new Observer<List<FileModel>>() {
            @Override
            public void onChanged(List<FileModel> fileModels) {
                mFileModels = new ArrayList<FileModel>(fileModels);
                fileAdapter.submitList(mFileModels);
                if (mFileModels.isEmpty()){
                    binding.theListIsEmpty.setVisibility(View.VISIBLE);
                }else {
                    binding.theListIsEmpty.setVisibility(View.GONE);

                }

            }
        });

        this.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.more_toolbar_download,menu);
                SearchView searchView = (SearchView) menu.findItem(R.id.search_download).getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        filter(newText);

                        return true;
                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        }, this , Lifecycle.State.RESUMED);


            Intent intent = new Intent(this, DownloaderService.class);
            bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);


    }

    private void filter(String text) {
        ArrayList<FileModel> filteredList = new ArrayList<>();

        for (FileModel item : mFileModels) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        fileAdapter.submitList(filteredList);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
    }

    @Override
    public void onPositionDownload(MenuItem Position, FileModel fileModel) {
        if (Position.getItemId() == R.id.delete_download){
            new MaterialAlertDialogBuilder(this)
                    .setTitle(fileModel.getName())
                    .setMessage(getString(R.string.Do_you_want_to_delete_the_file))
                    .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    File item =  new File(fileModel.getPath());
                                    data_name.kerols().deleteFile(fileModel);
                                    if (item.exists()){
                                        item.delete();
                                    }
                                }
                            });
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })

                    .show();


        }else if (Position.getItemId() == R.id.info_download){
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.info))
                    .setMessage("Name : " +  fileModel.getName() + "\n\n" +
                            "Url Download : " +  fileModel.getUrl() + "\n\n" +
                            "Path : " +  fileModel.getPath() + "\n\n")
                    .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    }).show();

        }else  {
            try {


                Uri pdfUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pdfUri = FileProvider.getUriForFile(this,"com.kerolsmm.incognitopro.provider" , new File(fileModel.getPath()));
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("*/*");
                    share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                    startActivity(Intent.createChooser(share, "Share file"));
                } else {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(fileModel.getPath());

                    if(fileWithinMyDir.exists()) {
                        intentShareFile.setType("*/*");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileModel.getPath()));

                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }

                }
            }catch (RuntimeException runtimeException) {
                Toast.makeText(this,"There is an error in the file",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClickDownloadFile(FileModel fileModel) {
        try {

            this.fileModel = fileModel;
            String mime = MimeUtils.getType(fileModel.getName());
            openFile(mime);
        }catch (RuntimeException runtimeException ) {
            Log.e("TAG", "onClickDownload: ", runtimeException );
        }
    }

    private String getMimeTypeFromFilePath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            String type = "*/*";
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                if (type == null) {
                    // If MIME type cannot be determined from extension, try to use URLConnection
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        type = URLConnection.guessContentTypeFromStream(fis);
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return type;
        }
        return "*/*";
    }

    private void openFileWithMimeType(String filePath, String mimeType) {
        // Create an Intent to open the file with the appropriate application
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);

        // Set the data URI with the file path and MIME type
        intent.setDataAndType(uri, mimeType);

        // Add flags to grant read permissions to other apps
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Verify that an app can handle the Intent before starting it
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle the case where no app is available to handle the file
        }
    }



    private String  getMimeType(String url) {
        String type = null;
        String extension = getExtension(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private String getExtension(String fileName)  {
        String encoded = "";
        try {
            encoded =  URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encoded = null;
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase(Locale.getDefault());
    }
    public void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Pass the package name of your app to open its settings
        Uri uri = Uri.fromParts("package",this.getPackageName(), null);
        intent.setData(uri);

        // Check if there's an activity that can handle the intent before launching
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openFile (String mimeType) {

        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri dirUri = Uri.parse(fileModel.getPath());
            Uri uri = Uri.parse("file://" + dirUri.getPath());
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mimeType);
            try {
                startActivity(Intent.createChooser(intent, fileModel.getName()));
            } catch (ActivityNotFoundException ee) {
                Toast.makeText(
                        this,
                        "You don't have apps to run",
                        Toast.LENGTH_SHORT
                ).show();
            }
        } catch (FileUriExposedException er) {
            try {
                Uri apkURI = FileProvider.getUriForFile(this,
                        "com.kerolsmm.incognitopro.provider", new File(fileModel.getPath()));
                Intent install = new Intent(Intent.ACTION_VIEW);

                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK) ;
                install.setDataAndType(apkURI, mimeType);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(install, fileModel.getName()));
            }catch (RuntimeException runtimeException) {
                Log.e("TAG", "openFile: ", runtimeException );
            }

        }catch (RuntimeException runtimeException) {
            Log.e("TAG", "openFile: ", runtimeException );
        }

    }
    
    
    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
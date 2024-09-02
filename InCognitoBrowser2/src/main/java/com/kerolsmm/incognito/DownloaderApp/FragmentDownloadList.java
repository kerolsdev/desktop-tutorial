package com.kerolsmm.incognito.DownloaderApp;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.FileUriExposedException;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kerolsmm.incognito.Activitys.DownloadActivity;
import com.kerolsmm.incognito.Data.AppExecutors;
import com.kerolsmm.incognito.Data.Data_name;
import com.kerolsmm.incognito.Data.FileModel;
import com.kerolsmm.incognito.Data.RoomLiveDataDownlaod;
import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.Utilts.FileAdapter;
import com.kerolsmm.incognito.databinding.ActivityDownloadBinding;
import com.kerolsmm.incognito.databinding.FragmentDownloadListBinding;
import com.kerolsmm.incognito.databinding.FragmentProgressDownloadBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentDownloadList extends Fragment implements FileAdapter.onClickDownload {


    public FragmentDownloadList () {

    }


    private Data_name data_name;
    private ArrayList<FileModel> mFileModels = new ArrayList<>();
    private FileAdapter fileAdapter;
    private FileModel fileModel;

    FragmentDownloadListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDownloadListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data_name = Data_name.getInstance(getContext());
        fileAdapter = new FileAdapter(requireActivity(),this);
        binding.listDownload.setAdapter(fileAdapter);
        RoomLiveDataDownlaod roomLiveDataDownlaod = new ViewModelProvider(this).get(RoomLiveDataDownlaod.class);

        roomLiveDataDownlaod.getTask().observe(getViewLifecycleOwner(), new Observer<List<FileModel>>() {
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

        requireActivity().addMenuProvider(new MenuProvider() {
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
        }, this.getViewLifecycleOwner() , Lifecycle.State.RESUMED);



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
    public void onPositionDownload(MenuItem Position, FileModel fileModel) {
        if (Position.getItemId() == R.id.delete_download){
            new MaterialAlertDialogBuilder(this.requireActivity())
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
            new MaterialAlertDialogBuilder(this.requireActivity())
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
                pdfUri = FileProvider.getUriForFile(requireActivity(),"com.kerolsmm.incognito.provider" , new File(fileModel.getPath()));
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
   Toast.makeText(requireContext(),"There is an error in the file",Toast.LENGTH_SHORT).show();
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
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
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
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);

        // Check if there's an activity that can handle the intent before launching
        if (intent.resolveActivity( requireActivity().getPackageManager()) != null) {
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
                        getContext(),
                        "You don't have apps to run",
                        Toast.LENGTH_SHORT
                ).show();
            }
        } catch (FileUriExposedException er) {
            try {
                Uri apkURI = FileProvider.getUriForFile( requireActivity(),
                        "com.kerolsmm.incognito.provider", new File(fileModel.getPath()));
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

}
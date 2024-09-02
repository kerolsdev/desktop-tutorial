package com.kerolsmm.incognitopro.DownloaderApp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DownloaderTabs extends FragmentStateAdapter {
    public DownloaderTabs(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return   new FragmentDownloadList();
        }else {
            return  new FragmentProgressDownload();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}

package com.wifinet.internetcheck.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentViewPager extends FragmentStateAdapter {
    public FragmentViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
             return new HomeFragment();
    }else if (position == 1) {
            return new NotificationFragment();
        }else
            {
            return new SettingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

package com.wifinet.internetcheck.wifiana;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewStateAdapter extends FragmentStateAdapter {


    public ViewStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Hardcoded in this order, you'll want to use lists and make sure the titles match
            if (position == 0) {return new Fragment_list();
            }else if (position == 1)
            {return new Fragment_Gz(); }
            else if (position == 2){
                return new FragmentDiagram5GHz();
            }else  {
                return new FragmentDiagram6GHz();
            }

    }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return 4;
        }
    }
package com.kerols.phoneboost.Fragments

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            UserApps()
        } else {
            SystemApps()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
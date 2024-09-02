package com.kerols.phoneboost.Utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerols.phoneboost.Fragments.SystemApps
import com.kerols.phoneboost.Fragments.UserApps

class ViewpagerUi(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
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
package com.kerols.appmanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerols.appmanager.fragments.BatteryFragment
import com.kerols.appmanager.fragments.NetworkFragment
import com.kerols.appmanager.fragments.PermissionFragment
import com.kerols.appmanager.fragments.SystemAppFragment
import com.kerols.appmanager.fragments.UsersAppFragment


public class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {



    override fun getItemCount(): Int {
         return 5
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {

            0 -> {
                return  UsersAppFragment ()
            }

            1 -> {
                return  SystemAppFragment ()
            }

            2 -> {
                return  BatteryFragment ()

            }
            3 -> {
                return  NetworkFragment ()
            }
            else -> {
                return  PermissionFragment()
            }
        }
    }
}
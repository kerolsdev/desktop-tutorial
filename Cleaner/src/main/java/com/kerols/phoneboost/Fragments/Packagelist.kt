package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kerols.phoneboost.R
import com.kerols.phoneboost.databinding.FragmentPackagelistBinding

class Packagelist : Fragment() {



    private lateinit var binding: FragmentPackagelistBinding
    private val fragmentSearchViewModel: FragmentSearchViewModel by activityViewModels()
    var positionItem : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPackagelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = FragmentAdapter(requireActivity())

        /*val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)*/

         val menuHost = requireActivity()
         menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menusearch, menu)
                val myActionMenuItem = menu.findItem(R.id.action_search)
                val searchView = myActionMenuItem.actionView as SearchView?
                searchView?.queryHint = getString(R.string.search);
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String?): Boolean {
                        if (positionItem == 0){
                            s?.let { fragmentSearchViewModel.setValue(it) }
                        } else {
                            s?.let { fragmentSearchViewModel.setValueTwo(it) }
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        binding.viewPager2.adapter = fragmentAdapter
        binding.let {
            TabLayoutMediator(it.tabs, binding.viewPager2) { tab, position ->
                if (position == 0) {
                    tab.text = "User Apps"
                } else {
                    tab.text = "System Apps"
                }
            }.attach()
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
            override fun onPageSelected(position: Int) {
               positionItem = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

    }
}
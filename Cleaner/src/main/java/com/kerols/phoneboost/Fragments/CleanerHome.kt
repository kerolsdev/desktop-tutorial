package com.kerols.phoneboost.Fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kerols.phoneboost.Utils.Utils
import com.kerols.phoneboost.databinding.FragmentCleanerHomeBinding

class CleanerHome : Fragment() {
    private lateinit var binding: FragmentCleanerHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCleanerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)*/

        val ut = Utils(requireContext())
        binding.Apk.isChecked = ut.getApk()
        binding.Junks.isChecked = ut.getJunks()
        binding.Corpse.isChecked = ut.getCorpse()
        binding.Empty.isChecked = ut.getEmpty()
        binding.thumb.isChecked = ut.getThumb()
        binding.CachedApps.isChecked = ut.getCachedApps()
        binding.VisiableCached.isChecked = ut.getVisibleCached()


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            binding.CachedAppsLayout.visibility = View.VISIBLE
            binding.CachedVisiableAppsLayout.visibility = View.GONE
        }else {
            binding.CachedAppsLayout.visibility = View.GONE
            binding.CachedVisiableAppsLayout.visibility = View.VISIBLE
        }



        binding.btnclick.setOnClickListener(View.OnClickListener { v ->

            if (binding.Apk.isChecked
                ||  binding.Junks.isChecked
                || binding.Corpse.isChecked
                || binding.Empty.isChecked
                || binding.thumb.isChecked || binding.CachedApps.isChecked || binding.VisiableCached.isChecked ) {


                    ut.saveApk(binding.Apk.isChecked)
                    ut.saveEmpty(binding.Empty.isChecked)
                    ut.saveJunks(binding.Junks.isChecked)
                    ut.saveCorpse(binding.Corpse.isChecked)
                    ut.saveCachedApps(binding.CachedApps.isChecked)
                    ut.saveThumb(binding.thumb.isChecked)
                    ut.saveVisibleCached(binding.VisiableCached.isChecked)


                val boolean = BooleanArray(7)
                boolean[0] = binding.Empty.isChecked
                boolean[1] = binding.Corpse.isChecked
                boolean[2] = binding.Apk.isChecked
                boolean[3] = binding.Junks.isChecked
                boolean[4] = binding.thumb.isChecked
                boolean[5] = binding.CachedApps.isChecked
                boolean[6] = binding.VisiableCached.isChecked

                Navigation.findNavController(v).navigate(CleanerHomeDirections.actionCleanerHomeToCleanerProcess(boolean))
            }else {
                Toast.makeText(requireActivity(),"Choose at least one",Toast.LENGTH_LONG).show()
            }
        })
    }
}
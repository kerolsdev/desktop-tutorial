package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.kerols.phoneboost.adapter.AdapterCustom
import com.kerols.phoneboost.Model.AppInfo
import com.kerols.phoneboost.Model.AppInfo.Companion.getArray
import com.kerols.phoneboost.Activitys.MainActivity.Companion.runUi
import com.kerols.phoneboost.R
import com.kerols.phoneboost.databinding.FragmentListSystemBinding
import java.lang.RuntimeException
import java.util.ArrayList

class SystemApps : Fragment(), AdapterCustom.OnClick {
    var apps = ArrayList<String>()
    private lateinit var binding: FragmentListSystemBinding
    private val fragmentSearchViewModel: FragmentSearchViewModel by activityViewModels()
    private var appInfos  = ArrayList<AppInfo>()
    private lateinit var adapterCustom : AdapterCustom

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCustom = AdapterCustom(ArrayList<AppInfo>(), requireContext(), this)

        Thread {

            try {

            appInfos = getArray(true, activity)
            runUi.post(Runnable {
                adapterCustom.setAppInfos(appInfos)
                binding.animationView.visibility = View.GONE
            })

            }catch (re : RuntimeException ) {
                Log.e("TAG", "onViewCreated: ",re )
            }
        }.start()

        binding.removeAppSystem.setOnClickListener(View.OnClickListener { v ->
            var AppsArray: Array<String> = arrayOf()
            AppsArray = apps.toArray(AppsArray)
            Navigation.findNavController(v)
                .navigate(PackagelistDirections.actionPackagelistToPackageScan(AppsArray))
        })

        binding.appsSystem.adapter = adapterCustom
        fragmentSearchViewModel.getValueTwo().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            filter(it)
        })

    }

   override fun onEventClick(appInfo: AppInfo?, check: Boolean) {
       if (check) {
           appInfo!!.pack.let { apps.add(it) }
       } else {
           apps.remove(appInfo?.pack)
       }
       if (apps.isEmpty()) {
           binding.removeAppSystem.visibility = View.GONE
       } else {
           binding.removeAppSystem.visibility = View.VISIBLE
       }
       binding.removeAppSystem.text = "${getString(R.string.uninstall)} (${apps.size})"
   }

    private fun filter(text: String) {

        val filteredList: ArrayList<AppInfo> = ArrayList<AppInfo>()
        for (item in appInfos) {
            if (item.app_title?.lowercase()?.contains(text.lowercase()) == true) {
                filteredList.add(item)
            }
        }
        adapterCustom.setAppInfos(filteredList)

    }

    override fun onResume() {
        super.onResume()
        filter("")
    }
}
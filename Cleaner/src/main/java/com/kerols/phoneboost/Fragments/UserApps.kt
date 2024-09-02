package com.kerols.phoneboost.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.kerols.phoneboost.*
import com.kerols.phoneboost.Model.AppInfo.Companion.getArray
import com.kerols.phoneboost.Activitys.MainActivity.Companion.runUi
import com.kerols.phoneboost.Model.AppInfo
import com.kerols.phoneboost.Utils.Utils
import com.kerols.phoneboost.adapter.AdapterCustom
import com.kerols.phoneboost.databinding.FragmentListPackageBinding
import java.lang.RuntimeException
import kotlin.collections.ArrayList

class UserApps : Fragment(), AdapterCustom.OnClick {
    var apps = ArrayList<String>()
    private lateinit var binding: FragmentListPackageBinding
    private var appInfos : ArrayList<AppInfo>  = ArrayList<AppInfo>()
    private lateinit var adapterCustom : AdapterCustom
    private val fragmentSearchViewModel: FragmentSearchViewModel by activityViewModels()
    private val utils : Utils? by  lazy {
        context?.let { Utils(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListPackageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterCustom = AdapterCustom(ArrayList<AppInfo>(), requireContext(), this)

        Thread {
            try {

                appInfos = getArray(false, activity)

                runUi.post( Runnable {
                    appInfos.let { adapterCustom.setAppInfos(it) }
                    binding.animationView.visibility = View.GONE
                })

            }catch (re : RuntimeException) {
                Log.e("TAG", "onViewCreated: ",re )
            }


             utils?.saveApps(true)

        }.start()


        binding.removeAppUser.setOnClickListener(View.OnClickListener { v ->
            var AppsArray: Array<String> = arrayOf()
            AppsArray = apps.toArray(AppsArray)
            Navigation.findNavController(v).navigate(PackagelistDirections.actionPackagelistToPackageScan(AppsArray))
        })

        binding.appsUser.adapter = adapterCustom

        fragmentSearchViewModel.getValue().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
            binding.removeAppUser.visibility = View.GONE
        } else {
            binding.removeAppUser.visibility = View.VISIBLE
        }

        binding.removeAppUser.text = "${getString(R.string.uninstall)} (${apps.size})"
    }
    private fun filter(text: String) {

        val filteredList: ArrayList<AppInfo> = ArrayList<AppInfo>()
        for (item in appInfos ?: ArrayList()) {
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
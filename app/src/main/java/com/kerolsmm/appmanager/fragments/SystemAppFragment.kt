package com.kerolsmm.appmanager.fragments

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.adapters.AdapterSystemApp
import com.kerolsmm.appmanager.databinding.FragmentSystemAppBinding
import com.kerolsmm.appmanager.dialogs.BottomDialogSystem
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.functions.QuerySystemApp
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.Locale


public class SystemAppFragment : Fragment(),
    AdapterSystemApp.AppSystemOnClick, Observer<ArrayList<AppModel>> {

    private lateinit var binding: FragmentSystemAppBinding
    private lateinit var adapterUserApp: AdapterSystemApp
    private  var arrayList : ArrayList<AppModel> = ArrayList()
    private var finish : Boolean = false
    private val viewmodel by activityViewModels<MvvmData>()
    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()
    private val querySystemApp : QuerySystemApp = QuerySystemApp()
    private var mLastClickTime: Long = 0
    private  var arrayListCheck : ArrayList<AppModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSystemAppBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null && activity != null) {
            adapterUserApp = AdapterSystemApp(requireActivity(), this, ArrayList<AppModel>())
            binding.listAdapterSystem.adapter = adapterUserApp
            finish = false

            /*  binding.swipeRefresh.isEnabled = false
        val queryUserApp = QuerySystemApp()
        queryUserApp.readByRxJava(this,requireActivity())*/


            val menuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.search, menu)
                    val myActionMenuItem = menu.findItem(R.id.action_search)
                    val searchView = myActionMenuItem.actionView as SearchView?

                    searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(s: String?): Boolean {
                            if (finish) {
                                s?.let { filter(it) }
                            }
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return true
                }
            }, viewLifecycleOwner, androidx.lifecycle.Lifecycle.State.RESUMED)

            val pm: PackageManager? = context?.packageManager
            viewmodel.getSystemApp().observe(viewLifecycleOwner) {
                if (arrayList.isNotEmpty()) {
                    arrayList.clear()
                }

                adapterUserApp.setArray(it)

                arrayListCheck = it


                binding.progress.visibility = View.GONE
                //  binding.swipeRefresh.isEnabled = true
                if (adapterUserApp.itemCount == 0) {
                    binding.viewEmpty.visibility = View.VISIBLE
                    binding.listAdapterSystem.visibility = View.GONE
                } else {
                    binding.listAdapterSystem.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                }

                /*Thread(Runnable {

                    try {

                        for (appModel in arrayList) {
                            val appName = appModel.applicationInfo?.let { it1 ->
                                pm?.getApplicationLabel(it1).toString()
                            }
                            appModel.appTitle = appName ?: "App"
                           // arrayList.add(appModel)
                        }

                    } catch (re: RuntimeException) {
                    }

                    finish = true
                    *//* requireActivity().invalidateOptionsMenu()*//*
                }).start()*/

                Thread(Runnable {
                    val pm: PackageManager? = context?.packageManager
                    try {

                        for (appModel in it) {
                            val appM = AppModel(   appModel.sizeApp,
                                "appName",
                                appModel.appPackage,
                                null,
                                appModel.appSource,
                                appModel.applicationInfo)

                            val appName = appM.applicationInfo?.let { it1 ->
                                pm?.getApplicationLabel(it1).toString()
                            }
                            appModel.appTitle = appName ?: "App"
                            arrayList.add(appModel)
                        }

                    } catch (re: RuntimeException) {
                    }

                    finish = true
                    /* requireActivity().invalidateOptionsMenu()*/
                }).start()

            }

            binding.swipeRefresh.setOnRefreshListener {
            finish = false
            binding.swipeRefresh.isRefreshing = false;
            binding.swipeRefresh.isEnabled = false
            binding.listAdapterSystem.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
                finish = false
            querySystemApp.readByRxJava(this,requireActivity())
        }

        }
    }
  /*  @SuppressLint("ShowToast")
    override fun onPrepareOptionsMenu(menu: Menu) {
      Toast.makeText(context,"Finish",Toast.LENGTH_LONG).toString()
    }*/

    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
    override fun onDestroy() {
        super.onDestroy()
    }
     override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {
        binding.progress.visibility = View.GONE
        binding.swipeRefresh.isEnabled = true
        if (adapterUserApp.itemCount == 0){
            binding.viewEmpty.visibility = View.VISIBLE
            binding.listAdapterSystem.visibility = View.GONE
        }else {
            binding.listAdapterSystem.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
        }
    }

    override fun onNext(t: ArrayList<AppModel>) {
        if (arrayList.isNotEmpty()) {
            arrayList.clear()
        }
      //  arrayList.addAll(t)
        arrayListCheck = t
        Thread(Runnable {
            val pm: PackageManager? = context?.packageManager
            try {

                for (appModel in t) {
                    val appM = AppModel(   appModel.sizeApp,
                        "appName",
                        appModel.appPackage,
                        null,
                        appModel.appSource,
                        appModel.applicationInfo)

                    val appName = appM.applicationInfo?.let { it1 ->
                        pm?.getApplicationLabel(it1).toString()
                    }
                    appModel.appTitle = appName ?: "App"
                    arrayList.add(appModel)
                }

            } catch (re: RuntimeException) {
            }

            finish = true
            /* requireActivity().invalidateOptionsMenu()*/
        }).start()

        adapterUserApp.setArray(t)
    }


    override fun onClick(appModel: AppModel) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        mvvmBottomApp.setValueAppModel(appModel)
        val bottomDialogApp = BottomDialogSystem()
        bottomDialogApp.show(requireActivity().supportFragmentManager,bottomDialogApp.tag)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<AppModel> = ArrayList<AppModel>()
        for (item in arrayList) {
            if (item.appTitle.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        adapterUserApp.setArray(filteredList)
    }
}
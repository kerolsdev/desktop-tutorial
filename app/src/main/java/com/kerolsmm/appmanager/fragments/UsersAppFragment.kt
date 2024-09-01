package com.kerolsmm.appmanager.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_FIRST_USER
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.adapters.AdapterUserApp
import com.kerolsmm.appmanager.databinding.FragmentUsersAppBinding
import com.kerolsmm.appmanager.dialogs.BottomDialogApp
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.functions.QueryUserApp
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.Locale


public class UsersAppFragment : Fragment(),
    AdapterUserApp.AppUserOnClick, BottomDialogApp.ClickBottomDialog,
    Observer<ArrayList<AppModel>> {


    private lateinit var binding: FragmentUsersAppBinding
    private lateinit var adapterUserApp: AdapterUserApp
    private  var arrayList : ArrayList<AppModel> = ArrayList()
    private var finish : Boolean = false
    private var model : AppModel? = null
    private val mvvmData : MvvmData by activityViewModels()
    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()
    private  var arrayListCheck : ArrayList<AppModel> = ArrayList()

    private lateinit var someActivityResultLauncher : ActivityResultLauncher<Intent>
    private val bottomDialogApp = BottomDialogApp(this)

    private val queryUserApp : QueryUserApp = QueryUserApp()

    private var mLastClickTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersAppBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null && activity != null) {

            finish = false
            adapterUserApp = AdapterUserApp(requireActivity(), this, ArrayList<AppModel>())
            binding.listAppUser.adapter = adapterUserApp

            //  binding.swipeRefresh.isEnabled = false

            /*   val queryUserApp = QueryUserApp()
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


            someActivityResultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK && finish) {
                        try {
                            finish = false
                            binding.swipeRefresh.isRefreshing = false;
                            binding.swipeRefresh.isEnabled = false
                            binding.listAppUser.visibility = View.GONE
                            binding.progress.visibility = View.VISIBLE
                            binding.viewEmpty.visibility = View.GONE
                            arrayList.remove(model)
                            arrayListCheck.remove(model)
                           // adapterUserApp.setArray(arrayList)
                            mvvmData.setUserApp(arrayListCheck)
                        } catch (re: RuntimeException) {

                        }
                    } else if (result.resultCode == RESULT_CANCELED) {
                        // Log.d("TAG", "onActivityResult: user canceled the (un)install");
                    } else if (result.resultCode == RESULT_FIRST_USER) {
                        //  Log.d("TAG", "onActivityResult: failed to (un)install");
                    }
                }
            val pm: PackageManager? = context?.packageManager

            mvvmData.getUserApp().observe(viewLifecycleOwner) {
              //   binding.progress.visibility = View.GONE
                 //  binding.swipeRefresh.isEnabled = true
                  arrayListCheck = it

                if (arrayList.isNotEmpty()) {
                    arrayList.clear()
                }
                adapterUserApp.setArray(it)

                binding.progress.visibility = View.GONE
                binding.swipeRefresh.isEnabled = true

                if (it.size == 0) {
                    binding.viewEmpty.visibility = View.VISIBLE
                    binding.listAppUser.visibility = View.GONE
                } else {
                    binding.listAppUser.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                }
                Thread(Runnable {
                    try {

                        for (appModel in it) {
                            val appM = AppModel(   appModel.sizeApp,
                                "appName",
                                appModel.appPackage,
                                null,
                                appModel.appSource,
                                appModel.applicationInfo)
                            val appName = appModel.applicationInfo?.let { it1 ->
                                pm?.getApplicationLabel(it1).toString()
                            }
                            appM.appTitle = appName ?: "App"
                            arrayList.add(appM)
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
            binding.listAppUser.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
            finish = false
            queryUserApp.readByRxJava(this,requireActivity())
        }
        }

    }


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
        if (arrayListCheck.size == 0){
            binding.viewEmpty.visibility = View.VISIBLE
            binding.listAppUser.visibility = View.GONE
        }else {
            binding.listAppUser.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
        }
    }

    override fun onNext(t: ArrayList<AppModel>) {
        arrayListCheck = t
        if (arrayList.isNotEmpty()) {
            arrayList.clear()
        }
        /*arrayList.addAll(t)*/
        /*arrayList = t*/
        val pm: PackageManager? = context?.packageManager
        Thread(Runnable {
            try {

                for (appModel in t) {
                    val appM = AppModel(   appModel.sizeApp,
                        "appName",
                        appModel.appPackage,
                        null,
                        appModel.appSource,
                        appModel.applicationInfo)
                    val appName = appModel.applicationInfo?.let { it1 ->
                        pm?.getApplicationLabel(it1).toString()
                    }
                    appM.appTitle = appName ?: "App"
                    arrayList.add(appM)
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
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        try {
            mvvmBottomApp.setValueAppModel(appModel)
            bottomDialogApp.show(requireActivity().supportFragmentManager,bottomDialogApp.tag)
        }catch (re : RuntimeException) {

        }
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

    override fun onClickBottom(packageName: AppModel) {
        model = packageName
        uninstallApp(requireContext(),packageName.appPackage)
    }

    private fun uninstallApp(context: Context, packageName: String) {
        try {

            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:$packageName")
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            someActivityResultLauncher.launch(intent)

        }catch (re : RuntimeException) {

        }

    }


}
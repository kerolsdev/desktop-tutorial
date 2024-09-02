package com.kerols.appmanager.fragments

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_FIRST_USER
import android.app.Activity.RESULT_OK
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.manager.Lifecycle
import com.kerols.appmanager.R
import com.kerols.appmanager.adapters.AdapterUserApp
import com.kerols.appmanager.databinding.FragmentUsersAppBinding
import com.kerols.appmanager.dialogs.BottomDialogApp
import com.kerols.appmanager.functions.MvvmData
import com.kerols.appmanager.functions.QueryUserApp
import com.kerols.appmanager.model.AppModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale


public class UsersAppFragment : Fragment(),
    AdapterUserApp.AppUserOnClick, BottomDialogApp.ClickBottomDialog {



    private lateinit var binding: FragmentUsersAppBinding
    private lateinit var adapterUserApp: AdapterUserApp
    private  var arrayList : ArrayList<AppModel> = ArrayList()
    private var finish : Boolean = false
    private var model : AppModel? = null
    val mvvmData : MvvmData by activityViewModels()

    lateinit var someActivityResultLauncher : ActivityResultLauncher<Intent>


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

        adapterUserApp = AdapterUserApp(requireActivity() , this, ArrayList<AppModel>())
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


        someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && finish) {
              try {
                  arrayList.remove(model)
                  adapterUserApp.setArray(arrayList)
              }catch (re : RuntimeException ) {

              }
            } else if (result.resultCode == RESULT_CANCELED) {
               // Log.d("TAG", "onActivityResult: user canceled the (un)install");
            } else if (result.resultCode == RESULT_FIRST_USER) {
              //  Log.d("TAG", "onActivityResult: failed to (un)install");
            }
        }
        val pm: PackageManager? = context?.packageManager

        mvvmData.getUserApp().observe(viewLifecycleOwner) {
            binding.progress.visibility = View.GONE
          //  binding.swipeRefresh.isEnabled = true
           // arrayList = it
            adapterUserApp.setArray(it)


            if (it.size == 0){
                binding.viewEmpty.visibility = View.VISIBLE
                binding.listAppUser.visibility = View.GONE
            }else {
                binding.listAppUser.visibility = View.VISIBLE
                binding.viewEmpty.visibility = View.GONE
            }
            Thread(Runnable {
                try {

                for (appModel in it ) {
                        val appName = appModel.applicationInfo?.let { it1 ->
                            pm?.getApplicationLabel(it1).toString()
                        }
                        appModel.appTitle = appName ?: "App"
                        arrayList.add(appModel)
                }

                } catch (re : RuntimeException) { }

                finish = true
                /* requireActivity().invalidateOptionsMenu()*/
            }).start()
        }




      /*  binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false;
            binding.swipeRefresh.isEnabled = false
            binding.listAppUser.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
            queryUserApp.readByRxJava(this,requireActivity())
        }*/


    }


    private fun isSystem (applicationInfo: ApplicationInfo) : Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    /*override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {
        binding.progress.visibility = View.GONE
       // binding.swipeRefresh.isEnabled = true
        finish = true
        if (arrayList.size == 0){
            binding.viewEmpty.visibility = View.VISIBLE
            binding.listAppUser.visibility = View.GONE
        }else {
            binding.listAppUser.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
        }
    }

    override fun onNext(t: ArrayList<AppModel>) {
        arrayList = t
        adapterUserApp.setArray(t)
    }*/

    override fun onClick(appModel: AppModel) {
        try {
            val bottomDialogApp = BottomDialogApp(appModel,this)
            bottomDialogApp.show(requireActivity().supportFragmentManager,bottomDialogApp.tag)

        }catch (re : RuntimeException) {

        }
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<AppModel> = ArrayList<AppModel>()
        for (item in arrayList) {
            if (item.appTitle.lowercase(Locale.ROOT).contains(text.lowercase(Locale.getDefault()))) {
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
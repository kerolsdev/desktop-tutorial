package com.kerolsmm.appmanager.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.trace
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import com.kerolsmm.appmanager.R
import com.kerolsmm.appmanager.activities.SystemAppsUsageActivity
import com.kerolsmm.appmanager.adapters.AdapterNetwork
import com.kerolsmm.appmanager.databinding.FragmentNetworkBinding
import com.kerolsmm.appmanager.dialogs.BottomDialogFilter
import com.kerolsmm.appmanager.dialogs.NetworkDialog
import com.kerolsmm.appmanager.functions.AppDataUsageModel
import com.kerolsmm.appmanager.functions.MvvmData
import com.kerolsmm.appmanager.functions.NetworkStatsHelper
import com.kerolsmm.appmanager.functions.QueryAppNetwork
import com.kerolsmm.appmanager.functions.Values
import com.kerolsmm.appmanager.model.AppModel
import com.kerolsmm.appmanager.mvvm.MvvmBottomApp
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.Locale


public  class NetworkFragment : Fragment(), Observer<HashMap<String,ArrayList<AppDataUsageModel>>>,
    AdapterNetwork.ClickNetwork, BottomDialogFilter.ClickNetworkFilter {


    private val handler : Handler = Handler(Looper.getMainLooper())
    private lateinit var binding : FragmentNetworkBinding
    private lateinit var adapterNetwork: AdapterNetwork
    private var mLastClickTime: Long = 0

    private val mvvmBottomApp : MvvmBottomApp by activityViewModels()

    private var systemApps: ArrayList<AppDataUsageModel> = ArrayList()
    private var userApps: ArrayList<AppDataUsageModel> = ArrayList()

    var session : Int = Values.SESSION_TODAY
    var type : Int = Values.TYPE_WIFI

    private val queryAppNetwork = QueryAppNetwork()

    private var isComplete : Boolean = false

    private val mvvmData : MvvmData by activityViewModels()

    private  var arrayList : ArrayList<AppDataUsageModel> = ArrayList()

    private var finish : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNetworkBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context != null && activity != null) {

            finish = false
            adapterNetwork = AdapterNetwork(
                requireContext(), this,
                ArrayList<AppDataUsageModel>()
            )
            binding.listNetworks.adapter = adapterNetwork


            binding.loadingApp.visibility = View.VISIBLE
            binding.ViewMain.visibility = View.GONE
            binding.Filter.visibility = View.GONE
            binding.viewEmpty.visibility = View.GONE
            binding.swipeRefresh.isEnabled = false

            
            queryAppNetwork.appNetWork(this, requireActivity(), session, type)


            binding.swipeRefresh.setOnRefreshListener {
                try {
                    finish = false
                    binding.swipeRefresh.isRefreshing = false;
                    binding.swipeRefresh.isEnabled = false
                    binding.loadingApp.visibility = View.VISIBLE
                    binding.ViewMain.visibility = View.GONE
                    binding.Filter.visibility = View.GONE
                    binding.viewEmpty.visibility = View.GONE
                    queryAppNetwork.appNetWork(this, requireActivity(), session, type)

                } catch (re: RuntimeException) {

                }
            }


            binding.Filter.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return@setOnClickListener;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (isComplete) {
                    val filterDialog = BottomDialogFilter(this, session, type)
                    filterDialog.show(requireActivity().supportFragmentManager, filterDialog.tag)
                }
            }


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

        }

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {
        isComplete = true
        binding.swipeRefresh.isEnabled = true
        binding.loadingApp.visibility = View.GONE
        binding.Filter.visibility = View.VISIBLE
        binding.ViewMain.visibility = View.VISIBLE
        if (userApps.isEmpty()) {
            binding.viewEmpty.visibility = View.VISIBLE
            binding.ViewMain.visibility = View.GONE
        } else {
            binding.viewEmpty.visibility = View.GONE
        }

    }

    override fun onNext(t: HashMap<String,ArrayList<AppDataUsageModel>>) {
        systemApps =   ArrayList( t["system"] ?: ArrayList())
        userApps  =    ArrayList(t["user"]   ?: ArrayList())
        if (arrayList.isNotEmpty()) {
            arrayList.clear()
        }
        Thread(Runnable {
            try {
                val pm: PackageManager? = context?.packageManager

                for (appModel in userApps) {
                    val appM = AppDataUsageModel()
                    appM.appName =  appModel.appName
                    appM.packageName = appModel.packageName
                    appM.uid = appModel.uid
                    appM.sentMobile = appModel.sentMobile
                    appM.receivedMobile =  appModel.receivedMobile
                    appM.session = appModel.session
                    appM.type = appModel.type
                    appM.progress = appModel.progress
                    appM.app = appModel.app
                    if (appM.appName == null) {
                        val appName = appModel.app?.let { it1 ->
                            pm?.getApplicationLabel(it1).toString()
                        }
                        appM.appName = appName ?: "App"
                    }
                    arrayList.add(appM)
                }

            } catch (re: RuntimeException) {

            }

            finish = true
            /* requireActivity().invalidateOptionsMenu()*/
        }).start()

        adapterNetwork.setArray(userApps)
    }

    override fun onClick(model: AppDataUsageModel) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (model.packageName == getString(R.string.package_system)) {
            val intent = Intent(requireActivity(),SystemAppsUsageActivity::class.java)
            intent.putExtra("system",systemApps)
            requireActivity().startActivity(intent)
        } else {
            val networkDialog = NetworkDialog()
            networkDialog.show(requireActivity().supportFragmentManager,networkDialog.tag)
            mvvmBottomApp.setValueAppModelNetwork(model)

        }
    }

    override fun click(session: Int, type: Int) {
        this.session = session
        this.type = type
        binding.loadingApp.visibility = View.VISIBLE
        binding.ViewMain.visibility = View.GONE
        binding.Filter.visibility = View.GONE
        binding.viewEmpty.visibility = View.GONE
        binding.swipeRefresh.isEnabled = false
        queryAppNetwork.appNetWork(this,requireActivity(),session, type)

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<AppDataUsageModel> = ArrayList<AppDataUsageModel>()
        for (item in arrayList) {
            if (item.appName?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.getDefault())) == true) {
                filteredList.add(item)
            }
        }
        // Log.e("TAG", "fiAsadadsalter: ", )
        adapterNetwork.setArray(filteredList)
    }

}